package account.service;


import account.exception.UserException;
import account.model.Payment;
import account.model.PaymentResponse;
import account.model.User;
import account.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserService userService;

    public List<PaymentResponse> getAllPayments(String email) {
        List<Payment> payments = paymentRepository.findAllByEmployeeOrderByPeriodDesc(email.toLowerCase());
        List<PaymentResponse> response = new ArrayList<>();
        User user = userService.findByEmail(email);
        for (Payment payment : payments) {
            response.add(new PaymentResponse(
                    user.getName(),
                    user.getLastname(),
                    payment.getPeriod(),
                    payment.getSalary()));
        }
        return response;
    }

    public PaymentResponse getPaymentByPeriod(String email, String period) {
        User user = userService.findByEmail(email);
        Payment payment = paymentRepository.findPaymentByEmployeeAndPeriod(email.toLowerCase(), period);
        if (payment == null) {
            throw new UserException("We don`t have info about this period");
        }
        return new PaymentResponse(
                user.getName(),
                user.getLastname(),
                payment.getPeriod(),
                payment.getSalary());
    }

    public void saveAll(List<Payment> payments) {
        for (Payment payment : payments) {
            if (paymentRepository.existsByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                save(payment);
            }
        }
    }

    public void save(Payment payment) {
        if (!checkPeriod(payment.getPeriod())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        payment.setUser(userService.findByEmail(payment.getEmployee()));
        paymentRepository.save(payment);
    }


    public void updatePayment(Payment payment) {
        paymentRepository.delete(paymentRepository.findPaymentByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod()));
        save(payment);
    }

    public boolean checkPeriod(String period) {
        try{
            YearMonth.parse(period, DateTimeFormatter.ofPattern("MM-yyyy"));
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}