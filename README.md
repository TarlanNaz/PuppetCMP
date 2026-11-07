# Каталог Puppet Forge — каркас на Compose Multiplatform (ЛР1)

Каталог модулей Puppet Forge. Сети пока нет: 20 модулей лежат в моках
(`data/MockModules.kt`).

## Запуск

```bash
./gradlew :app:desktopApp:run                  # основной таргет, интерфейс на русском
./gradlew :app:desktopApp:run -Plocale=en      # тот же интерфейс на английском
python3 tools/check-strings.py                 # паритет ключей локалей
```

## Устройство

```text
app/shared/src/commonMain/kotlin/com/example/puppet/
  Screen.kt              маршруты: sealed interface Screen { List, Detail(slug) }
  App.kt                 сборка зависимостей, тема, шапка
  domain/                PuppetModule, ModuleRepository, поиск и соседи
  data/                  моки и ModuleRepositoryImpl
  ui/navigation/         Navigator (бэкстек) и AppNavDisplay (NavDisplay)
  ui/model/              UI-модели и мапперы
  ui/components/         AppScaffold, CardSurface, ModuleAvatar
  ui/screens/list/       состояние, намерения, ViewModel, фабрика, экран
  ui/screens/detail/     то же для детали
```

- Навигация — Navigation 3, мультиплатформенная сборка JetBrains
  `org.jetbrains.androidx.navigation3:navigation3-ui`. Заданы прямой переход,
  возврат и предиктивный возврат.
- Экраны — MVI: вниз состояние (`StateFlow` из ViewModel), вверх намерения
  (`onIntent`). ViewModel в экран не передаётся.
- Подписи — только в `composeResources`: `values` (русский) и `values-en`.
- Тема — токены `MaterialTheme`, светлая и тёмная, переключатель в шапке.
- Отчёт компилятора Compose пишется в `app/shared/build/compose_compiler`.
