package ru.netology.bdd.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement fromField = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private SelenideElement heading = $(".heading_size_xl");

    public TransferPage() {
        amountField.shouldBe(visible);
        heading.shouldHave(com.codeborne.selenide.Condition.text("Пополнение карты"));
    }

    public DashboardPage transfer(int amount, String fromCardNumber) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(fromCardNumber);
        transferButton.click();
        return new DashboardPage();
    }

    public DashboardPage cancel() {
        cancelButton.click();
        return new DashboardPage();
    }
}