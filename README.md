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
| shouldTransferFromFirstToSecond | ❌ FAILED | Перевод с первой карты на вторую |
| shouldTransferFromSecondToFirst | ❌ FAILED | Перевод со второй карты на первую |
| shouldShowErrorWhenAmountExceedsBalance | ❌ FAILED | Ошибка при превышении баланса (БАГ #1) |
| shouldShowErrorWhenAmountIsZeroOrNegative | ❌ FAILED | Ошибка при нулевой/отрицательной сумме (БАГ #2) |

**Итог:** ⚠️ 0 тестов пройдены, 4 теста упали (найдены баги)

### Обнаруженные баги

1. **[Issue #1](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/issues/1)** - При превышении баланса не показывается сообщение об ошибке
2. **[Issue #2](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/issues/2)** - Отсутствует проверка на нулевую или отрицательную сумму перевода
3. **[Issue #3](https://github.com/dimitrieva-vika/aqa-homeworks-bdd/issues/3)** - ID карт захардкожены в тестах

## Запуск тестов

### Локальный запуск
1. Запустите SUT в тестовом режиме:

java -jar ./artifacts/app-ibank-build-for-testers.jar -P:profile=test
2. В другом терминале выполните:
./gradlew test