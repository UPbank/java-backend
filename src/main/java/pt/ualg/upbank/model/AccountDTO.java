package pt.ualg.upbank.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String hash;

    @NotBlank
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
    private Long balance;

    @NotNull
    private AddressDTO address;

}
