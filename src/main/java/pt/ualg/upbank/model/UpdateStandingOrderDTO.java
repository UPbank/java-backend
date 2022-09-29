package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateStandingOrderDTO  { 


    @NotNull
    private Long amount;

    @NotNull
    private Frequency frequency;

    @NotNull
    private String receiverIban;

}
