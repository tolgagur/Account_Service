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
        Payroll payroll1 = payrollRepository.
                findByEmployeeAndPeriod(payroll.getEmployee(),payroll.getPeriod());

        Long id = payroll1.getId();

        if (id == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found!");

        Payroll payroll2 =payrollRepository.findById(payroll1.getId());
        payroll2.setSalary(payroll.getSalary());
        payrollRepository.save(payroll2);
        return Map.of(
                "status","Updated successfully!"
        );


    }
}
