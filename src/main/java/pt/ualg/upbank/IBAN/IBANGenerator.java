package pt.ualg.upbank.IBAN;

public class IBANGenerator {
    
    private static final String prefix = "00972890";

    public static String generateIBAN(Long id) {
        BBAN nib = new NIB(NIB.addCheckDigits(prefix + String.format("%011d", id)));
        return IBAN.addCheckDigits("PT", nib);
    }
    public static Long ibanToId(String iban){
        return Long.parseLong(iban.substring(12,  iban.length()-2));
    }
  
}
