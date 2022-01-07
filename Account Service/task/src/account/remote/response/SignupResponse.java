package account.remote.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SignupResponse {

    @NotBlank
    private final long id;
    @NotBlank
    private final String name;
    @NotBlank
    private final String lastname;
    @NotBlank
    private final String email;
}