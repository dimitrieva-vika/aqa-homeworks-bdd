package ru.netology.bdd.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {

    private DashboardPage dashboardPage;

    @BeforeAll
    static void setUpAll() {
        // Configuration.headless = true;
    }

    @BeforeEach
    void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Should transfer money from first card to second card")
    void shouldTransferFromFirstToSecond() {
        List<String> cardIds = dashboardPage.getAllCardIds();
        assertEquals(2, cardIds.size(), "Должно быть 2 карты");

        String firstCardId = cardIds.get(0);
        String secondCardId = cardIds.get(1);

        int firstCardBalanceBefore = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalanceBefore = dashboardPage.getCardBalance(secondCardId);
        int amount = firstCardBalanceBefore / 2;

        var transferPage = dashboardPage.chooseCardForDeposit(secondCardId);
        dashboardPage = transferPage.transfer(amount, DataHelper.getFirstCardNumber());

        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        assertEquals(firstCardBalanceBefore - amount, firstCardBalanceAfter);
        assertEquals(secondCardBalanceBefore + amount, secondCardBalanceAfter);
    }

    @Test
    @DisplayName("Should transfer money from second card to first card")
    void shouldTransferFromSecondToFirst() {
        List<String> cardIds = dashboardPage.getAllCardIds();
        assertEquals(2, cardIds.size(), "Должно быть 2 карты");

        String firstCardId = cardIds.get(0);
        String secondCardId = cardIds.get(1);

        int firstCardBalanceBefore = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalanceBefore = dashboardPage.getCardBalance(secondCardId);
        int amount = secondCardBalanceBefore / 2;

        var transferPage = dashboardPage.chooseCardForDeposit(firstCardId);
        dashboardPage = transferPage.transfer(amount, DataHelper.getSecondCardNumber());

        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        assertEquals(firstCardBalanceBefore + amount, firstCardBalanceAfter);
        assertEquals(secondCardBalanceBefore - amount, secondCardBalanceAfter);
    }

    @Test
    @DisplayName("Should transfer the entire balance")
    void shouldTransferEntireBalance() {
        List<String> cardIds = dashboardPage.getAllCardIds();
        assertEquals(2, cardIds.size(), "Должно быть 2 карты");

        String firstCardId = cardIds.get(0);
        String secondCardId = cardIds.get(1);

        int firstCardBalanceBefore = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalanceBefore = dashboardPage.getCardBalance(secondCardId);
        int amount = firstCardBalanceBefore;

        var transferPage = dashboardPage.chooseCardForDeposit(secondCardId);
        dashboardPage = transferPage.transfer(amount, DataHelper.getFirstCardNumber());

        int firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        assertEquals(0, firstCardBalanceAfter);
        assertEquals(secondCardBalanceBefore + amount, secondCardBalanceAfter);
    }

    @Disabled("Bug #1: При переводе суммы, превышающей баланс карты, баланс становится отрицательным")
    @Test
    @DisplayName("Should show error when amount exceeds balance")
    void shouldShowErrorWhenAmountExceedsBalance() {
        List<String> cardIds = dashboardPage.getAllCardIds();
        String firstCardId = cardIds.get(0);
        String secondCardId = cardIds.get(1);

        int firstCardBalance = dashboardPage.getCardBalance(firstCardId);
        var transferPage = dashboardPage.chooseCardForDeposit(secondCardId);
        dashboardPage = transferPage.transfer(firstCardBalance, DataHelper.getFirstCardNumber());

        int amount = 1000;
        transferPage = dashboardPage.chooseCardForDeposit(secondCardId);
        dashboardPage = transferPage.transfer(amount, DataHelper.getFirstCardNumber());

        dashboardPage.verifyErrorNotification("Ошибка! Недостаточно средств на карте");
    }

    @Disabled("Bug #3: Отсутствует валидация нулевой суммы перевода")
    @Test
    @DisplayName("Should show error when amount is zero")
    void shouldShowErrorWhenAmountIsZero() {
        List<String> cardIds = dashboardPage.getAllCardIds();
        String firstCardId = cardIds.get(0);
        String secondCardId = cardIds.get(1);

        var transferPage = dashboardPage.chooseCardForDeposit(secondCardId);
        dashboardPage = transferPage.transfer(0, DataHelper.getFirstCardNumber());

        dashboardPage.verifyErrorNotification("Сумма должна быть больше 0");
    }
}