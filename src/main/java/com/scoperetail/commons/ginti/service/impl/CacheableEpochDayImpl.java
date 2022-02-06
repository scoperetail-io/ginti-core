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
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * A read through cache implementation to get calculaae the current epoch day. It expires after 60
 * minutes and loads on a cache miss.
 */
@Service
public class CacheableEpochDayImpl implements EpochDay {

  private Cache<String, Integer> epochDayCache =
      new Cache2kBuilder<String, Integer>() {}.name("epochDayCache-"+hashCode())
          .eternal(false)
          .entryCapacity(1)
          .expireAfterWrite(60 * 60 * 1000, TimeUnit.MILLISECONDS)
          .loader(key -> from(LocalDate.now()))
          .build();

  private int from(LocalDate currentDate) {
    return (int) (System.currentTimeMillis() / EPOCH_CONVERTER);
  }

  @Override
  public int current() {
    return epochDayCache.get("CURRENT_EPOCH_DAY");
  }
}
