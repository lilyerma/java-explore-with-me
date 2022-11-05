package explorewithme.ewm.users.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewUserRequest {

    @NotNull
    @NotBlank
    @Email
    private String email;
    String name;
}
