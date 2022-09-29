package pt.ualg.upbank.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OtherAccountDTO {

    private Long id;

    @NotBlank
    @NotNull
    @Size(max = 255)
    private String fullName;

}
