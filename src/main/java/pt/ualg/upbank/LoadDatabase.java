package pt.ualg.upbank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.bytebuddy.asm.Advice.Local;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Address;
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



@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(TelcoProviderRepository telcoProviderRepository, AccountRepository accountRepository, AddressRepository addressRepository, TelcoProviderService telcoProviderService, AccountService accountService, AddressService addressService) {
    // TelcoProvider altice = new TelcoProvider();
    // altice.setId((long)1);
    // altice.setName("Altice");
    // altice.setDateCreated(OffsetDateTime.now());
    // altice.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider nos = new TelcoProvider();
    // nos.setId((long)2);
    // nos.setName("NOS");
    // nos.setDateCreated(OffsetDateTime.now());
    // nos.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider moche = new TelcoProvider();
    // moche.setId((long)3);
    // moche.setName("Moche");
    // moche.setDateCreated(OffsetDateTime.now());
    // moche.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider sapo = new TelcoProvider();
    // sapo.setId((long)4);
    // sapo.setName("Sapo");
    // sapo.setDateCreated(OffsetDateTime.now());
    // sapo.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider uzo = new TelcoProvider();
    // uzo.setId((long)5);
    // uzo.setName("UZO");
    // uzo.setDateCreated(OffsetDateTime.now());
    // uzo.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider viaCard = new TelcoProvider();
    // viaCard.setId((long)6);
    // viaCard.setName("Via Card");
    // viaCard.setDateCreated(OffsetDateTime.now());
    // viaCard.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider vodafone = new TelcoProvider();
    // vodafone.setId((long)7);
    // vodafone.setName("Vodafone");
    // vodafone.setDateCreated(OffsetDateTime.now());
    // vodafone.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider wtf = new TelcoProvider();
    // wtf.setId((long)8);
    // wtf.setName("WTF");
    // wtf.setDateCreated(OffsetDateTime.now());
    // wtf.setLastUpdated(OffsetDateTime.now());
    

    //   Address address = new Address();
    //   address.setId((long)1);
    //   address.setCity("city");
    //   address.setDistrict("district");
    //   address.setLine1("line1");
    //   address.setLine2("line2");
    //   address.setZipCode("zipCode");


    //   Account government =new Account();
    //   government.setAddress(address);
    //   government.setFullName("Governo de Portugal");
    //   government.setId((long)9);
    //   government.setBalance((long)0);
    //   government.setBirthdate(LocalDate.parse("1143-10-05"));
    //   government.setTaxNumber("9999999");
    //   government.setIdNumber("0000000001");
    //   government.setEmail("gov@gov.pt");
    //   government.setHash("1234");

    //   Account entity =new Account();
    //   entity.setAddress(address);
    //   entity.setFullName("Pedro Charlito");
    //   entity.setId((long)10);
    //   entity.setBalance((long)0);
    //   entity.setBirthdate(LocalDate.parse("1994-02-03"));
    //   entity.setTaxNumber("020202020");
    //   entity.setIdNumber("0000000002");
    //   entity.setEmail("pdrchrld@gmail.com");
    //   entity.setHash("2345");

    //   Account telecomunicações =new Account();
    //   entity.setAddress(address);
    //   entity.setFullName("Telecomunicações");
    //   entity.setId((long)11);
    //   entity.setBalance((long)0);
    //   entity.setBirthdate(LocalDate.parse("1990/01/01 "));
    //   entity.setTaxNumber("020202020");
    //   entity.setIdNumber("0000000002");
    //   entity.setEmail("telco@telco.pt");
    //   entity.setHash("2345");



    return args -> {
      // log.info("Preloading " + telcoProviderRepository.save(altice));
      // log.info("Preloading " + telcoProviderRepository.save(nos));
      // log.info("Preloading " + telcoProviderRepository.save(moche));
      // log.info("Preloading " + telcoProviderRepository.save(sapo));
      // log.info("Preloading " + telcoProviderRepository.save(uzo));
      // log.info("Preloading " + telcoProviderRepository.save(viaCard));
      // log.info("Preloading " + telcoProviderRepository.save(vodafone));
      // log.info("Preloading " + telcoProviderRepository.save(wtf));

      // log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)1, "Altice")));

      // log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)2, "Nos")));

      // log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)3, "Moche")));

      // log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)4, "Sapo")));

      // log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)5, "Uzo")));
      

      // log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)6, "Via Card")));

      // log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)7, "Vodafone")));

      // log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)8, "WTF")));

      // log.info("Preloading " + addressRepository.save(address));

      // log.info("Preloading " + accountRepository.save(government));
      
      // log.info("Preloading " + accountService.create(new AccountDTO((long)9, "gov@gov.pt", "1234", "Governo de Portugal", LocalDate.parse("1143/10/5") , "9999999", "0000000001", (long)0, new AddressDTO((long)1)))); 


      // log.info("Preloading " + accountRepository.save(entity));

    //   //entidade
      // log.info("Preloading " + accountService.create(new AccountDTO((long)10, "pdrchrld@gmail.com", "2345", "Pedro Charlito", LocalDate.of(1994, 2, 3) , "020202020", "0000000002", (long)0, new AddressDTO())));

    //   //telecomunicações
    //   log.info("Preloading " + accountService.create(new AccountDTO((long)11, "telco@telco.pt", "3456", "Telecomunicações", LocalDate.of(1990, 1, 1) , "030303030", "0000000003", (long)0, new AddressDTO()))); 

    // };

      
  };
}
}
