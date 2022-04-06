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
import com.scoperetail.commons.ginti.model.Occurrence;
import com.scoperetail.commons.ginti.model.Request;
import com.scoperetail.commons.ginti.service.EpochDay;
import com.scoperetail.commons.ginti.service.GintiGenerator;
import com.scoperetail.commons.ginti.util.ValidateTokens;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.scoperetail.commons.ginti.exception.ConfigurationException;

@Service
@Slf4j
public class StringTypeSequenceGeneratorImpl implements GintiGenerator<String> {

	private ValidateTokens validate;
	private final SequenceFormatter<List<String>> formatter;
	
	public StringTypeSequenceGeneratorImpl(final EpochDay epochDay,
			final SequenceFormatter<List<String>> formatter, final ValidateTokens validate) {
		this.formatter = formatter;
		this.validate = validate;
	}

	@Override
	public List<String> next(Request seqRequest) throws ConfigurationException {

		log.debug("seqRequest: {}", seqRequest);
		Map<Character, Set<Occurrence>> tokenOccurenceMap = validate.validate(seqRequest);
		return formatter.format(seqRequest,tokenOccurenceMap);
	}
}