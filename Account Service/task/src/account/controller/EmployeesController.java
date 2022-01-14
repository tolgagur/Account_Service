package account.controller;

import account.dto.PaymentResponse;
import account.entity.Payroll;
import account.entity.User;
import account.exception.InvalidPaymentDataException;
import account.repo.PayrollRepository;
import account.service.AuthenticationService;
import account.service.PayrollService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static account.service.shared.utils.validation.PeriodValidator.isValidDate;

@RestController
@RequestMapping("/api")
public class EmployeesController {

    private  AuthenticationService service;
    private  PayrollRepository payrollRepository;
    private PayrollService payrollService;

    public EmployeesController(AuthenticationService service, PayrollRepository payrollRepository, PayrollService payrollService) {
        this.service = service;
        this.payrollRepository = payrollRepository;
        this.payrollService = payrollService;
    }
//    @GetMapping("/empl/payment")
//    public ResponseEntity<UserResponse> getPayment(Principal principal, UserResponse userResponse){
//        User user = service.findUserByEmail(principal.getName());
//        userResponse.setId(user.getId());
//        userResponse.setName(user.getName());
//        userResponse.setLastname(user.getLastname());
//        userResponse.setEmail(user.getEmail().toLowerCase(Locale.ROOT));
//        return new ResponseEntity<>(userResponse, HttpStatus.OK);
//    }

//    @GetMapping("/empl/payment/test")
//    public List<Payroll> getPayment(Principal principal){
//        List<Payroll> payrollOptional = payrollRepository.findAllEmployee(principal.getName());
//        return payrollOptional;
//    }




    @GetMapping("/empl/payment")
    public ResponseEntity<?>  getPaymentAndUser(@RequestParam("period") Optional<String> period, Principal principal) {
        User user = service.findUserByEmail(principal.getName());


        if (period.isPresent()) {
            Payroll payroll = payrollRepository.findByEmployeeAndPeriod(principal.getName(), period.get());
            Optional<Payroll> byPeriod = Optional.ofNullable(
                    payrollRepository.findByEmployeeAndPeriod(principal.getName(), period.get()));

            Optional<Payroll> payroll12 = payrollRepository.
                    findByEmployeeAndPeriodIgnoreCase(principal.getName(), period.get());

            if (!isValidDate(period.get()))
                throw new InvalidPaymentDataException("period not valid on query string");

            if(byPeriod.isEmpty()){
               // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"geçersiz tarih girdiniz!");
                Payroll payroll1 = new Payroll();
                payroll1.setUserid(user.getId());
                payroll1.setEmployee(user.getEmail());
                payroll1.setPeriod(period.get());
                payroll1.setSalary(77777L);

                payrollRepository.save(payroll1);
                return ResponseEntity.ok( new PaymentResponse(
                                user.getName(),
                                period.get(),
                                user.getLastname(),
                                payroll1.getSalary()
                        ));

            }


            return ResponseEntity.ok(byPeriod.stream()
                    .map(payroll1 -> new PaymentResponse(
                            user.getName(),
                            payroll.getPeriod(),
                            user.getLastname(),
                            payroll.getSalary()
                    )).findFirst());
        }

            //List<Payroll> payrollOptional = payrollRepository.findAllEmployee(principal.getName());
            List<Payroll> payrollList = payrollRepository.findAll();
            return ResponseEntity.ok(payrollList.stream()
                    .filter(payment -> payment.getUserid().equals(user.getId()))
                    .map(payment -> new PaymentResponse(
                            user.getName(),
                            payment.getPeriod(),
                            user.getLastname(),
                            payment.getSalary()
                    )).sorted(Comparator.comparing(PaymentResponse::getPeriodAsYearMonth).reversed())
                    .collect(Collectors.toList()));



    }

}
