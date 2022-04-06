package com.scoperetail.commons.ginti.util;

/*-
 * *****
 * ginti-core
 * -----
 * Copyright (C) 2018 - 2022 Scope Retail Systems Inc.
 * -----
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =====
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.scoperetail.commons.ginti.exception.ConfigurationException;
import com.scoperetail.commons.ginti.model.Occurrence;
import com.scoperetail.commons.ginti.model.Request;
import com.scoperetail.commons.ginti.service.EpochDay;

@Component
public class ValidateTokens {

	@Autowired
	private EpochDay epochDay;

	public Map<Character, Set<Occurrence>> validate(Request seqRequest) {

		checkForValidToken(seqRequest.getSequenceFormat());
		return initializeTokenMap(seqRequest);
	}

	public void checkForValidToken(String seqFormat) {

		if (!Pattern.matches(Constants.VALID_CHARACTERS_IN_FORMAT, seqFormat))
			throw new ConfigurationException("Sequence Format " + seqFormat + " not valid");
	}

	private Map<Character, Set<Occurrence>> initializeTokenMap(Request seqReq) {

		String seqFormat = seqReq.getSequenceFormat();
		String alias = seqReq.getAlias();
		Map<Character, Set<Occurrence>> tokenOccurenceMap = new HashMap<Character, Set<Occurrence>>();
		int start = 0, end = 0;
		for (int i = 0; i < seqFormat.length(); i++) {
			char currentToken = seqFormat.charAt(i);
			if (!(i < (seqFormat.length() - 1) && currentToken == seqFormat.charAt(i + 1))) {
				end = i;
				Set<Occurrence> occurenceSet = new HashSet<Occurrence>();
				occurenceSet.add(createOccurenceObject(currentToken, start, end, alias));

				if (tokenOccurenceMap.get(currentToken) == null)
					tokenOccurenceMap.put(currentToken, occurenceSet);
				else {
					Set<Occurrence> setObject = tokenOccurenceMap.get(currentToken);
					setObject.add(createOccurenceObject(currentToken, start, end, alias));
				}
				start = i + 1;
			}
		}
		return tokenOccurenceMap;
	}

	private Occurrence createOccurenceObject(char currentToken, int start, int end, String alias) {
		Occurrence occ = new Occurrence(start, end);

		switch (currentToken) {

		case 'D':
			String epochDays = String.valueOf(epochDay.current());
			validateTokenLength(start, end, epochDays.length(), currentToken);
			occ.setStrToReplace(epochDays);
			break;

		case 'T':
			validateTokenLength(start, end, alias.length(), currentToken);
			occ.setStrToReplace(alias);
			break;
		default:
			break;
		}
		return occ;
	}

	private void validateTokenLength(int start, int end, int lengthToValidate, Character currentToken) {

		if ((end - start + 1) < lengthToValidate)
			throw new ConfigurationException("Invalid configuration of the token '" + currentToken
					+ "'. Should be minimum of length " + lengthToValidate);
	}
}
