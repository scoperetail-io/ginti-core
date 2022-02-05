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

import com.scoperetail.commons.ginti.config.GintiConfig;
import com.scoperetail.commons.ginti.format.Formatter;
import com.scoperetail.commons.ginti.persistence.SequenceDao;
import com.scoperetail.commons.ginti.service.EpochDay;
import com.scoperetail.commons.ginti.service.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LongTypeSequenceGeneratorImpl implements SequenceGenerator<Long> {
  @Value(value = "${scoperetail.ginti.sql}")
  private String sql;

  /** Caches the SQL needed to generate the next sequence for a specific tenant */
  private final Map<String, String> sqlCache = new HashMap<>(1, 1);

  private final SequenceDao dao;
  private final GintiConfig config;
  private final EpochDay epochDay;
  private final Formatter<Long> formatter;

  public LongTypeSequenceGeneratorImpl(
      final SequenceDao dao,
      final GintiConfig config,
      final EpochDay epochDay,
      final Formatter<Long> formatter) {
    this.dao = dao;
    this.config = config;
    this.epochDay = epochDay;
    this.formatter = formatter;
  }

  @Override
  public Long next(final String tenantId) {
    final long sequence = dao.next(sqlCache.get(tenantId));
    final int daysSinceEpoch = epochDay.current();
    return formatter.format(config.getTenant(tenantId), sequence, daysSinceEpoch);
  }

  @Override
  public Long next(final String tenantId, final String sequenceId) {
    return null;
  }

  @Override
  public List<Long> next(final String tenantId, final int count) {
    return null;
  }

  @Override
  public List<Long> next(final String tenantId, final String sequenceId, final int count) {
    return null;
  }
  /** Initialize the cache for sequence */
  @PostConstruct
  private void init() {
    config
        .getTenant()
        .forEach(
            tenant ->
                sqlCache.put(
                    tenant.getId(),
                    StringUtils.replace(sql, ":sequenceName", tenant.getSequence())));
    log.info("sqlCache:{}", sqlCache);
  }
}
