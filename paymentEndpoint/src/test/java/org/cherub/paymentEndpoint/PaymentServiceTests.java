package org.cherub.paymentEndpoint;

import org.cherub.paymentEndpoint.dtos.FeePaymentDto;
import org.cherub.paymentEndpoint.models.FeePayment;
import org.cherub.paymentEndpoint.models.StudentAccount;
import org.cherub.paymentEndpoint.repositories.FeePaymentRepository;
import org.cherub.paymentEndpoint.repositories.StudentAccountRepository;
import org.cherub.paymentEndpoint.services.FeePaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTests {


    @Mock
    FeePaymentRepository feePaymentRepository;

    @Mock
    StudentAccountRepository studentAccountRepository;

    @InjectMocks
    FeePaymentService feePaymentService;

    @Test
    void testProcessPaymentWithValidStudent(){

        StudentAccount studentAccount = new StudentAccount();
        studentAccount.setId(1L);
        studentAccount.setStudentNumber("STUD000001");
        studentAccount.setInitialBalance(new BigDecimal("800000"));
        studentAccount.setCurrentBalance(new BigDecimal("800000"));

        when(studentAccountRepository.findByStudentNumber("STUD000001")).thenReturn(Optional.of(studentAccount));
        when(feePaymentRepository.save(any(FeePayment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FeePaymentDto feePaymentDto = new FeePaymentDto("STUD000001",new BigDecimal("100000"));

        FeePayment feePayment = feePaymentService.processPayment(feePaymentDto);
        assertEquals(new BigDecimal("0.03"), feePayment.getIncentiveRate());
        assertEquals(new BigDecimal("3000.00"), feePayment.getIncentiveAmount());
        assertEquals(new BigDecimal("697000.00"), feePayment.getNewBalance());
        assertEquals(new BigDecimal("800000"), feePayment.getPreviousBalance());
        assertNotEquals("None", feePayment.getNextPaymentDueDate());
    }

    @Test
    void nextDueDateShouldBeNoneWhenBalanceIsZero() {
        StudentAccount studentAccount = new StudentAccount();
        studentAccount.setId(1L);
        studentAccount.setStudentNumber("STUD000001");
        studentAccount.setInitialBalance(new BigDecimal("800000"));

        when(studentAccountRepository.findByStudentNumber("STUD000001")).thenReturn(Optional.of(studentAccount));
        when(feePaymentRepository.save(any(FeePayment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FeePaymentDto paymentDto = new FeePaymentDto("STUD000001", new BigDecimal("800000"));
        FeePayment payment = feePaymentService.processPayment(paymentDto);

        assertEquals("None", payment.getNextPaymentDueDate());
    }


    @Test
    void nextPaymentDueDateIsNotAWeekend() {
        StudentAccount studentAccount = new StudentAccount();
        studentAccount.setId(1L);
        studentAccount.setStudentNumber("STUD000001");
        studentAccount.setInitialBalance(new BigDecimal("800000"));
        studentAccount.setCurrentBalance(new BigDecimal("800000"));

        when(studentAccountRepository.findByStudentNumber("STUD000001")).thenReturn(Optional.of(studentAccount));
        when(feePaymentRepository.save(any(FeePayment.class))).thenAnswer(invocation -> invocation.getArgument(0));


        FeePaymentDto paymentDto = new FeePaymentDto("STUD000001", new BigDecimal("100000"));
        FeePayment payment = feePaymentService.processPayment(paymentDto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        LocalDate newNextDueDate = LocalDate.parse(payment.getNextPaymentDueDate(), formatter);

        assertNotEquals(DayOfWeek.SATURDAY,newNextDueDate.getDayOfWeek());
        assertNotEquals(DayOfWeek.SUNDAY,newNextDueDate.getDayOfWeek());

    }
}
