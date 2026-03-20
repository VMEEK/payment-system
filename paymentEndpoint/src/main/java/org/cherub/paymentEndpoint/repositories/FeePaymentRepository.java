package org.cherub.paymentEndpoint.repositories;

import org.cherub.paymentEndpoint.models.FeePayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeePaymentRepository extends JpaRepository<FeePayment,Long> {
   List<FeePayment> findByStudentNumber(String studentNumber);
}
