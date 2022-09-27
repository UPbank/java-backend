package pt.ualg.upbank.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pt.ualg.upbank.IBAN.IBAN;
import pt.ualg.upbank.IBAN.IBANGenerator;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Transfer;
import pt.ualg.upbank.model.AccountDTO;
import pt.ualg.upbank.model.SimplePage;
import pt.ualg.upbank.model.TransferDTO;
import pt.ualg.upbank.model.IbanTransferDTO;

import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.TelcoProviderRepository;
import pt.ualg.upbank.repos.TransferRepository;


@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final TelcoProviderRepository telcoProviderRepository;

    private final AccountService accountService;

    public TransferService(final TransferRepository transferRepository,
        final AccountRepository accountRepository, final TelcoProviderRepository telcoProviderRepository, final AccountService accountService) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.telcoProviderRepository = telcoProviderRepository;
        this.accountService = accountService;
    }

    public SimplePage<TransferDTO> findAll(final Pageable pageable) {
        final Page<Transfer> page = transferRepository.findAll(pageable);
        return new SimplePage<>(page.getContent()
            .stream()
            .map(transfer -> mapToDTO(transfer, new TransferDTO()))
            .collect(Collectors.toList()),
            page.getTotalElements(), pageable);
    }

    public SimplePage<TransferDTO> getAll(final Long id, final Pageable pageable) {
        final Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"transfers.account.notFound"));
        final Page<Transfer> page =  transferRepository.findBySenderOrReceiver(account, account , pageable);
            return new SimplePage<>(page.getContent()
            .stream()
            .map(transfer -> mapToDTO(transfer, new TransferDTO()))
            .collect(Collectors.toList()),
            page.getTotalElements(), pageable);
    }

    
    public SimplePage<TransferDTO> getByName(final Long id,final String name, final Pageable pageable) {
        final Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"transfers.account.notFound"));
        
        final Page<Transfer> page =  transferRepository.getByNameAndByDate(id, name, pageable);
   
        return new SimplePage<>(page.getContent()
            .stream()
            .map(transfer -> mapToDTO(transfer, new TransferDTO()))
            .collect(Collectors.toList()),
            page.getTotalElements(), pageable);
    }

    public SimplePage<TransferDTO> getByStartDateandEndDate(final Long id,final OffsetDateTime startDate,final OffsetDateTime endDate, final Pageable pageable) {
        final Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"transfers.account.notFound"));
        final Page<Transfer> page =  transferRepository.findBySenderAndDateCreatedGreaterThanAndDateCreatedLessThanOrReceiverAndDateCreatedGreaterThanAndDateCreatedLessThan(account,startDate,endDate, account, startDate,endDate, pageable);
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


    @Transactional
    //Added set time of creation
    public Long create(final TransferDTO transferDTO) {
        final Transfer transfer = new Transfer();
        mapToEntity(transferDTO, transfer);
        
        
        
        //Check Balance is positive 
        if(transfer.getSender().getBalance()<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount.must.be.positive");
        }
        //Check if amount is more than balance
        if(transferDTO.getAmount()>transfer.getSender().getBalance()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not.enough.balance");
        }

        final Account sender = accountRepository.findById(transferDTO.getSender()) //Add number of telecomunicações account
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        accountService.removeMoney(sender, transfer.getAmount());

        final Account reciever = accountRepository.findById(transferDTO.getReceiver()) //Add number of telecomunicações account
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        accountService.addMoney(reciever, transfer.getAmount());

        
        transfer.setDateCreated(OffsetDateTime.now());
        return transferRepository.save(transfer).getId();
    }

    //  //Method for taking money from a bank account
    //  public void removeMoney(Account account,Long amount){
    //     account.setBalance(account.getBalance()-amount);
    // }

    //  //Method for Adding money to a bank account
    //  public void addMoney(final Account account ,Long amount){
    //     account.setBalance(account.getBalance()+amount);
    // }

    @Transactional
    //method to deal wiht Entity and Reference payments
    public Long createFromEntity(final Long entity, final Long reference, final Long amount, final long id) {
        final Account reciever = accountRepository.findByIdentifier("Services")//TODO: change to env variable
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service.account.notFound"));

        final TransferDTO transfer = new TransferDTO();
        transfer.setAmount(amount);
        transfer.setReceiver(reciever.getId());
        transfer.setSender(id);
        transfer.setMetadata("{type:\"SERV\", reference:\"" + reference +", entity:\"" + entity + "\"}");

        return create(transfer);
    }

    @Transactional
    //method to deal wiht reference payments
    public Long createFromGovernment(final Long reference, final Long amount, final long id) {
        final Account reciever = accountRepository.findByIdentifier("Government") //TODO: change to env variabl
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Government.account.notFound"));

        final TransferDTO transfer = new TransferDTO();
        transfer.setAmount(amount);
        transfer.setReceiver(reciever.getId());
        transfer.setSender(id);
        transfer.setMetadata("{type:\"GOV\", reference:\"" + reference + "\"}");

        return create(transfer);
    }
    @Transactional
    //method to deal with phone payments
    public Long createFromPhoneNumber(final String provider, final Long amount, final AccountDTO account, Long phone) {
        final Account reciever = accountRepository.findByIdentifier("TelCos") //TODO: change to env variabl
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"TelCos.account.notFound"));

        final Long sender = account.getId();
        final TransferDTO transfer = new TransferDTO();
        transfer.setAmount(amount);
        transfer.setReceiver(reciever.getId());
        transfer.setSender(sender);
        String json = "{type:\"TEL\", PhoneNumber:\"" + phone + "\", TelCO:\"" + reciever.getFullName() + "\"}"; 
        transfer.setMetadata(json);

        return create(transfer);
    }

    @Transactional
    //method to deal wiht Iban payments
    public Long createFromIban(IbanTransferDTO accountDTO, Long sender) {
        if (!new IBAN(accountDTO.getIban()).validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IBAN.invalid");
        }
        
        final long receiverId = IBANGenerator.ibanToId(accountDTO.getIban());
        final Account receiver = accountRepository.findByIdentifier("Bank Account") //TODO: change to env variabl
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Bank.account.notFound"));
        final TransferDTO transfer = new TransferDTO();
       
        if(!accountRepository.existsById((receiver.getId()))){
            transfer.setReceiver(receiver.getId()); //TODO: change to env variable
        }else{
            transfer.setReceiver(receiverId);
        }
        transfer.setAmount(accountDTO.getAmount());
        transfer.setSender(sender);

        String json = "{type:\"TRAN\", IBAN:\"" + accountDTO.getIban() + "\", amount:\"" + accountDTO.getAmount() + "\"}";
        transfer.setMetadata(json);

        String newNotes = accountDTO.getNote() == null ? null : accountDTO.getNote(); 
        if(newNotes != null)
            transfer.setNotes(newNotes);

        String newImage = accountDTO.getImage() == null ? null : accountDTO.getImage();
        if(newImage != null)
            transfer.setImage(newImage);

        return create(transfer);
    }

    public void update(final Long id, final TransferDTO transferDTO) {
        final Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(transferDTO.getNotes() != null){
            transfer.setNotes(transferDTO.getNotes());
        }
        if(transferDTO.getImage()!=null){
            transfer.setImage(transferDTO.getImage());
        }
        transfer.setLastUpdated(OffsetDateTime.now());
        transferRepository.save(transfer);
    }

    @Transactional
    public void delete(final Long id) {
        transferRepository.deleteById(id);
    }

    private TransferDTO mapToDTO(final Transfer transfer, final TransferDTO transferDTO) {
        transferDTO.setId(transfer.getId());
        transferDTO.setAmount(transfer.getAmount());
        transferDTO.setMetadata(transfer.getMetadata());
        transferDTO.setNotes(transfer.getNotes());
        transferDTO.setImage(transfer.getImage());
        transferDTO.setCreated(transfer.getDateCreated());
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "receiver not found" ));
        
        transfer.setReceiver(receiver);
        return transfer;
    }

    public Boolean checkEntity(Long entity){     
        return entity.toString().length()==5;
    }

    public Boolean checkReference(Long reference){
        return reference.toString().length()==9;
    }

    //already checked in create transferes
    public Boolean checkPositiveAmount(Long amount){
        return amount > 0;
    }

    public Boolean checkGovernamentReference(Long reference){
        return reference.toString().length()==15;
    }

    public Boolean checkTelcoProvider (String name){
        return telcoProviderRepository.existsByName(name);
    }

    public Boolean checkPhoneDigits(Long number){
        return number.toString().length()==9;
    }

    public Boolean checkPhoneNumberStartingDigits(Long number){
        int check = (int)(number/10000000);
        return (check == 91 || check == 92 || check == 93 || check == 96);
    }
    
}
