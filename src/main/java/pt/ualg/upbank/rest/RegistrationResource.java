package pt.ualg.upbank.rest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pt.ualg.upbank.model.RegistrationRequest;
import pt.ualg.upbank.service.RegistrationService;


@RestController
public class RegistrationResource {

    private final RegistrationService registrationService;
    private static final Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");

    public RegistrationResource(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid final RegistrationRequest registrationRequest) {
        final Matcher mat = pattern.matcher(registrationRequest.getEmail());
        if (!mat.matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registration.email.invalid");
        }
        if (registrationService.emailExists(registrationRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registration.register.taken");
        }
        registrationService.register(registrationRequest);
        return ResponseEntity.ok().build();
    }

}
