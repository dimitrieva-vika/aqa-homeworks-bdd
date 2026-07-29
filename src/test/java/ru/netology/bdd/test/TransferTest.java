package ru.netology.bdd.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {

    private DashboardPage dashboardPage;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    @BeforeAll
    static void setUpAll() {
        // Configuration.headless = true; // для headless режима в CI
    }

    @BeforeEach
    void setUp() {
        // 1. Открываем страницу логина
        var loginPage = open("http://localhost:9999", LoginPage.class);

        // 2. Вводим логин и пароль
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);

        // 3. Вводим код подтверждения
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);

        // 4. Получаем данные карт
        firstCard = DataHelper.getFirstCardInfo();
        secondCard = DataHelper.getSecondCardInfo();
    }

    @Test
    @DisplayName("Should transfer money from first card to second card")
    void shouldTransferFromFirstToSecond() {
        // 1. Получаем балансы ДО перевода
        int firstCardBalanceBefore = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceBefore = dashboardPage.getCardBalance(secondCard.getId());

        // 2. Считаем сумму перевода (половина баланса первой карты)
        int amount = DataHelper.calculateTransferAmount(firstCardBalanceBefore);

        // 3. Считаем ОЖИДАЕМЫЕ балансы после перевода
        int expectedFirstBalance = firstCardBalanceBefore - amount;
        int expectedSecondBalance = secondCardBalanceBefore + amount;

        // 4. Выполняем перевод
        var transferPage = dashboardPage.chooseCardForDeposit(secondCard.getId());
        dashboardPage = transferPage.transfer(amount, firstCard.getNumber());

        // 5. Получаем ФАКТИЧЕСКИЕ балансы после перевода
        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCard.getId());

        // 6. Сравниваем ожидаемые и фактические балансы
        assertEquals(expectedFirstBalance, firstCardBalanceAfter,
                "Баланс первой карты должен уменьшиться на сумму перевода");
        assertEquals(expectedSecondBalance, secondCardBalanceAfter,
                "Баланс второй карты должен увеличиться на сумму перевода");
    }

    @Test
    @DisplayName("Should transfer money from second card to first card")
    void shouldTransferFromSecondToFirst() {
        // 1. Получаем балансы ДО перевода
        int firstCardBalanceBefore = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceBefore = dashboardPage.getCardBalance(secondCard.getId());

        // 2. Считаем сумму перевода (половина баланса второй карты)
        int amount = DataHelper.calculateTransferAmount(secondCardBalanceBefore);

        // 3. Считаем ОЖИДАЕМЫЕ балансы после перевода
        int expectedFirstBalance = firstCardBalanceBefore + amount;
        int expectedSecondBalance = secondCardBalanceBefore - amount;

        // 4. Выполняем перевод
        var transferPage = dashboardPage.chooseCardForDeposit(firstCard.getId());
        dashboardPage = transferPage.transfer(amount, secondCard.getNumber());

        // 5. Получаем ФАКТИЧЕСКИЕ балансы после перевода
        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCard.getId());

        // 6. Сравниваем ожидаемые и фактические балансы
        assertEquals(expectedFirstBalance, firstCardBalanceAfter,
                "Баланс первой карты должен увеличиться на сумму перевода");
        assertEquals(expectedSecondBalance, secondCardBalanceAfter,
                "Баланс второй карты должен уменьшиться на сумму перевода");
    }

    @Test
    @DisplayName("Should transfer the entire balance")
    void shouldTransferEntireBalance() {
        // 1. Получаем балансы ДО перевода
        int firstCardBalanceBefore = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceBefore = dashboardPage.getCardBalance(secondCard.getId());

        // 2. Считаем сумму перевода (весь баланс первой карты)
        int amount = DataHelper.calculateFullTransferAmount(firstCardBalanceBefore);

        // 3. Считаем ОЖИДАЕМЫЕ балансы после перевода
        int expectedFirstBalance = 0;
        int expectedSecondBalance = secondCardBalanceBefore + amount;

        // 4. Выполняем перевод
        var transferPage = dashboardPage.chooseCardForDeposit(secondCard.getId());
        dashboardPage = transferPage.transfer(amount, firstCard.getNumber());

        // 5. Получаем ФАКТИЧЕСКИЕ балансы после перевода
        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCard.getId());

        // 6. Сравниваем ожидаемые и фактические балансы
        assertEquals(expectedFirstBalance, firstCardBalanceAfter,
                "Баланс первой карты должен стать 0");
        assertEquals(expectedSecondBalance, secondCardBalanceAfter,
                "Баланс второй карты должен увеличиться на всю сумму");
    }

    @Test
    @DisplayName("Should show error when amount exceeds balance")
    void shouldShowErrorWhenAmountExceedsBalance() {
        // 1. Получаем балансы ДО перевода
        int firstCardBalanceBefore = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceBefore = dashboardPage.getCardBalance(secondCard.getId());

        // 2. Сумма перевода превышает баланс первой карты
        int amount = DataHelper.calculateExceedAmount(firstCardBalanceBefore);

        // 3. Ожидаемые балансы - НЕ ДОЛЖНЫ ИЗМЕНИТЬСЯ (так как должна быть ошибка)
        int expectedFirstBalance = firstCardBalanceBefore;
        int expectedSecondBalance = secondCardBalanceBefore;

        // 4. Выполняем перевод
        var transferPage = dashboardPage.chooseCardForDeposit(secondCard.getId());
        dashboardPage = transferPage.transfer(amount, firstCard.getNumber());

        // 5. Проверяем наличие сообщения об ошибке
        // БАГ #1: Приложение не показывает ошибку, тест упадет
        dashboardPage.verifyErrorNotification("Ошибка! Недостаточно средств на карте");

        // 6. Получаем ФАКТИЧЕСКИЕ балансы после перевода
        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCard.getId());

        // 7. Проверяем, что балансы НЕ ИЗМЕНИЛИСЬ
        // Если баланс изменился (ушел в минус), тест упадет
        assertEquals(expectedFirstBalance, firstCardBalanceAfter,
                "Баланс первой карты не должен измениться при ошибке");
        assertEquals(expectedSecondBalance, secondCardBalanceAfter,
                "Баланс второй карты не должен измениться при ошибке");
    }

    @Test
    @DisplayName("Should show error when amount is zero")
    void shouldShowErrorWhenAmountIsZero() {
        // 1. Получаем балансы ДО перевода
        int firstCardBalanceBefore = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceBefore = dashboardPage.getCardBalance(secondCard.getId());

        // 2. Сумма перевода = 0 (невалидная сумма)
        int amount = 0;

        // 3. Ожидаемые балансы - НЕ ДОЛЖНЫ ИЗМЕНИТЬСЯ (так как должна быть ошибка)
        int expectedFirstBalance = firstCardBalanceBefore;
        int expectedSecondBalance = secondCardBalanceBefore;

        // 4. Выполняем перевод
        var transferPage = dashboardPage.chooseCardForDeposit(secondCard.getId());
        dashboardPage = transferPage.transfer(0, firstCard.getNumber());

        // 5. Проверяем наличие сообщения об ошибке
        // БАГ #3: Приложение не показывает ошибку, тест упадет
        dashboardPage.verifyErrorNotification("Сумма должна быть больше 0");

        // 6. Получаем ФАКТИЧЕСКИЕ балансы после перевода
        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCard.getId());
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCard.getId());

        // 7. Проверяем, что балансы НЕ ИЗМЕНИЛИСЬ
        assertEquals(expectedFirstBalance, firstCardBalanceAfter,
                "Баланс первой карты не должен измениться при ошибке");
        assertEquals(expectedSecondBalance, secondCardBalanceAfter,
                "Баланс второй карты не должен измениться при ошибке");
    }
}