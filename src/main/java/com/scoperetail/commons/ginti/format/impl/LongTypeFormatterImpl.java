package com.scoperetail.commons.ginti.format.impl;

import com.scoperetail.commons.ginti.entity.Tenant;

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
import org.springframework.stereotype.Service;
import static org.apache.commons.lang3.StringUtils.leftPad;

/**
 * Default format is DDDDDTTNNNNNNNNNNN
 * <li>DDDDD - 5 digits represent the current day since the epoch. For example –
 * January 20th, # 2022 is day number 19,011 since January 1st, 1970
 * <li>XX - 2 digit division ID. Can be used to detect the correct shard.
 * <li>NNNNNNNNNNN - 11 digits sequence number that cycles between “00000000001”
 * and “99999999999” (1 short of 100 Billion)
 *
 * <p>
 * You can override this class or implement the interface to change the default
 * format
 */
@Service
public class LongTypeFormatterImpl implements SequenceFormatter<Long> {

	/**
	 * We are not adding any validations to avoid the performance impact. It is the
	 * responsibility of the caller of this API to ensure the format is correct and
	 * sequence object is set up.
	 *
	 * @param tenant
	 * @param sequence
	 * @param daysSinceEpoch
	 * @return
	 */
	@Override
	public Long format(final Tenant tenant, final long sequence, final int daysSinceEpoch) {
		StringBuilder builder = new StringBuilder(18);
		return Long.valueOf(builder.append(daysSinceEpoch).append(tenant.getServiceList().get(0).getAlias())
				.append(leftPad(Long.toString(sequence), 11, "0")).toString());
	}
}
