package pt.ualg.upbank.model;

import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateTransferDTO {

    @Size(max = 255)
    private String notes;

    @Size(max = 511)
    private String image;
    
}
