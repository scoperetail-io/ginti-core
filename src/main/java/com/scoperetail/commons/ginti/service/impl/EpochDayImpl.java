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

import com.scoperetail.commons.ginti.service.EpochDay;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class EpochDayImpl implements EpochDay {
  public static final long EPOCH_CONVERTER = (1000 * 60 * 60 * 24);
  //  TODO use Cache2k instead
  private Map<LocalDate, Integer> cache = new HashMap<>(1, 1);

  private int from(LocalDate currentDate) {
    int daysSinceEpoch = 0;
    if (cache.containsKey(currentDate)) {
      daysSinceEpoch = cache.get(currentDate);
    } else {
      cache.clear();
      daysSinceEpoch = (int) (System.currentTimeMillis() / EPOCH_CONVERTER);
      cache.put(currentDate, daysSinceEpoch);
    }
    return daysSinceEpoch;
  }

  @Override
  public int current() {
    // TODO - Use Cache2K cache loader to avoid the call of now() everytime
    return from(LocalDate.now());
  }
}
