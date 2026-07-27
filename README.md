# Домашнее задание: BDD (Page Object's)

[![CI](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/actions/workflows/gradle.yml/badge.svg)](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/actions/workflows/gradle.yml)

## Описание проекта

Автотесты для тестирования перевода средств между своими картами в интернет-банке с использованием Selenide и Page Object's.

## Технологии

- Java 11
- JUnit 5
- Selenide 6.19.1
- Lombok 1.18.36
- Gradle 8.14.5
- GitHub Actions (CI)

## Статус тестирования

| Тест | Статус |
|------|--------|
| shouldTransferFromFirstToSecond | ✅ PASSED |
| shouldTransferFromSecondToFirst | ✅ PASSED |
| shouldTransferEntireBalance | ✅ PASSED |
| shouldShowErrorWhenAmountExceedsBalance | ⏭️ SKIPPED |
| shouldShowErrorWhenAmountIsZero | ⏭️ SKIPPED |

**Итог:** ✅ 3 passed, ⏭️ 2 skipped

## Обнаруженные баги

| # | Описание | Ссылка |
|---|----------|--------|
| 1 | При превышении баланса баланс уходит в минус | [Issue #1](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/issues/1) |
| 3 | Нет валидации нулевой суммы | [Issue #3](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/issues/3) |

## Запуск тестов

1. Запустить SUT:

```bash
java -jar ./artifacts/app-ibank-build-for-testers.jar -P:profile=test
```

2. Запустить тесты:

```bash
./gradlew test --info
```