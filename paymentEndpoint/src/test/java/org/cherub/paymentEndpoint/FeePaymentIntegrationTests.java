package org.cherub.paymentEndpoint;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.cherub.paymentEndpoint.dtos.FeePaymentDto;
import org.cherub.paymentEndpoint.models.FeePayment;
import org.cherub.paymentEndpoint.models.StudentAccount;
import org.cherub.paymentEndpoint.repositories.FeePaymentRepository;
import org.cherub.paymentEndpoint.repositories.StudentAccountRepository;
import org.cherub.paymentEndpoint.services.FeePaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(statements = {
        "ALTER TABLE student_accounts ALTER COLUMN id RESTART WITH 1",
        "ALTER TABLE fee_payments ALTER COLUMN id RESTART WITH 1"
})
public class FeePaymentIntegrationTests {

        @Autowired
        private  FeePaymentService feePaymentService;

        @Autowired
        private StudentAccountRepository studentAccountRepository;

        @Autowired
        private FeePaymentRepository feePaymentRepository;

        @Autowired
        EntityManager entityManager;


        @BeforeEach
        void setup() {
            feePaymentRepository.deleteAll();
            studentAccountRepository.deleteAll();
            studentAccountRepository.save(new StudentAccount("STUD000004", new BigDecimal("800000"), new BigDecimal("800000"), "None"));
            studentAccountRepository.save(new StudentAccount("STUD000005", new BigDecimal("800000"), new BigDecimal("800000"), "None"));
            studentAccountRepository.flush();

        }


        @Test
        void processPaymentShouldApplyIncentiveAndUpdateBalance() {

            StudentAccount studentAccount = studentAccountRepository.findByStudentNumber("STUD000004").orElseThrow();
//            studentAccount.setStudentNumber("STUD000004");
//            studentAccount.setInitialBalance(new BigDecimal("800000"));
//            studentAccount.setCurrentBalance(new BigDecimal("800000"));
//            studentAccountRepository.saveAndFlush(studentAccount);

            FeePaymentDto paymentDto = new FeePaymentDto("STUD000004", new BigDecimal("100000"));

            FeePayment payment = feePaymentService.processPayment(paymentDto);
            List<FeePayment> payments = feePaymentRepository.findAll();

            System.out.println("[Payments]:" + payments);


            assertNotNull(payment);
            assertEquals(new BigDecimal("0.03"), payment.getIncentiveRate());
            assertEquals(0, payment.getIncentiveAmount().compareTo(new BigDecimal("3000")));
            assertEquals(0, payment.getPreviousBalance().compareTo(new BigDecimal("800000")));
            assertEquals(0, payment.getNewBalance().compareTo(new BigDecimal("697000")));

            // Verify studentAccount has been updated in DB
            StudentAccount updatedAccount = studentAccountRepository.findByStudentNumber("STUD000004").orElseThrow();
            assertEquals(0, updatedAccount.getCurrentBalance().compareTo(new BigDecimal("697000")));
            assertNotEquals("None", updatedAccount.getNextDueDate());
        }

        @Test
        void processPaymentShouldSetNextDueDateToNoneWhenBalanceIsZero() {

            StudentAccount studentAccount = studentAccountRepository.findByStudentNumber("STUD000005").orElseThrow();
//            studentAccount.setStudentNumber("STUD000005");
//            studentAccount.setInitialBalance(new BigDecimal("800000"));
//            studentAccount.setCurrentBalance(new BigDecimal("800000"));
//            System.out.println("+++++++++++++   " + studentAccount);
//            studentAccountRepository.saveAndFlush(studentAccount);

            FeePaymentDto paymentDto = new FeePaymentDto("STUD000005", new BigDecimal("800000"));
            FeePayment payment = feePaymentService.processPayment(paymentDto);

            assertNotNull(payment);
            assertEquals("None", payment.getNextPaymentDueDate());
            StudentAccount updatedAccount = studentAccountRepository.findByStudentNumber("STUD000005").orElseThrow();
            List<StudentAccount> accounts = studentAccountRepository.findAll();

            System.out.println("[Students]:" + accounts);

            assertEquals("None", updatedAccount.getNextDueDate());
        }
}


