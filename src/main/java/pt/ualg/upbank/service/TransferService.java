package pt.ualg.upbank.service;

import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Transfer;
import pt.ualg.upbank.model.SimplePage;
import pt.ualg.upbank.model.TransferDTO;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.TransferRepository;


@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public TransferService(final TransferRepository transferRepository,
            final AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public SimplePage<TransferDTO> findAll(final Pageable pageable) {
        final Page<Transfer> page = transferRepository.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(transfer -> mapToDTO(transfer, new TransferDTO()))
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable);
    }

    public TransferDTO get(final Long id) {
        return transferRepository.findById(id)
                .map(transfer -> mapToDTO(transfer, new TransferDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final TransferDTO transferDTO) {
        final Transfer transfer = new Transfer();
        mapToEntity(transferDTO, transfer);
        return transferRepository.save(transfer).getId();
    }

    public void update(final Long id, final TransferDTO transferDTO) {
        final Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(transferDTO, transfer);
        transferRepository.save(transfer);
    }

    public void delete(final Long id) {
        transferRepository.deleteById(id);
    }

    private TransferDTO mapToDTO(final Transfer transfer, final TransferDTO transferDTO) {
        transferDTO.setId(transfer.getId());
        transferDTO.setAmount(transfer.getAmount());
        transferDTO.setMetadata(transfer.getMetadata());
        transferDTO.setNotes(transfer.getNotes());
        transferDTO.setImage(transfer.getImage());
        transferDTO.setSender(transfer.getSender() == null ? null : transfer.getSender().getId());
        transferDTO.setReceiver(transfer.getReceiver() == null ? null : transfer.getReceiver().getId());
        return transferDTO;
    }

    private Transfer mapToEntity(final TransferDTO transferDTO, final Transfer transfer) {
        transfer.setAmount(transferDTO.getAmount());
        transfer.setMetadata(transferDTO.getMetadata());
        transfer.setNotes(transferDTO.getNotes());
        transfer.setImage(transferDTO.getImage());
        final Account sender = transferDTO.getSender() == null ? null : accountRepository.findById(transferDTO.getSender())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sender not found"));
        transfer.setSender(sender);
        final Account receiver = transferDTO.getReceiver() == null ? null : accountRepository.findById(transferDTO.getReceiver())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "receiver not found"));
        transfer.setReceiver(receiver);
        return transfer;
    }

}