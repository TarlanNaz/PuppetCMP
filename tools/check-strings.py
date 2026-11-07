#!/usr/bin/env python3
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
RES = ROOT / "app/shared/src/commonMain/composeResources"
BASE = "values"


def keys(path: Path) -> set[str]:
    return set(re.findall(r'name="([^"]+)"', path.read_text(encoding="utf-8")))


def main() -> int:
    base_file = RES / BASE / "strings.xml"
    if not base_file.exists():
        print(f"нет базовой локали: {base_file.relative_to(ROOT)}")
        return 1

    base = keys(base_file)
    others = sorted(p for p in RES.glob("values-*/strings.xml"))
    if not others:
        print("нет ни одной второй локали — нечего сверять")
        return 1

    failed = False
    for path in others:
        locale = path.parent.name
        other = keys(path)
        missing = sorted(base - other)
        extra = sorted(other - base)
        if missing or extra:
            failed = True
            print(f"{locale}: расхождений {len(missing) + len(extra)}")
            for k in missing:
                print(f"  нет в {locale}: {k}")
            for k in extra:
                print(f"  нет в {BASE}: {k}")
        else:
            print(f"{locale}: 0 расхождений, ключей {len(other)}")

    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
