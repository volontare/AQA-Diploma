package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PurchasePage {

    private final SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement expireMonthField = $("input[placeholder='08']");
    private final SelenideElement expireYearField = $("input[placeholder='22']");
    private final ElementsCollection fieldSet = $$(".input__control");
    private final SelenideElement cardHolderField = fieldSet.get(3);
    private final SelenideElement cvcCodeField = $("input[placeholder='999']");

    private final SelenideElement emptyField = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement incorrectFormat = $(byText("Неверный формат"));
    private final SelenideElement invalidCardExpirationDate = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement expiredDatePass = $(byText("Истёк срок действия карты"));
    private final SelenideElement successResult = $(byText("Операция одобрена Банком."));
    private final SelenideElement failureResult = $(byText("Ошибка! Банк отказал в проведении операции."));
    private final SelenideElement continueButton = $$("button").find(exactText("Продолжить"));

    public void completedPurchaseForm(DataHelper.CardData cardData) {
        cardNumberField.setValue(cardData.getNumber());
        expireMonthField.setValue(cardData.getMonth());
        expireYearField.setValue(cardData.getYear());
        cardHolderField.setValue(cardData.getHolder());
        cvcCodeField.setValue(cardData.getCvc());
        continueButton.click();
    }

    public void emptyFieldError() {
        emptyField.shouldBe(Condition.visible);
    }

    public void incorrectFormatError() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void invalidCardExpirationDateError() {
        invalidCardExpirationDate.shouldBe(Condition.visible);
    }

    public void expiredDatePassError() {
        expiredDatePass.shouldBe(Condition.visible);
    }

    public void successResultNotification() {
        successResult.shouldHave(Condition.visible, Duration.ofSeconds(15));
    }

    public void failureResultNotification() {
        failureResult.shouldHave(Condition.visible, Duration.ofSeconds(15));
    }
}
