package org.cherub.paymentEndpoint.repositories;

import org.cherub.paymentEndpoint.models.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentAccountRepository extends JpaRepository<StudentAccount, String> {
    Optional<StudentAccount> findByStudentNumber(String studentNumber);

}
