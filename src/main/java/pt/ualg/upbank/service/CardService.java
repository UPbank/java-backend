package pt.ualg.upbank.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Card;
import pt.ualg.upbank.model.CardDTO;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.CardRepository;


@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardService(final CardRepository cardRepository,
            final AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    public List<CardDTO> findAll() {
        return cardRepository.findAll(Sort.by("id"))
                .stream()
                .map(card -> mapToDTO(card, new CardDTO()))
                .collect(Collectors.toList());
    }

    public CardDTO get(final Long id) {
        return cardRepository.findById(id)
                .map(card -> mapToDTO(card, new CardDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final CardDTO cardDTO) {
        final Card card = new Card();
        mapToEntity(cardDTO, card);
        return cardRepository.save(card).getId();
    }

    public void update(final Long id, final CardDTO cardDTO) {
        final Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(cardDTO, card);
        cardRepository.save(card);
    }

    public void delete(final Long id) {
        cardRepository.deleteById(id);
    }

    private CardDTO mapToDTO(final Card card, final CardDTO cardDTO) {
        cardDTO.setId(card.getId());
        cardDTO.setName(card.getName());
        cardDTO.setExpirationDate(card.getExpirationDate());
        cardDTO.setPinCode(card.getPinCode());
        cardDTO.setAccount(card.getAccount() == null ? null : card.getAccount().getId());
        return cardDTO;
    }

    private Card mapToEntity(final CardDTO cardDTO, final Card card) {
        card.setName(cardDTO.getName());
        card.setExpirationDate(cardDTO.getExpirationDate());
        card.setPinCode(cardDTO.getPinCode());
        final Account account = cardDTO.getAccount() == null ? null : accountRepository.findById(cardDTO.getAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "account not found"));
        card.setAccount(account);
        return card;
    }

}
