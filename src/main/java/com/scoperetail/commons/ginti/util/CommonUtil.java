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
