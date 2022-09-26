package pt.ualg.upbank.rest;

import static pt.ualg.upbank.service.JwtUserDetailsService.ROLE_USER;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pt.ualg.upbank.model.CardDTO;
import pt.ualg.upbank.service.CardService;


@RestController
@RequestMapping(value = "/api/cards", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('" + ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class CardResource {

    private final CardService cardService;
    private final AccountResource accountResource;

    public CardResource(final CardService cardService, final AccountResource accountResource) {
        this.cardService = cardService;
        this.accountResource = accountResource;
    }

    //TODO: delete mapping
    @GetMapping("/all")
    public ResponseEntity<List<CardDTO>> getAllCards() {
        return ResponseEntity.ok(cardService.findAll());
    }

    @GetMapping("/")
    public ResponseEntity<List<CardDTO>> getCard() {
        return ResponseEntity.ok(cardService.get(accountResource.getRequestUser().getId()));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCard(@RequestBody @Valid final CardDTO cardDTO) {
        if(cardDTO.getPinCode()!=null && cardDTO.getPinCode().toString().length()!=4){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "card.update.invalidPin");
        }
        return new ResponseEntity<>(cardService.create(cardDTO), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCard(@PathVariable final Long id,
            @RequestBody @Valid final CardDTO cardDTO) {
        cardService.update(id ,cardDTO.getNfcPayments(), cardDTO.getOnlinePayments(), cardDTO.getPinCode());
        return ResponseEntity.ok().build();
    }

    
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCard(@PathVariable final Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
