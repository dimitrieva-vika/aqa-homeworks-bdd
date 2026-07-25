# Домашнее задание: BDD (Page Object's)

[![CI](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/actions/workflows/gradle.yml/badge.svg)](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/actions/workflows/gradle.yml)

## Описание проекта

Автотесты для тестирования перевода средств между своими картами в интернет-банке с использованием Selenide и Page Object's.

### Технологии

- Java 11
- JUnit 5
- Selenide 6.19.1
- Lombok 1.18.36
- Gradle 8.14.5
- GitHub Actions (CI)

### Статус тестирования

| Тест | Статус | Описание |
|------|--------|----------|
| shouldTransferFromFirstToSecond | ❌ FAILED | Перевод с первой карты на вторую (БАГ #1) |
| shouldTransferFromSecondToFirst | ❌ FAILED | Перевод со второй карты на первую (БАГ #1) |
| shouldShowErrorWhenAmountExceedsBalance | ❌ FAILED | Ошибка при превышении баланса (БАГ #2) |

**Итог:** ⚠️ 0 тестов пройдены, 3 теста упали (найдены 2 бага)

### Обнаруженные баги

1. **[Issue #1](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/issues/1)** - ID карт не совпадают (тесты не могут найти карты)
2. **[Issue #2](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/issues/2)** - При превышении баланса не показывается сообщение об ошибке

## Запуск тестов

### Локальный запуск

1. Запустите SUT в тестовом режиме:
```bash
java -jar ./artifacts/app-ibank-build-for-testers.jar -P:profile=test