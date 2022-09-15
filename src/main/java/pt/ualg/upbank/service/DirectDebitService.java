package pt.ualg.upbank.service;

import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.DirectDebit;
import pt.ualg.upbank.model.DirectDebitDTO;
import pt.ualg.upbank.model.SimplePage;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.DirectDebitRepository;


@Service
public class DirectDebitService {

    private final DirectDebitRepository directDebitRepository;
    private final AccountRepository accountRepository;

    public DirectDebitService(final DirectDebitRepository directDebitRepository,
            final AccountRepository accountRepository) {
        this.directDebitRepository = directDebitRepository;
        this.accountRepository = accountRepository;
    }

    public SimplePage<DirectDebitDTO> findAll(final Pageable pageable) {
        final Page<DirectDebit> page = directDebitRepository.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(directDebit -> mapToDTO(directDebit, new DirectDebitDTO()))
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable);
    }

    public DirectDebitDTO get(final Long id) {
        return directDebitRepository.findById(id)
                .map(directDebit -> mapToDTO(directDebit, new DirectDebitDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final DirectDebitDTO directDebitDTO) {
        final DirectDebit directDebit = new DirectDebit();
        mapToEntity(directDebitDTO, directDebit);
        return directDebitRepository.save(directDebit).getId();
    }

    public void update(final Long id, final DirectDebitDTO directDebitDTO) {
        final DirectDebit directDebit = directDebitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(directDebitDTO, directDebit);
        directDebitRepository.save(directDebit);
    }

    public void delete(final Long id) {
        directDebitRepository.deleteById(id);
    }

    private DirectDebitDTO mapToDTO(final DirectDebit directDebit,
            final DirectDebitDTO directDebitDTO) {
        directDebitDTO.setId(directDebit.getId());
        directDebitDTO.setActive(directDebit.getActive());
        directDebitDTO.setLastDebit(directDebit.getLastDebit());
        directDebitDTO.setReceiver(directDebit.getReceiver() == null ? null : directDebit.getReceiver().getId());
        directDebitDTO.setSender(directDebit.getSender() == null ? null : directDebit.getSender().getId());
        return directDebitDTO;
    }

    private DirectDebit mapToEntity(final DirectDebitDTO directDebitDTO,
            final DirectDebit directDebit) {
        directDebit.setActive(directDebitDTO.getActive());
        directDebit.setLastDebit(directDebitDTO.getLastDebit());
        final Account receiver = directDebitDTO.getReceiver() == null ? null : accountRepository.findById(directDebitDTO.getReceiver())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "receiver not found"));
        directDebit.setReceiver(receiver);
        final Account sender = directDebitDTO.getSender() == null ? null : accountRepository.findById(directDebitDTO.getSender())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sender not found"));
        directDebit.setSender(sender);
        return directDebit;
    }

}
