import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

RES = Path(__file__).resolve().parent.parent / "app/shared/src/commonMain/composeResources"
ARG = re.compile(r"%(\d+)\$[sd]")


def load(folder):
    root = ET.parse(RES / folder / "strings.xml").getroot()
    return {s.get("name"): set(ARG.findall(s.text or "")) for s in root.findall("string")}


def main():
    base = load("values")
    ok = True
    for folder in sorted(p.name for p in RES.glob("values-*")):
        other = load(folder)
        problems = []
        for key in sorted(base.keys() - other.keys()):
            problems.append(f"  нет в {folder}: {key}")
        for key in sorted(other.keys() - base.keys()):
            problems.append(f"  нет в values: {key}")
        for key in sorted(base.keys() & other.keys()):
            if base[key] != other[key]:
                problems.append(f"  {key}: разные аргументы")
        if problems:
            ok = False
            print(f"{folder}: расхождений {len(problems)}")
            print("\n".join(problems))
        else:
            print(f"{folder}: 0 расхождений, ключей {len(other)}")
    sys.exit(0 if ok else 1)


if __name__ == "__main__":
    main()
