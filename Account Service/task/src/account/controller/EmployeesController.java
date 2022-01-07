package account.controller;

import account.dto.PaymentResponse;
import account.entity.Payroll;
import account.entity.User;
import account.repo.PayrollRepository;
import account.service.AuthenticationService;
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

@RestController
@RequestMapping("/api")
public class EmployeesController {

    private final AuthenticationService service;
    private final PayrollRepository payrollRepository;

    public EmployeesController(AuthenticationService service, PayrollRepository payrollRepository) {
        this.service = service;
        this.payrollRepository = payrollRepository;
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




    @GetMapping("/empl/payment")
    public ResponseEntity<?>  getPaymentAndUser(@RequestParam("period") String period, Principal principal) {
        User user = service.findUserByEmail(principal.getName());
        Optional<Payroll> payroll = payrollRepository.findByEmployeeAndPeriod(principal.getName(),period);
        Optional<Payroll> byPeriod = payrollRepository.findByPeriod(period);
        if (payroll.isPresent()) {

            if (payroll.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not date!");
            String date = payroll.get().getPeriod();
            String[] stringsDate = date.split("-");
            List<String> map = new ArrayList<>();
            int dateNumber = Integer.parseInt(stringsDate[0]);
            String[] mount = {"January", "February", "March",
                    "April", "May", "June",
                    "July", "August", "September",
                    "October", "November", "December"};
            int payrollNumber = Math.toIntExact(payroll.get().getSalary());

            int dolar = payrollNumber / 100;
            int cent = payrollNumber % 100;

            Map<String,String> map1 =new HashMap<>();
            map1.put("name", user.getName());
            map1.put("lastname", user.getLastname());
            map1.put("period", mount[dateNumber + 1] + "-" + stringsDate[1]);
            map1.put("salary", dolar + " dollar(s) " + cent + " cent(s)");

            return ResponseEntity.ok(byPeriod.stream()
                    .map(payroll1 -> new PaymentResponse(
                            user.getName(),
                            payroll.get().getPeriod(),
                            user.getLastname(),
                            payroll.get().getSalary()
                    )).findFirst());
        }


        return ResponseEntity.ok(payroll.stream()
                .map(payment -> new PaymentResponse(
                        user.getName(),
                        payroll.get().getPeriod(),
                        user.getLastname(),
                        payroll.get().getSalary()
                )).sorted(Comparator.comparing(PaymentResponse::getPeriodAsYearMonth).reversed())
                .collect(Collectors.toList()));




    }


}
