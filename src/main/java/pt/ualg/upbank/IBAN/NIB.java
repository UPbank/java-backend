package pt.ualg.upbank.IBAN;

import java.math.BigInteger;

/**
 * @author Pedro Cavalheiro
 * Class representing a Bank Identification Number that extends {@link BBAN},
 * creates and validates a NIb. 
 */
public class NIB extends BBAN {

	public NIB(String value) {
		if (value.length() != 21)
			throw new IllegalArgumentException("NIB must have exactly 21 digits.");
		char[] chars = value.toCharArray();
		for (char ch : chars)
			if (!(ch >= '0' && ch <= '9'))
				throw new IllegalArgumentException("NIB has non numeric characters.");
		this.value = value;
		if (!this.validate())
			throw new IllegalArgumentException("Invalid NIB");
	}

	@Override
	public boolean validate() {
		BigInteger asNumber = new BigInteger(value);
		return asNumber.mod(IBAN.NINETY_SEVEN).equals(BigInteger.ONE);
	}

	public static String addCheckDigits(String val) {
		if (val.length() != 19)
			throw new IllegalArgumentException("Number should have 19 digits");
		BigInteger toNumber = new BigInteger(val + "00");
		BigInteger mod97 = toNumber.mod(IBAN.NINETY_SEVEN);
		return val + String.format("%02d", IBAN.NINETY_EIGHT.subtract(mod97));
	}

}
