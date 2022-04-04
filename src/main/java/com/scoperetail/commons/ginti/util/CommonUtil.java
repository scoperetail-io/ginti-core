package com.scoperetail.commons.ginti.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.scoperetail.commons.ginti.exception.ValidationFailedException;
import com.scoperetail.commons.ginti.model.Occurrence;

@Component
public class CommonUtil {

	public void checkForValidToken(String seqFormat) throws ValidationFailedException {
		Set<Character> tokenSet = Tokens.tokenSet;
		seqFormat.chars().mapToObj(i -> Character.valueOf((char) i)).forEach(e -> {
			if (!tokenSet.contains(e))
				throw new ValidationFailedException("Unknown Character " + e + " in sequence format");
		});
	}

	public Map<Character, Set<Occurrence>> checkForValidformat(String seqFormat) throws ValidationFailedException {
		Map<Character, Set<Occurrence>> tokenOccurenceMap = new HashMap<Character, Set<Occurrence>>();
		initiliazeTokenOccurence(seqFormat,tokenOccurenceMap);
		tokenOccurenceMap.get('D').forEach(e -> {
			if (!(e.getEnd() - e.getStart() + 1 >= 5))
				throw new ValidationFailedException("The number of epoch Days 'D' should be minimum of 5 digits");
		});
		return tokenOccurenceMap;
	}

	private void initiliazeTokenOccurence(String seqFormat, Map<Character, Set<Occurrence>> tokenOccurenceMap) {
		
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
