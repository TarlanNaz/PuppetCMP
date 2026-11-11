# Каталог Puppet Forge — живые данные на Compose Multiplatform (ЛР4)

Каталог модулей Puppet Forge. Данные приходят из API Puppet Forge по сети:
список грузится страницами, поиск выполняет сервер.

## Настройка

Адрес API в репозитории не хранится. Перед первой сборкой добавьте его в
`local.properties` (файл в `.gitignore`):

```properties
forge.baseUrl=<адрес API Puppet Forge, без /v3>
```

Вместо этого можно передать `-Pforge.baseUrl=…` или задать переменную окружения
`FORGE_BASE_URL`. При сборке задача `generateAppConfig` кладёт адрес в
сгенерированный `AppConfig` (каталог `build`, в git не попадает). Без адреса
сборка останавливается с понятным сообщением.

## Запуск

```bash
./gradlew :app:desktopApp:run                    # основной таргет, интерфейс на русском
./gradlew :app:desktopApp:run -Plocale=en        # тот же интерфейс на английском
./gradlew :app:androidApp:assembleDebug          # Android
./gradlew :app:webApp:wasmJsBrowserDevelopmentRun  # wasm в браузере
./gradlew :app:shared:jvmTest                    # тесты разбора ответа
python3 tools/check-strings.py                   # паритет ключей локалей
```

## Устройство

```text
app/shared/src/commonMain/kotlin/com/example/puppet/
  Screen.kt              маршруты: sealed interface Screen { List, Detail(slug) }
  App.kt                 тема, шапка, навигация
  AppGraph.kt            зависимости (ViewModel): закрывает репозиторий и HTTP-клиент
  domain/                PuppetModule, ModulesPage, ModuleRepository, соседи
  data/network/          ForgeApi (запросы) и expect platformHttpClient
  data/dto/              модели ответа API (kotlinx.serialization)
  data/                  ForgeParser (разбор и маппинг), ModuleRepositoryImpl
  ui/navigation/         Navigator (бэкстек) и AppNavDisplay (NavDisplay)
  ui/model/              UI-модели и мапперы
  ui/components/         AppScaffold, CardSurface, ModuleAvatar, LoadError
  ui/screens/list/       состояние, намерения, ViewModel, фабрика, экран
  ui/screens/detail/     то же для детали
app/shared/src/{android,jvm,ios,js,wasmJs}Main/.../data/network/
  PlatformHttpClient.*.kt  actual: движок OkHttp, Darwin или Js
```

- Сеть — Ktor Client. Движок у каждой платформы свой и выбирается через
  `expect`/`actual`: OkHttp на Android и JVM, Darwin на iOS, Js (fetch) в браузере.
- Асинхронность — корутины во `viewModelScope`. Отмена (новый поисковый запрос,
  уход с экрана) пробрасывается как `CancellationException` и ошибкой не считается.
- Разбор ответа — `kotlinx.serialization`. Модели ответа (`data/dto`) отделены от
  доменных и от UI-моделей, неизвестные поля игнорируются.
- Разбор JSON, перевод в UI-модели и склейка страниц выполняются на
  `Dispatchers.Default`, а не на UI-диспетчере.
- Кэш — только в памяти. На диск ничего не сохраняется.
- Соседи модуля — соседние места в рейтинге всего каталога по загрузкам. Если модуль
  есть в уже загруженном начале списка, они берутся оттуда без запросов; иначе позиция
  находится двоичным поиском по смещению (около 13 запросов по одной записи) и окно
  вокруг неё берётся одним запросом. Соседи грузятся отдельно от карточки модуля.
- HTTP-клиент принадлежит репозиторию, а репозиторий — `AppGraph` (ViewModel):
  при очистке хранилища ViewModel он закрывается.
- Навигация — Navigation 3, мультиплатформенная сборка JetBrains
  `org.jetbrains.androidx.navigation3:navigation3-ui`. Заданы прямой переход,
  возврат и предиктивный возврат.
- Экраны — MVI: вниз состояние (`StateFlow` из ViewModel), вверх намерения
  (`onIntent`). ViewModel в экран не передаётся. Состояния помечены `@Immutable`.
- Подписи — только в `composeResources`: `values` (русский) и `values-en`.
- Тема — токены `MaterialTheme`, светлая и тёмная, переключатель в шапке.
- Отчёт компилятора Compose пишется в `app/shared/build/compose_compiler`.
