package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateDirectDebitDTO {
    @NotNull
    private Boolean active;
    
}
