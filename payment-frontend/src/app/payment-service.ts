import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FeePaymentRequest } from './interfaces/fee-payment-request';
import { Observable } from 'rxjs';
import { FeePaymentResponse } from './interfaces/fee-payment-response';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {

  private url = "http://localhost:8080/one-time-payment";

  constructor(private http:HttpClient){}

  makePayment(paymentData: FeePaymentRequest): Observable<FeePaymentResponse> {
    return this.http.post<FeePaymentResponse>(this.url,paymentData);
  }


}
