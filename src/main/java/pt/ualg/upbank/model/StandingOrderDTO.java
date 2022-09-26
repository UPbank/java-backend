package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.ReadOnlyProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StandingOrderDTO extends IbanTransferDTO { 

    @ReadOnlyProperty
    private Long id;

    @NotNull
    private Long amount;

    @NotNull
    private Frequency frequency;

    @NotNull
    private Long sender;

    @NotNull
    private String iban;

}
