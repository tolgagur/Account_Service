package account.controller;

import account.entity.Payroll;
import account.repo.PayrollRepository;
import account.service.PayrollService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api")
public class AcctController {

    private PayrollService payrollService;
    private  PayrollRepository payrollRepository;

    public AcctController(PayrollService payrollService,PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
        this.payrollService = payrollService;
    }

    @PostMapping("/acct/payments")
    public Map<String, String> addUserPayments(@RequestBody List<Payroll> payrollList){
        return payrollService.postAllPayroll(payrollList);
    }

    @GetMapping("/acct/payments/all")
    public ResponseEntity<List<Payroll>> getAllUserPayments(){
        return status(HttpStatus.OK).body(payrollRepository.findAll());

    }

    @PutMapping("/acct/payments")
    public Map<String, String> putUserPayment(@RequestBody Payroll payroll){

        boolean regexCheck = Pattern.matches("(0[1-9]|10|11|12)-20[0-9]{2}$",payroll.getPeriod());

        if (regexCheck == false)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong date!");

        Optional<Payroll> payroll1 = payrollRepository.
                findByEmployeeAndPeriodIgnoreCase(payroll.getEmployee(),payroll.getPeriod());


        if (payroll1.isPresent()){
            Payroll payroll2 = payrollRepository.findByEmployeeAndPeriod(payroll.getEmployee(),payroll.getPeriod());
            payroll2.setSalary(payroll.getSalary());
            payrollRepository.save(payroll2);
            return Map.of(
                    "status","Updated successfully!"
            );
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found!");

        }



    }
}
