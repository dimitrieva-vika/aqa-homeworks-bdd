package ru.netology.bdd.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransferTest {

    private DashboardPage dashboardPage;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    // Current balances, updated after each test
    private static int firstCardBalance = 10000;
    private static int secondCardBalance = 10000;

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

        firstCard = DataHelper.getFirstCardInfo();
        secondCard = DataHelper.getSecondCardInfo();

        int actualFirstBalance = dashboardPage.getCardBalance(firstCard.getId());
        int actualSecondBalance = dashboardPage.getCardBalance(secondCard.getId());

        System.out.println("=== Current State ===");
        System.out.println("Expected first card balance: " + firstCardBalance);
        System.out.println("Actual first card balance: " + actualFirstBalance);
        System.out.println("Expected second card balance: " + secondCardBalance);
        System.out.println("Actual second card balance: " + actualSecondBalance);
        System.out.println("=====================");
    }

    @Test
    @Order(1)
    @DisplayName("Should transfer money from first card to second card")
    void shouldTransferFromFirstToSecond() {
        int amount = firstCardBalance / 2;

        var transferPage = dashboardPage.chooseCardForDeposit(secondCard.getId());
        dashboardPage = transferPage.transfer(amount, firstCard.getNumber());

        firstCardBalance -= amount;
        secondCardBalance += amount;

        int actualFirstBalance = dashboardPage.getCardBalance(firstCard.getId());
        int actualSecondBalance = dashboardPage.getCardBalance(secondCard.getId());

        assertEquals(firstCardBalance, actualFirstBalance);
        assertEquals(secondCardBalance, actualSecondBalance);
    }

    @Test
    @Order(2)
    @DisplayName("Should transfer money from second card to first card")
    void shouldTransferFromSecondToFirst() {
        int amount = secondCardBalance / 2;

        var transferPage = dashboardPage.chooseCardForDeposit(firstCard.getId());
        dashboardPage = transferPage.transfer(amount, secondCard.getNumber());

        firstCardBalance += amount;
        secondCardBalance -= amount;

        int actualFirstBalance = dashboardPage.getCardBalance(firstCard.getId());
        int actualSecondBalance = dashboardPage.getCardBalance(secondCard.getId());

        assertEquals(firstCardBalance, actualFirstBalance);
        assertEquals(secondCardBalance, actualSecondBalance);
    }

    @Test
    @Order(3)
    @DisplayName("Should transfer the entire balance")
    void shouldTransferEntireBalance() {
        int amount = firstCardBalance;

        var transferPage = dashboardPage.chooseCardForDeposit(secondCard.getId());
        dashboardPage = transferPage.transfer(amount, firstCard.getNumber());

        firstCardBalance -= amount;
        secondCardBalance += amount;

        int actualFirstBalance = dashboardPage.getCardBalance(firstCard.getId());
        int actualSecondBalance = dashboardPage.getCardBalance(secondCard.getId());

        assertEquals(0, actualFirstBalance);
        assertEquals(secondCardBalance, actualSecondBalance);
    }

    @Test
    @Order(4)
    @DisplayName("Should show error when amount exceeds balance")
    void shouldShowErrorWhenAmountExceedsBalance() {
        // First, transfer all money from first card to second card
        int amountToClear = firstCardBalance;
        var transferPage = dashboardPage.chooseCardForDeposit(secondCard.getId());
        dashboardPage = transferPage.transfer(amountToClear, firstCard.getNumber());

        firstCardBalance -= amountToClear;
        secondCardBalance += amountToClear;

        // Now first card balance = 0
        // Try to transfer amount exceeding balance
        int amount = firstCardBalance + 1000; // 0 + 1000 = 1000

        transferPage = dashboardPage.chooseCardForDeposit(secondCard.getId());
        dashboardPage = transferPage.transfer(amount, firstCard.getNumber());

        // Verify error
        dashboardPage.verifyErrorNotification("Error! Insufficient funds on card");
    }

    @Test
    @Order(5)
    @DisplayName("Should show error when amount is zero")
    void shouldShowErrorWhenAmountIsZero() {
        var transferPage = dashboardPage.chooseCardForDeposit(secondCard.getId());
        dashboardPage = transferPage.transfer(0, firstCard.getNumber());

        dashboardPage.verifyErrorNotification("Amount must be greater than 0");
    }
}