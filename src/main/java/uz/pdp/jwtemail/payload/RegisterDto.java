package uz.pdp.jwtemail.payload;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {

    @NotNull
    @Size(min = 3,max = 50)
    private String firstname;

    @NotNull
    @Size(min = 3,max = 50)
    private String lastname;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
