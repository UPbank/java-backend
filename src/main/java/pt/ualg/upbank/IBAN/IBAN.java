package pt.ualg.upbank.IBAN;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
	2 letters ISO country code
	2 digits IBAN check digits
	4 digits bank code
	4 digits branch code
	11 digits account number
	2 digits check digit
 */
public class IBAN {

	private String countryCode;
	private int countryCheckDigits;
	private BBAN bban;

	static final BigInteger NINETY_SEVEN = new BigInteger("97");
	static final BigInteger NINETY_EIGHT = new BigInteger("98");

	public IBAN(String val) {
		this.countryCode = val.substring(0, 2);
		this.countryCheckDigits = Integer.parseInt(val.substring(2, 4));
        switch (this.countryCode) {
            case "PT":
                this.bban = new NIB(val.substring(4));
                break;

            default:
                throw new UnsupportedOperationException("Country code not supported");
        }

		if (!this.validate())
			throw new IllegalArgumentException("Invalid IBAN");
	}

	public static BigInteger countryCodeToNumber(String countryCode) {
		List<String> result = new ArrayList<>();
		for (char ch : countryCode.toCharArray()) {
			if (ch >= '0' && ch <= '9')
				result.add(Integer.toString(ch-'0'));
			else
				result.add(Integer.toString(ch-'A'+10));
		}
		return new BigInteger(String.join("", result));
	}

	public BigInteger countryCodeToNumber() {
		return IBAN.countryCodeToNumber(this.countryCode);
	}

	public boolean validate() {
		if (!bban.validate())
			return false;
		BigInteger ibanAsNumber = new BigInteger(bban.toNumber().toString() + this.countryCodeToNumber() + Integer.toString(countryCheckDigits));
		return ibanAsNumber.mod(NINETY_SEVEN).equals(BigInteger.ONE);
	}

	public static String addCheckDigits(String countryCode, BBAN bban) {
		if (!bban.validate())
			throw new IllegalArgumentException("BBAN is invalid");
		BigInteger bbanAsInteger = bban.toNumber();
		BigInteger toNumber = new BigInteger(bbanAsInteger.toString() + IBAN.countryCodeToNumber(countryCode).toString() + "00");
		BigInteger mod97 = toNumber.mod(IBAN.NINETY_SEVEN);
		return countryCode + String.format("%02d", IBAN.NINETY_EIGHT.subtract(mod97)) + bban.toString();
	}



}
