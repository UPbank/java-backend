package pt.ualg.upbank.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TelcoProviderDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    public TelcoProviderDTO() {
    }

    public TelcoProviderDTO (long id, String name){
        this.id = id;
        this.name= name;
    }
}
