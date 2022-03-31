package com.scoperetail.commons.ginti.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.scoperetail.commons.ginti.exception.ValidationFailedException;
import com.scoperetail.commons.ginti.test.Occurrence;

public class CommonUtil {

	private static Map<Character, Set<Occurrence>> tokenOccurenceMap = new HashMap<Character, Set<Occurrence>>();

	public static void checkForValidToken(String seqFormat) throws ValidationFailedException {
		Set<Character> tokenSet = Tokens.tokenSet;
		seqFormat.chars().mapToObj(i -> Character.valueOf((char) i)).forEach(e -> {
			if (!tokenSet.contains(e))
				throw new ValidationFailedException("Unknown Character " + e + " in sequence format");
		});
	}

	public static Map<Character, Set<Occurrence>> checkForValidformat(String seqFormat) throws ValidationFailedException {
		initiliazeTokenOccurence(seqFormat);
		tokenOccurenceMap.get('D').forEach(e -> {
			if (!(e.getEnd() - e.getStart() + 1 >= 5))
				throw new ValidationFailedException("The number of epoch Days 'D' should be minimum of 5 digits");
		});
		return tokenOccurenceMap;
	}

	private static void initiliazeTokenOccurence(String seqFormat) {

		int start = 0, end = 0;
		for (int i = 0; i < seqFormat.length(); i++) {
			char currentToken = seqFormat.charAt(i);
			if (!(i < (seqFormat.length() - 1) && currentToken == seqFormat.charAt(i + 1))) {
				end = i;
				Set<Occurrence> occurenceSet = new HashSet<Occurrence>();
				occurenceSet.add(new Occurrence(start, end));

				if (tokenOccurenceMap.get(currentToken) == null)
					tokenOccurenceMap.put(currentToken, occurenceSet);
				else {
					Set<Occurrence> setObject = tokenOccurenceMap.get(currentToken);
					setObject.add(new Occurrence(start, end));
				}
				start = i + 1;
			}
		}
	}
}
