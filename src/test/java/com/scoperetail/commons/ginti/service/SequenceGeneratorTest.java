package com.scoperetail.commons.ginti.service;

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

import com.scoperetail.commons.ginti.GintiCoreApplication;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.scoperetail.commons.ginti.service.impl.EpochDayImpl.EPOCH_CONVERTER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("sequencegenerator")
@ComponentScan(basePackageClasses = GintiCoreApplication.class)
@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
class SequenceGeneratorTest {
  @Autowired private SequenceGenerator<Long> generator;
  private static final int EPOCH_DAY = (int) (System.currentTimeMillis() / EPOCH_CONVERTER);

  @Test
  void next() {
    final Long next = generator.next("05");
    assertEquals(Long.valueOf(EPOCH_DAY + "0500000000010"), next);
    assertEquals(18, StringUtils.length(next + ""));
  }
}
