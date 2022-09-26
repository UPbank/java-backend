package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TelcoTransferDTO {
    @NotNull
    private Long amount;

    @Size(max = 255)
    @NotNull
    private String name;

    @Size(max = 511)
    private String note
    ;

    @NotNull
    private Long phoneNumber;

    
}
