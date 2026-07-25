package ru.netology.bdd.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement fromField = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");

    public TransferPage() {
        amountField.shouldBe(visible);
    }

    public DashboardPage transfer(int amount, String fromCardId, String toCardId) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(fromCardId);
        transferButton.click();
        return new DashboardPage();
    }
}