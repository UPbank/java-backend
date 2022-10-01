package pt.ualg.upbank.model;

import java.time.OffsetDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDTO {

	private Long id;

	@NotNull
	private Long amount;

	@Size(max = 255)
	private String metadata;

	@Size(max = 255)
	private String notes;

	@Size(max = 511)
	private String image;

	@NotNull
	private Long sender;

	@NotNull
	private Long receiver;
	@NotNull
	private OffsetDateTime created;

}
