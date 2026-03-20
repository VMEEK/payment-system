import { Component } from '@angular/core';
import { PaymentService } from '../payment-service';
import { FeePaymentRequest } from '../interfaces/fee-payment-request';
import { FormsModule } from '@angular/forms';
import { FeePaymentResponse } from '../interfaces/fee-payment-response';

@Component({
  selector: 'app-payment-component',
  imports: [FormsModule],
  templateUrl: './payment-component.html',
  styleUrl: './payment-component.css',
})
export class PaymentComponent {

  
  paymentData: FeePaymentRequest = {
    studentNumber: '',
    paymentAmount: 0
  }

  paymentResponse:any;
  responseMessage:any;


  constructor(private paymentService:PaymentService){  }

  processPayment(){
    this.paymentService.makePayment(this.paymentData).subscribe({
      next: (val:FeePaymentResponse) => {
        this.paymentResponse = val;
        if(this.paymentResponse){
        console.log('[data]: ',this.paymentResponse);
        this.responseMessage = "Payment Successful!"
        alert(this.responseMessage);
        }
      },
      error: (err) => {
        this.responseMessage = "Payment Failed";
        console.error("Error:", err);
        alert(err.error.message); 
      }
    })
  }
}
