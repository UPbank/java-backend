package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class IbanTransferDTO {


    @NotNull
    private Long amount;

    @Size(max = 255)
    private String note;

    @Size(max = 511)
    private String image;

    @NotNull
    private String iban;

}


    

