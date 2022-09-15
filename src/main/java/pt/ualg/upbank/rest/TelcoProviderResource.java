package pt.ualg.upbank.rest;

import static pt.ualg.upbank.service.JwtUserDetailsService.ROLE_USER;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ualg.upbank.model.TelcoProviderDTO;
import pt.ualg.upbank.service.TelcoProviderService;


@RestController
@RequestMapping(value = "/api/telcoProviders", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('" + ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class TelcoProviderResource {

    private final TelcoProviderService telcoProviderService;

    public TelcoProviderResource(final TelcoProviderService telcoProviderService) {
        this.telcoProviderService = telcoProviderService;
    }

    @GetMapping
    public ResponseEntity<List<TelcoProviderDTO>> getAllTelcoProviders() {
        return ResponseEntity.ok(telcoProviderService.findAll());
    }

}
