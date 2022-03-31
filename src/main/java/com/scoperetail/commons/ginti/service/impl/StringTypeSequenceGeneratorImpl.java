package com.scoperetail.commons.ginti.service.impl;

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
import com.scoperetail.commons.ginti.persistence.SequenceDao;
import com.scoperetail.commons.ginti.service.EpochDay;
import com.scoperetail.commons.ginti.service.GintiGenerator;
import com.scoperetail.commons.ginti.test.Occurrence;
import com.scoperetail.commons.ginti.util.CommonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.scoperetail.commons.ginti.dto.SequenceRequest;
import com.scoperetail.commons.ginti.exception.ValidationFailedException;

@Service
public class StringTypeSequenceGeneratorImpl implements GintiGenerator<String> {

	private static final String SEQUENCE_NAME = ":sequ_name";
	private static final String SEQUENCE_COUNT = ":count";
	private final SequenceDao dao;
	private final EpochDay epochDay;
	private final SequenceFormatter<List<String>> formatter;

	@Value(value = "${scoperetail.ginti.sql}")
	private String sql;

	public StringTypeSequenceGeneratorImpl(final SequenceDao dao, final EpochDay epochDay,
			final SequenceFormatter<List<String>> formatter) {
		this.dao = dao;
		this.epochDay = epochDay;
		this.formatter = formatter;
	}

	@Override
	public List<String> next(SequenceRequest seqRequest, int count) throws ValidationFailedException {

		String seqFormat = seqRequest.getSequenceFormat();
		CommonUtil.checkForValidToken(seqFormat);
		Map<Character, Set<Occurrence>> tokenOccurenceMap= CommonUtil.checkForValidformat(seqFormat);
		sql = sql.replace(SEQUENCE_NAME, seqRequest.getSequenceObject()).replace(SEQUENCE_COUNT, String.valueOf(count));
		Map<String, Object> sqlResponse= dao.next(sql);
		final Integer daysSinceEpoch = epochDay.current();

		return formatter.format(seqRequest,tokenOccurenceMap,sqlResponse,daysSinceEpoch);
	}
}