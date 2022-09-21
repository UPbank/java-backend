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

    public AccountDTO(){}
    
    public AccountDTO(long id, String email, String hash, String fullName, LocalDate birthdate, String taxNumber, String idNumber, long balance, AddressDTO address){

        this.id = id;
        this.email = email;
        this.hash = hash;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.taxNumber = taxNumber;
        this.idNumber = idNumber;
        this.balance = balance;
        this.address = address;
    }

}
