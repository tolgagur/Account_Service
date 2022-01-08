package account.repo;

import account.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Long> {

    Optional<Payroll> findByPeriod(String period);

    Payroll findByEmployeeAndPeriod(String email,String period);

    List<Payroll> findAll();


}
