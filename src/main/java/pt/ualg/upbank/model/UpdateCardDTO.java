package pt.ualg.upbank.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateCardDTO  { 


    @Max(value = 9999)
    private Integer pinCode;

    private Boolean nfcPayments;
    
    private Boolean onlinePayments;
}
