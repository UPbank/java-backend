package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StandingOrderDTO {

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
