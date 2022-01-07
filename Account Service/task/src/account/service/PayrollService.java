package account.service;

import account.entity.Payroll;
import account.entity.User;
import account.exception.InvalidPaymentDataException;
import account.repo.PayrollRepository;
import account.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.springframework.http.ResponseEntity.status;

@Service
public class PayrollService {

    private PayrollRepository payrollRepository;
    private UserRepository userRepository;
    private AuthenticationService authenticationService;

    public PayrollService(PayrollRepository payrollRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        this.payrollRepository = payrollRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public Map<String, String> postAllPayroll(List<Payroll> payrollList){


        for (Payroll payroll:payrollList){

            User user = authenticationService.findUserByEmail(payroll.getEmployee());

            if(user.getEmail().isEmpty()) {
                throw new InvalidPaymentDataException("period not valid on query string");
            };

            boolean regexCheck = Pattern.matches("(0[1-9]|10|11|12)-20[0-9]{2}$",payroll.getPeriod());

            if (regexCheck == false)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong date!");

            Optional<Payroll> byPeriod= payrollRepository.findByPeriod(payroll.getPeriod());
            if (payroll.getSalary()<0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Salary must be non negative!");

            Optional<Payroll> payroll1 = payrollRepository.findByEmployeeAndPeriod(payroll.getEmployee(),payroll.getPeriod());
            if (payroll1.isEmpty())
                payrollRepository.save(payroll);
            else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error!");
        }

        return Map.of("status", "Added successfully!");
    }


}
