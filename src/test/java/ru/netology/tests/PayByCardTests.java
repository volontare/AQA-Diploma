package ru.netology.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.pages.MainPage;
import ru.netology.pages.PurchasePage;

import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;

public class PayByCardTests extends BaseUITest {

    MainPage mainPage = new MainPage();
    PurchasePage purchasePage = new PurchasePage();

    @BeforeEach
    void setUpForPayWithCard() {
        mainPage.payWithCard();
    }

    // Успешная покупка, карта со статусом APPROVED (тест прошел)
    @Test
    public void shouldSuccessPayIfValidApprovedCard() {
        val cardData = getApprovedCardNumber();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.successResultNotification();

        val expectedStatus = "APPROVED";
        val actualStatus = getCardStatusForPayWithCard();
        assertEquals(expectedStatus, actualStatus);

        val expectedAmount = "4500000";
        val actualAmount = getAmountPurchase();
        assertEquals(expectedAmount, actualAmount);

        val transactionIdExpected = getTransactionId();
        val paymentIdActual = getPaymentIdForPayWithCard();
        assertNotNull(transactionIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(transactionIdExpected, paymentIdActual);
    }

    // Неуспешная покупка, карта  со статусом DECLINED (тест не прошел, оплата успешная)
    @Test
    public void shouldFailurePayIfValidDeclinedCards() {
        val cardData = getDeclinedCardNumber();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.failureResultNotification();

        val expectedStatus = "DECLINED";
        val actualStatus = getCardStatusForPayWithCard();
        assertEquals(expectedStatus, actualStatus);

        val transactionIdExpected = getTransactionId();
        val paymentIdActual = getPaymentIdForPayWithCard();
        assertNotNull(transactionIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(transactionIdExpected, paymentIdActual);
    }

    // Пустое поле Номер карты (тест не прошел, появляется ошибка "Неверный формат" вместо "Поле обязательно для
    // заполнения")
    @Test
    public void shouldHaveEmptyCardNumber() {
        val cardData = getEmptyCardNumber();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    // Ввод в поле Номер карты недосточного количества цифр (тест прошел, но лучше указывать ошибку "Указано
    // недостаточно цифр")
    @Test
    public void shouldHaveCardNumberIfFewDigits() {
        val cardData = getCardNumberIfFewDigits();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Оплата картой, которой нет в БД (тест прошел)
    @Test
    public void shouldHaveCardNumberIfOutOfBase() {
        val cardData = getCardNumberIfNotExistInBase();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.failureResultNotification();
    }

    // Оплата картой разных форматов,которых нет в БД (тест не проходит, если количество цифр в карте меньше или больше
    // 16, хотя существуют карты от 13 до 19 цифр)
    @Test
    public void shouldHaveCardNumberIfFakerCard() {
        val cardData = getCardNumberFaker();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.failureResultNotification();
    }

    // Пустое поле Месяц (тест не прошел,неверная ошибка "Неверный формат" вместо "Поле обязательно для заполнения")
    @Test
    public void shouldHaveEmptyExpireMonth() {
        val cardData = getEmptyExpireMonth();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    // Ввод в поле Месяц нулевых значений (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveExpireMonthWithZero() {
        val cardData = getExpireMonthWithZero();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц значения больше 12 (тест прошел, но в качестве пожелания лучше указать ошибку "Введиту срок
    // действия как указано на карте")
    @Test
    public void shouldHaveExpireMonthMore12() {
        val cardData = getExpireMonthMore12();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц 1 цифры (тест прошел)
    @Test
    public void shouldHaveExpireMonthWithOneDigit() {
        val cardData = getExpireMonthWithOneDigit();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Пустое поле Год (тест не прошел, ошибка Неверный формат, а не Поле обязательно для заполнения)
    @Test
    public void shouldHaveEmptyExpireYear() {
        val cardData = getEmptyExpireYear();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    // Истек срок действия карты (тест не прошел, неверная ошибка "Неверно указан срок действия карты" вместо
    // "Истек срок действия карты")
    @Test
    public void shouldHaveExpireYearBeforeCurrentYear() {
        val cardData = getExpiredCard();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.expiredDatePassError();
    }

    // Год намного позднее текущего (тест прошел)
    @Test
    public void shouldHaveExpireYearInTheFarFuture() {
        val cardData = getInvalidYearIfInTheFarFuture();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.invalidCardExpirationDateError();
    }

    // Поле Год с одной цифрой (тест прошел)
    @Test
    public void shouldHaveExpireYearWithOneDigit() {
        val cardData = getExpireYearWithOneDigit();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Поле Год с нулевыми значениями (тест прошел)
    @Test
    public void shouldHaveExpireYearWithZero() {
        val cardData = getExpireYearWithZero();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.expiredDatePassError();
    }

    // Пустое поле Владелец (тест прошел)
    @Test
    public void shouldHaveEmptyCardholderName() {
        val cardData = getEmptyCardHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    // Ввод в поле Владелец только имени (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveCardHolderWithoutName() {
        val cardData = getCardHolderWithoutName();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Указание Владельца кириллицей (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveRussianCardHolder() {
        val cardData = getRussianCardHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Ввод в поле Владелец цифр (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveDigitsInCardHolder() {
        val cardData = getDigitsInCardHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Поле Владелец с указанием спецсимволов (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveSpecialCharactersInCardHolder() {
        val cardData = getSpecialCharactersInCardHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества пробелов между фамилией и именем (тест не прошел, оплата успешная.
    // Необходима доработка в виде появляющейся ошибки "Допустим только один пробел между именем и фамилией")
    @Test
    public void shouldHaveManySpacesInCardHolder() {
        val cardData = getCardHolderWithManySpaces();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества символов (тест не прошел, оплата успешная. Необходима доработка в виде
    // появляющейся ошибки "Допустимо не более ** символов)
    @Test
    public void shouldHaveCardHolderWithManyLetters() {
        val cardData = getCardHolderWithManyLetters();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Указание в поле Владелец фамилии через дефис (тест прошел)
    @Test
    public void shouldHaveCardHolderSurnameWithDash() {
        val cardData = getCardHolderSurnameWithDash();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.successResultNotification();
    }

    // Указание в поле Владелец имени через дефис (тест прошел)
    @Test
    public void shouldHaveCardHolderNameWithDash() {
        val cardData = getCardHolderNameWithDash();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.successResultNotification();
    }

    // Пустое поле CVC-код (тест не прошел,ошибка появляется под полем Владелец)
    @Test
    public void shouldHaveEmptyCvcCode() {
        val cardData = getEmptyCvcCode();
        purchasePage.completedPurchaseForm(cardData);
        final ElementsCollection fieldSub = $$(".input__sub");
        final SelenideElement cvvFieldSub = fieldSub.get(2);
        cvvFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    // Поле CVC-код с 2 цифрами (тест прошел, но необходима доработка в виде сообщения "Поле должно состоять из 3 цифр")
    @Test
    public void shouldHaveCvcCodeWithTwoDigits() {
        val cardData = getCvcCodeWithTwoDigits();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Поле CVC-код с нулевыми значениями (тест не прошел, оплата успешная)
    @Test
    public void shouldFailurePaymentIfCvcCodeWithZero() {
        val cardData = getCvcCodeWithZero();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Все поля формы пустые (тест не прошел, ошибка "Неверный формат" во всех полях кроме поля Владелец)
    @Test
    public void shouldHaveEmptyAllFields() {
        val cardData = getCardDataIfEmptyAllFields();
        purchasePage.completedPurchaseForm(cardData);
        final ElementsCollection fieldSub = $$(".input__sub");
        final SelenideElement cardNumberFieldSub = fieldSub.get(1);
        final SelenideElement monthFieldSub = fieldSub.get(2);
        final SelenideElement yearFieldSub = fieldSub.get(3);
        final SelenideElement cardholderFieldSub = fieldSub.get(4);
        final SelenideElement cvvFieldSub = fieldSub.get(5);
        cardNumberFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        monthFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        yearFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        cardholderFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        cvvFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }
}