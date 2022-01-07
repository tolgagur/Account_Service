package account.remote.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ChangePasswordResponse {

    @Email
    private final String email;

    @NotBlank
    private final String status;
}