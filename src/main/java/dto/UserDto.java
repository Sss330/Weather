package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "Login can`t be empty")
    @Size( max = 15, message = "Password can`t be longer than 15 chars")
    private String login;

    @NotBlank(message = "Password can`t be empty")
    private String password;
}
