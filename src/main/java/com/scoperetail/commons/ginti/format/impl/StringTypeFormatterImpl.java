package com.scoperetail.commons.ginti.format.impl;

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

import com.scoperetail.commons.ginti.format.SequenceFormatter;
import com.scoperetail.commons.ginti.model.Occurrence;
import com.scoperetail.commons.ginti.model.SequenceRequest;
import com.scoperetail.commons.ginti.util.Constants;
import org.springframework.stereotype.Service;
import static org.apache.commons.lang3.StringUtils.leftPad;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StringTypeFormatterImpl implements SequenceFormatter<List<String>> {

	@Override
	public List<String> format(SequenceRequest sequenceRequest, Map<Character, Set<Occurrence>> tokenOccurenceMap,
			Map<String, Object> sqlResponse, Integer daysSinceEpoch) {

		StringBuffer intermediateFormat = new StringBuffer(sequenceRequest.getSequenceFormat());
		tokenOccurenceMap.entrySet().forEach(e -> {
			Set<Occurrence> eachCharOccure = e.getValue();
			eachCharOccure.stream().forEach(eachOccu -> {
				replaceFormat(e.getKey(), eachOccu, daysSinceEpoch, intermediateFormat, sequenceRequest.getAlias());
			});
		});
		return replaceSequence(tokenOccurenceMap, sqlResponse, intermediateFormat);
	}

	private void replaceFormat(Character key, Occurrence eachOccu, Integer daysSinceEpoch,
			StringBuffer intermediateFormat, String alias) {

		int length = (eachOccu.getEnd() - eachOccu.getStart() + 1);
		switch (key) {
		case 'D':
			replaceString(intermediateFormat, leftPad(String.valueOf(daysSinceEpoch), length, "0"), eachOccu.getStart(),
					eachOccu.getEnd());
			break;

		case 'T':
			replaceString(intermediateFormat, leftPad(alias, length, "0"), eachOccu.getStart(), eachOccu.getEnd());
			break;
		default:
			break;
		}
	}

	private List<String> replaceSequence(Map<Character, Set<Occurrence>> tokenOccurenceMap,
			Map<String, Object> sqlResponse, StringBuffer intermediateFormat) {

		List<String> outputSequencesList = new ArrayList<String>();
		long seqRangeStart = (long) sqlResponse.get(Constants.SEQUENCE_START);
		long seqRangeEnd = (long) sqlResponse.get(Constants.SEQUENCE_END);

		tokenOccurenceMap.entrySet().forEach(e -> {
			if (e.getKey() == 'N') {
				Set<Occurrence> eachCharOccure = e.getValue();
				eachCharOccure.stream().forEach(eachOccu -> {
					int length = (eachOccu.getEnd() - eachOccu.getStart() + 1);

					for (long i = seqRangeStart; i <= seqRangeEnd; i++) {
						StringBuffer outputSequences = new StringBuffer(intermediateFormat);
						replaceString(outputSequences, leftPad(String.valueOf(i), length, "0"), eachOccu.getStart(),
								eachOccu.getEnd());
						outputSequencesList.add(outputSequences.toString());
					}
				});
			}

		});
		return outputSequencesList;
	}

	private void replaceString(StringBuffer intermediateFormat, String replaceString, int start, int end) {
		intermediateFormat.replace(start, end + 1, replaceString);
	}
}
