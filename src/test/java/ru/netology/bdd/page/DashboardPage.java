package ru.netology.bdd.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private SelenideElement heading = $("[data-test-id='dashboard']");
    private ElementsCollection cards = $$(".list__item div[data-test-id]");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible, Duration.ofSeconds(15));
    }

    public List<String> getAllCardIds() {
        List<String> cardIds = new ArrayList<>();
        for (SelenideElement card : cards) {
            String cardId = card.getAttribute("data-test-id");
            if (cardId != null && !cardId.isEmpty()) {
                cardIds.add(cardId);
            }
        }
        return cardIds;
    }

    public int getCardBalance(String cardId) {
        var cardElement = getCardElementById(cardId);
        var text = cardElement.text();
        return extractBalance(text);
    }

    public SelenideElement getCardElementById(String cardId) {
        return $("[data-test-id='" + cardId + "']");
    }

    public TransferPage chooseCardForDeposit(String cardId) {
        var cardElement = getCardElementById(cardId);
        cardElement.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldBe(visible);
        errorNotification.$(".notification__content").shouldHave(text(expectedText));
    }

    public void verifyErrorNotification() {
        errorNotification.shouldBe(visible);
    }

    public void verifyNoErrorNotification() {
        errorNotification.shouldNotBe(visible);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish)
                .replaceAll("\\s", "")
                .replaceAll(",", "")
                .replaceAll("\u00A0", "");
        return Integer.parseInt(value);
    }
}