package pt.ualg.upbank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.bytebuddy.asm.Advice.Local;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Address;
import pt.ualg.upbank.domain.TelcoProvider;
import pt.ualg.upbank.model.AccountDTO;
import pt.ualg.upbank.model.AddressDTO;
import pt.ualg.upbank.model.TelcoProviderDTO;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.AddressRepository;
import pt.ualg.upbank.repos.TelcoProviderRepository;
import pt.ualg.upbank.service.AccountService;
import pt.ualg.upbank.service.AddressService;
import pt.ualg.upbank.service.TelcoProviderService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;



@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(TelcoProviderRepository telcoProviderRepository, AccountRepository accountRepository, AddressRepository addressRepository, TelcoProviderService telcoProviderService, AccountService accountService, AddressService addressService) {
    TelcoProvider altice = new TelcoProvider();
    altice.setName("Altice");
    altice.setDateCreated(OffsetDateTime.now());
    altice.setLastUpdated(OffsetDateTime.now());

    TelcoProvider nos = new TelcoProvider();
    nos.setName("NOS");
    nos.setDateCreated(OffsetDateTime.now());
    nos.setLastUpdated(OffsetDateTime.now());

    TelcoProvider moche = new TelcoProvider();
    moche.setName("Moche");
    moche.setDateCreated(OffsetDateTime.now());
    moche.setLastUpdated(OffsetDateTime.now());

    TelcoProvider sapo = new TelcoProvider();
    sapo.setName("Sapo");
    sapo.setDateCreated(OffsetDateTime.now());
    sapo.setLastUpdated(OffsetDateTime.now());

    TelcoProvider uzo = new TelcoProvider();
    uzo.setName("UZO");
    uzo.setDateCreated(OffsetDateTime.now());
    uzo.setLastUpdated(OffsetDateTime.now());

    TelcoProvider viaCard = new TelcoProvider();
    viaCard.setName("Via Card");
    viaCard.setDateCreated(OffsetDateTime.now());
    viaCard.setLastUpdated(OffsetDateTime.now());

    TelcoProvider vodafone = new TelcoProvider();
    vodafone.setName("Vodafone");
    vodafone.setDateCreated(OffsetDateTime.now());
    vodafone.setLastUpdated(OffsetDateTime.now());

    TelcoProvider wtf = new TelcoProvider();
    wtf.setName("WTF");
    wtf.setDateCreated(OffsetDateTime.now());
    wtf.setLastUpdated(OffsetDateTime.now());
    

      Address address = new Address();
      address.setCity("city");
      address.setDistrict("district");
      address.setLine1("line1");
      address.setLine2("line2");
      address.setZipCode("zipCode");
      


      Account government =new Account();
      government.setAddress(address);
      government.setFullName("Governo de Portugal");
      government.setBalance((long)0);//TODO: create env variable
      government.setBirthdate(LocalDate.parse("1143-10-05"));
      government.setTaxNumber("9999999");
      government.setIdNumber("0000000001");
      government.setEmail("gov@gov.pt");
      government.setHash("1234");

      Account entity =new Account();
      entity.setAddress(address);
      entity.setFullName("Pedro Charlito");
      entity.setBalance((long)0);
      entity.setBirthdate(LocalDate.parse("1994-02-03"));
      entity.setTaxNumber("020202020");
      entity.setIdNumber("0000000002");
      entity.setEmail("pdrchrld@gmail.com");
      entity.setHash("2345");

      Account telecomunication =new Account();
      telecomunication.setAddress(address);
      telecomunication.setFullName("Telecomunicações");
      telecomunication.setBalance((long)0);
      telecomunication.setBirthdate(LocalDate.parse("1990-01-01"));
      telecomunication.setTaxNumber("020202020");
      telecomunication.setIdNumber("0000000002");
      telecomunication.setEmail("telco@telco.pt");
      telecomunication.setHash("2345");

       Account bankAccount =new Account();
      bankAccount.setAddress(address);
      bankAccount.setFullName("Bank Account");
      bankAccount.setBalance((long)0);
      bankAccount.setBirthdate(LocalDate.parse("1990-01-01"));
      bankAccount.setTaxNumber("020202020");
      bankAccount.setIdNumber("0000000002");
      bankAccount.setEmail("bank@account.pt");
      bankAccount.setHash("2345");


    return args -> {
      if( !telcoProviderRepository.existsByName("Altice")){

        log.info("Preloading " + telcoProviderRepository.save(altice));
      }
      if( !telcoProviderRepository.existsByName("Nos")){
        log.info("Preloading " + telcoProviderRepository.save(nos));

      }
      if( !telcoProviderRepository.existsByName("Moche")){

        log.info("Preloading " + telcoProviderRepository.save(moche));
      }
      if( !telcoProviderRepository.existsByName("Sapo")){

        log.info("Preloading " + telcoProviderRepository.save(sapo));
      }
      if( !telcoProviderRepository.existsByName("UZO")){

        log.info("Preloading " + telcoProviderRepository.save(uzo));
      }
      if( !telcoProviderRepository.existsByName("Via Card")){

        log.info("Preloading " + telcoProviderRepository.save(viaCard));
      }
      if( !telcoProviderRepository.existsByName("Vodafone")){

        log.info("Preloading " + telcoProviderRepository.save(vodafone));
      }
      if( !telcoProviderRepository.existsByName("WTF")){

        log.info("Preloading " + telcoProviderRepository.save(wtf));
      }

      if(!addressRepository.existsByIdentifier("General")){
        address.setIdentifier("General");

      log.info("Preloading " + addressRepository.save(address));
      }

      if(!accountRepository.existsByIdentifier("Bank Account")){
        government.setIdentifier("Bank Account");


        log.info("Preloading " + accountRepository.save(government));
      }

      if(!accountRepository.existsByIdentifier("Services")){
        entity.setIdentifier("Services");

        log.info("Preloading " + accountRepository.save(entity));
        
      }
      if(!accountRepository.existsByIdentifier("Telecomunications")){
        telecomunication.setIdentifier("Telecomunications");

     
        log.info("Preloading " + accountRepository.save(telecomunication));
      }

      
    

      
  };
}
}

