package account.repo;

import account.entity.Payroll;
import account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Integer> {

    Payroll findById(Long id);
//    Optional<Payroll> findAllEmail(@Email String email);
    Optional<Payroll> findByPeriod(String period);

    Payroll findByEmployeeAndPeriod(String email,String period);

}
