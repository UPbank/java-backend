package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {

	@NotNull
	@Size(max = 255)
	private String email;

	@NotNull
	@Size(max = 255)
	private String password;

}
