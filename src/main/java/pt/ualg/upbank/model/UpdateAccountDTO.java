package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountDTO {
	private Long id;

	@Size(max = 255)
	private String email;

	@NotNull
	private AddressDTO address;

}
