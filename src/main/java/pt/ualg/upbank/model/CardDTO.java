package pt.ualg.upbank.model;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CardDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private Integer pinCode;

    @NotNull
    private Long account;

}
