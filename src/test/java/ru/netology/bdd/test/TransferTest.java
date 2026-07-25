package ru.netology.bdd.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;
import ru.netology.bdd.page.VerificationPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {

    private DashboardPage dashboardPage;

    @BeforeAll
    static void setUpAll() {
        Configuration.headless = true;
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
        var firstCardId = DataHelper.getFirstCardId();
        var secondCardId = DataHelper.getSecondCardId();

        var firstCardBalanceBefore = dashboardPage.getCardBalance(firstCardId);
        var secondCardBalanceBefore = dashboardPage.getCardBalance(secondCardId);
        var amount = 1000;

        var transferPage = dashboardPage.chooseCardForTransfer(firstCardId);
        dashboardPage = transferPage.transfer(amount, firstCardId, secondCardId);

        var firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        var secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        assertEquals(firstCardBalanceBefore - amount, firstCardBalanceAfter);
        assertEquals(secondCardBalanceBefore + amount, secondCardBalanceAfter);
    }

    @Test
    @DisplayName("Should transfer money from second card to first card")
    void shouldTransferFromSecondToFirst() {
        var firstCardId = DataHelper.getFirstCardId();
        var secondCardId = DataHelper.getSecondCardId();

        var firstCardBalanceBefore = dashboardPage.getCardBalance(firstCardId);
        var secondCardBalanceBefore = dashboardPage.getCardBalance(secondCardId);
        var amount = 500;

        var transferPage = dashboardPage.chooseCardForTransfer(secondCardId);
        dashboardPage = transferPage.transfer(amount, secondCardId, firstCardId);

        var firstCardBalanceAfter = dashboardPage.getCardBalance(firstCardId);
        var secondCardBalanceAfter = dashboardPage.getCardBalance(secondCardId);

        assertEquals(firstCardBalanceBefore + amount, firstCardBalanceAfter);
        assertEquals(secondCardBalanceBefore - amount, secondCardBalanceAfter);
    }

    @Test
    @DisplayName("Should show error when amount exceeds balance")
    void shouldShowErrorWhenAmountExceedsBalance() {
        var firstCardId = DataHelper.getFirstCardId();
        var secondCardId = DataHelper.getSecondCardId();

        var firstCardBalance = dashboardPage.getCardBalance(firstCardId);
        var amount = firstCardBalance + 1000;

        var transferPage = dashboardPage.chooseCardForTransfer(firstCardId);
        dashboardPage = transferPage.transfer(amount, firstCardId, secondCardId);

        $("[data-test-id='error-notification']").shouldBe(visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Недостаточно средств на карте"));
    }

    @Test
    @DisplayName("Should show error when amount is zero or negative")
    void shouldShowErrorWhenAmountIsZeroOrNegative() {
        var firstCardId = DataHelper.getFirstCardId();
        var secondCardId = DataHelper.getSecondCardId();
        var amount = 0;

        var transferPage = dashboardPage.chooseCardForTransfer(firstCardId);
        dashboardPage = transferPage.transfer(amount, firstCardId, secondCardId);

        $("[data-test-id='error-notification']").shouldBe(visible);
    }
}