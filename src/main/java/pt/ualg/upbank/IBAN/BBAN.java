package pt.ualg.upbank.IBAN;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pedro Cavalheiro
 *         Abstract Class representing a general Basic Bank Account Number
 * 
 *         Up to 30 alphanumeric characters
 */

public abstract class BBAN {
	protected String value;

	protected BBAN() {
	}

	protected BBAN(String value) {
		if (value.length() > 30)
			throw new IllegalArgumentException("BBAN has more than 30 characters");
		char[] chars = value.toCharArray();
		for (char ch : chars)
			if (!((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z')))
				throw new IllegalArgumentException("BBAN has non alphanumeric characters.");
		this.value = value;
	}

	public abstract boolean validate();

	public BigInteger toNumber() {
		List<String> result = new ArrayList<>();
		for (char ch : value.toCharArray()) {
			if (ch >= '0' && ch <= '9')
				result.add(Integer.toString(ch - '0'));
			else
				result.add(Integer.toString(ch - 'A'));
		}
		return new BigInteger(String.join("", result));
	}

	@Override
	public String toString() {
		return value;
	}
}
