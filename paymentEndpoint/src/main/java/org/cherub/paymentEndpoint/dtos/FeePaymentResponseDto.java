package org.cherub.paymentEndpoint.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeePaymentResponseDto {
    private String studentNumber;
    private BigDecimal previousBalance;
    private BigDecimal paymentAmount;
    private BigDecimal incentiveRate;
    private BigDecimal incentiveAmount;
    private String nextPaymentDueDate;
    private BigDecimal newBalance;

}
