package ru.netology.tests;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.data.APIHelper.*;
import static ru.netology.data.DataHelper.getApprovedCardNumber;
import static ru.netology.data.DataHelper.getDeclinedCardNumber;

public class APITest {

    //Олата валидной картой, статус карты APPROVED
    @Test
    void shouldStatusPayWithValidApprovedCardNumber() {
        val validApprovedCardNumber = getApprovedCardNumber();
        val status = payWithCardForm(validApprovedCardNumber);
        assertTrue(status.contains("APPROVED"));
    }

    //Олата невалидной картой, статус карты DECLINED
    @Test
    void shouldStatusPayWithValidDeclinedCardNumber() {
        val validDeclinedCardNumber = getDeclinedCardNumber();
        val status = payWithCardForm(validDeclinedCardNumber);
        assertTrue(status.contains("DECLINED"));
    }

    //Олата валидной картой в кредит, статус карты APPROVED
    @Test
    void shouldStatusPayWithCreditValidApprovedCardNumber() {
        val validApprovedCardNumber = getApprovedCardNumber();
        val status = payWithCreditForm(validApprovedCardNumber);
        assertTrue(status.contains("APPROVED"));
    }

    //Олата невалидной картой в кредит, статус карты DECLINED
    @Test
    void shouldStatusPayWithCreditValidDeclinedCardNumber() {
        val validDeclinedCardNumber = getDeclinedCardNumber();
        val status = payWithCreditForm(validDeclinedCardNumber);
        assertTrue(status.contains("DECLINED"));
    }
}
