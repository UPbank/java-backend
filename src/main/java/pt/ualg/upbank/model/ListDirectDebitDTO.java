package pt.ualg.upbank.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.ReadOnlyProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListDirectDebitDTO {

	@ReadOnlyProperty
	private Long id;

	@NotNull
	private Boolean active;

	private LocalDate lastDebit;

	@NotNull
	private OtherAccountDTO receiver;

	@NotNull
	private Long sender;

	@NotNull
	private OffsetDateTime dateCreated;

}
