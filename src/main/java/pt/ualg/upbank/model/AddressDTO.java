package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.ReadOnlyProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

	@ReadOnlyProperty
	private Long id;

	@NotNull
	@Size(max = 255)
	private String line1;

	@Size(max = 255)
	private String line2;

	@NotNull
	@Size(max = 255)
	private String zipCode;

	@NotNull
	@Size(max = 255)
	private String city;

	@NotNull
	@Size(max = 255)
	private String district;

	@Size(max = 255)
	private String identifier;

	public AddressDTO() {
	};

	public AddressDTO(Long id, String line1, String line2, String zipCode, String city, String district) {
		this.id = id;
		this.line1 = line1;
		this.line2 = line2;
		this.zipCode = zipCode;
		this.city = city;
		this.district = district;
	};

}