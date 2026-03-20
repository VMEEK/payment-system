package org.cherub.paymentEndpoint.models;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fee_payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentAccountId;
    private String studentNumber;
    private BigDecimal paymentAmount;
    private BigDecimal incentiveRate;
    private BigDecimal incentiveAmount;
    private String paymentDate;
    private String nextPaymentDueDate;
    private BigDecimal previousBalance;
    private BigDecimal newBalance;
}
