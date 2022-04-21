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
import com.scoperetail.commons.ginti.model.Request;
import com.scoperetail.commons.ginti.persistence.SequenceDao;
import com.scoperetail.commons.ginti.util.Constants;
import com.scoperetail.commons.ginti.exception.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static org.apache.commons.lang3.StringUtils.leftPad;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class StringTypeFormatterImpl implements SequenceFormatter<List<String>> {

	@Autowired
	private SequenceDao dao;

	@Value(value = "${scoperetail.ginti.sql}")
	private String sqlQuery;

	@Override
	public List<String> format(Request seqRequest, Map<Character, Set<Occurrence>> tokenOccurenceMap) {

		StringBuilder intermediateFormat = new StringBuilder(seqRequest.getSequenceFormat());
		tokenOccurenceMap.entrySet().forEach(e -> {
			Set<Occurrence> eachCharOccure = e.getValue();
			eachCharOccure.stream().forEach(eachOccu -> {
				replaceFormat(eachOccu, intermediateFormat);
			});
		});
		return replaceSequence(seqRequest, tokenOccurenceMap, intermediateFormat);
	}

	private List<String> replaceSequence(Request seqRequest, Map<Character, Set<Occurrence>> tokenOccurenceMap,
			StringBuilder intermediateFormat) {

		Set<Occurrence> seqOccurenceInFormat = tokenOccurenceMap.get('N');
		// List<String> outputSequencesList = Collections.nCopies(seqRequest.getCount(),
		// intermediateFormat.toString());
		List<String> outputSequencesList = new ArrayList<String>();

		if (!Objects.isNull(seqOccurenceInFormat)) {
			String seqQuery = sqlQuery.replace(Constants.SEQUENCE_NAME, seqRequest.getSequenceName())
					.replace(Constants.SEQUENCE_COUNT, String.valueOf(seqRequest.getCount()));
			Map<String, Object> sqlResponse = dao.next(seqQuery);
			long seqRangeStart = (long) sqlResponse.get(Constants.SEQUENCE_START);
			long seqRangeEnd = (long) sqlResponse.get(Constants.SEQUENCE_END);

			if (seqRequest.getSequenceFormat().length() < String.valueOf(seqRangeEnd).length())
				throw new ConfigurationException("Invalid configuration of the token 'N'. Should be minimum of length "
						+ String.valueOf(seqRangeEnd).length());

			seqOccurenceInFormat.stream().forEach(eachOccu -> {
				for (long i = seqRangeStart; i <= seqRangeEnd; i++) {
					StringBuilder outputSequences = new StringBuilder(intermediateFormat);
					eachOccu.setStrToReplace(String.valueOf(i));
					replaceFormat(eachOccu, outputSequences);
					outputSequencesList.add(outputSequences.toString());
				}
			});
		}
		return outputSequencesList;
	}

	private void replaceFormat(Occurrence eachOccu, StringBuilder intermediateFormat) {

		int length = (eachOccu.getEnd() - eachOccu.getStart() + 1);
		String strToReplace = leftPad(eachOccu.getStrToReplace(), length, "0");
		if (!Objects.isNull(strToReplace))
			intermediateFormat.replace(eachOccu.getStart(), eachOccu.getEnd() + 1, strToReplace);
	}
}
