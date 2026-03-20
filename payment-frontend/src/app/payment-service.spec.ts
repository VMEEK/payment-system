import { TestBed } from '@angular/core/testing';

import { PaymentService } from './payment-service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('PaymentService', () => {
  let service: PaymentService;
  let httpMock: HttpTestingController;


  beforeEach(() => {
    TestBed.configureTestingModule({
     
      providers: [PaymentService,provideHttpClientTesting()]
    });
    service = TestBed.inject(PaymentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call backend with correct payload', () => {
    const payload = { studentNumber: 'STUD000004', paymentAmount: 100000 };

    service.makePayment(payload).subscribe(response => {
      expect(response.newBalance).toEqual(697000);

      const testRequest = httpMock.expectOne("http://localhost:8080/one-time-payments");
      expect(testRequest.request.method).toBe('POST');
      expect(testRequest.request.body).toEqual(payload);

      testRequest.flush({newBalance:697000});
    });

  })
});





