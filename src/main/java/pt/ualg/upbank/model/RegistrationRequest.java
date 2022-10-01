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

	public RegistrationRequest() {

	}

	public RegistrationRequest(String email, String password, String fullName, LocalDate birthdate, String taxNumber,
			String idNumber, AddressDTO address) {
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.birthdate = birthdate;
		this.taxNumber = taxNumber;
		this.idNumber = idNumber;
		this.address = address;

	}

}
