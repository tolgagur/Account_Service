package account.repo;


import account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    User findByEmail(@Email String email);
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByEmailEqualsIgnoreCase(@NotBlank @Email String email);
    Optional<User> findByEmailEqualsIgnoreCase(@NotBlank @Email String email);

    Optional<User> findByEmailIgnoreCase(@NotBlank @Email String email);

    boolean existsByEmailIgnoreCase(String employee);
}
