package org.cherub.paymentEndpoint.services;

import org.cherub.paymentEndpoint.dtos.FeePaymentDto;
import org.cherub.paymentEndpoint.dtos.FeePaymentResponseDto;
import org.cherub.paymentEndpoint.models.FeePayment;
import org.cherub.paymentEndpoint.models.StudentAccount;
import org.cherub.paymentEndpoint.repositories.FeePaymentRepository;
import org.cherub.paymentEndpoint.repositories.StudentAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FeePaymentService {

    private final StudentAccountRepository studentAccountRepository;
    private final FeePaymentRepository feePaymentRepository;

    public FeePaymentService(StudentAccountRepository studentAccountRepository, FeePaymentRepository feePaymentRepository){
        this.studentAccountRepository = studentAccountRepository;
        this.feePaymentRepository = feePaymentRepository;
    }

    public FeePayment processPayment(FeePaymentDto paymentDto){
       BigDecimal incentiveRate,incentiveAmount,totalReductionAmount,newBalance,finalBalance;
       StudentAccount studentAccount = studentAccountRepository.findByStudentNumber(paymentDto.getStudentNumber()).orElseThrow(() -> new RuntimeException("Student Not Found!"));
       List<FeePayment> payments = feePaymentRepository.findByStudentNumber(studentAccount.getStudentNumber());
       boolean isFirstPayment = payments.isEmpty();

       incentiveRate = determineIncentiveRate(paymentDto.getPaymentAmount(),isFirstPayment);
       incentiveAmount = paymentDto.getPaymentAmount().multiply(incentiveRate);
       totalReductionAmount = paymentDto.getPaymentAmount().add(incentiveAmount);
       newBalance = isFirstPayment ? studentAccount.getInitialBalance().subtract(totalReductionAmount) : studentAccount.getCurrentBalance().subtract(totalReductionAmount);
       finalBalance = newBalance.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newBalance;

       if(paymentDto.getPaymentAmount().compareTo(studentAccount.getInitialBalance()) > 0) {
        throw new IllegalArgumentException("Payment Amount has exceeded initial balance!");
       }

       if(!isFirstPayment && paymentDto.getPaymentAmount().compareTo(finalBalance)  > 0) {
            throw new IllegalArgumentException("Payment Amount has exceeded previous balance!");
       }

       if (!isFirstPayment && finalBalance.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalStateException("Payments cannot be accepted when student balance is 0");
       }

        // create new payment record
       FeePayment feePayment = new FeePayment();
       feePayment.setStudentAccountId(studentAccount.getId());
       feePayment.setStudentNumber(studentAccount.getStudentNumber());
       feePayment.setPaymentAmount(paymentDto.getPaymentAmount());
       feePayment.setPaymentDate(getSystemDate());
       feePayment.setPreviousBalance(isFirstPayment ? studentAccount.getInitialBalance() : studentAccount.getCurrentBalance());

       feePayment.setIncentiveRate(incentiveRate);
       feePayment.setIncentiveAmount(incentiveAmount);
       feePayment.setNewBalance(finalBalance);

        // update student Account
        String nextDueDate = calculateNextDueDate(feePayment.getPaymentDate());
        studentAccount.setCurrentBalance(finalBalance);

        // nextDueDate is set to None if New balance is zero else nextDueDate
        studentAccount.setNextDueDate(studentAccount.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0 ? "None" : nextDueDate);
        studentAccountRepository.saveAndFlush(studentAccount);
        feePayment.setNextPaymentDueDate(studentAccount.getNextDueDate());

       return feePaymentRepository.save(feePayment);
    }


    private BigDecimal determineIncentiveRate(BigDecimal paymentAmount,boolean isFirstPayment) {
        BigDecimal incentiveRate;
        if (isFirstPayment) {
            if (paymentAmount.compareTo(BigDecimal.ZERO) > 0 && paymentAmount.compareTo(new BigDecimal("100000")) < 0) {
                incentiveRate = new BigDecimal("0.01");
            } else if (paymentAmount.compareTo(new BigDecimal("100000")) >= 0 && paymentAmount.compareTo(new BigDecimal("500000")) < 0) {
                incentiveRate = new BigDecimal("0.03");
            } else if (paymentAmount.compareTo(new BigDecimal("500000")) >= 0) {
                incentiveRate = new BigDecimal("0.05");
            } else {
                incentiveRate = BigDecimal.ZERO;
            }
        }else{
            incentiveRate = BigDecimal.ZERO;
        }
        return incentiveRate;
    }

    private String getSystemDate(){
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        return date.format(formatter);
    }

    private String calculateNextDueDate(String feePaymentDate){
        LocalDate nextDueDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        LocalDate paymentDate = LocalDate.parse(feePaymentDate,formatter);
        String dayOfTheWeek = paymentDate.getDayOfWeek().toString();
        if(dayOfTheWeek.equals("SATURDAY")){
            nextDueDate = paymentDate.plusDays(92);
        }else if(dayOfTheWeek.equals("SUNDAY")){
            nextDueDate = paymentDate.plusDays(91);
        }else{
            nextDueDate = paymentDate.plusDays(90);
        }
        DateTimeFormatter actualFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        return nextDueDate.format(actualFormatter);
    }
}
