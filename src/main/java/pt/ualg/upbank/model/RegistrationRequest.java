package pt.ualg.upbank.model;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationRequest {

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    private String fullName;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    @Size(max = 255)
    private String taxNumber;

    @NotNull
    @Size(max = 255)
    private String idNumber;

		@NotNull
		private AddressDTO address;

}
