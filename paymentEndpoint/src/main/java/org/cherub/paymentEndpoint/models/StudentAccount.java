package org.cherub.paymentEndpoint.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String studentNumber;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private String nextDueDate;


    public StudentAccount(String studentNumber, BigDecimal initialBalance,
                          BigDecimal currentBalance, String nextDueDate) {
        this.studentNumber = studentNumber;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
        this.nextDueDate = nextDueDate;
    }
}
