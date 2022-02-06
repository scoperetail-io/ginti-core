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
import com.scoperetail.commons.ginti.format.SequenceFormatter;
import com.scoperetail.commons.ginti.persistence.SequenceDao;
import com.scoperetail.commons.ginti.service.EpochDay;
import com.scoperetail.commons.ginti.service.GintiGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LongTypeSequenceGeneratorImpl implements GintiGenerator<Long> {
  public static final String SEQUENCE_NAME = ":sequenceName";

  @Value(value = "${scoperetail.ginti.sql}")
  private String sql;

  /** Caches the SQL needed to generate the next sequence for a specific tenant */
  private final Map<String, String> sqlCache = new HashMap<>(1, 1);

  private final SequenceDao dao;
  private final GintiConfig config;
  private final EpochDay epochDay;
  private final SequenceFormatter<Long> formatter;

  public LongTypeSequenceGeneratorImpl(
      final SequenceDao dao,
      final GintiConfig config,
      final EpochDay epochDay,
      final SequenceFormatter<Long> formatter) {
    this.dao = dao;
    this.config = config;
    this.epochDay = epochDay;
    this.formatter = formatter;
  }
  /** Initialize the cache for sequence */
  @PostConstruct
  private void init() {
    config
        .getTenant()
        .forEach(
            tenant ->
                sqlCache.put(
                    tenant.getId(), StringUtils.replace(sql, SEQUENCE_NAME, tenant.getSequence())));
    log.info("sqlCache:{}", sqlCache);
  }

  @Override
  public Long next(final String tenantId) {
    final String newSql = sqlCache.get(tenantId);
    return formattedSequence(tenantId, newSql);
  }

  @Override
  public Long next(final String tenantId, final String sequenceId) {
    final String newSql = StringUtils.replace(sql, SEQUENCE_NAME, sequenceId);
    return formattedSequence(tenantId, newSql);
  }

  @Override
  public List<Long> next(final String tenantId, final int count) {
    List<Long> sequences = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      sequences.add(next(tenantId));
    }
    return sequences;
  }

  @Override
  public List<Long> next(final String tenantId, final String sequenceId, final int count) {
    final String newSql = StringUtils.replace(sql, SEQUENCE_NAME, sequenceId);
    List<Long> sequences = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      sequences.add(formattedSequence(tenantId, newSql));
    }
    return sequences;
  }

  private Long formattedSequence(final String tenantId, final String newSql) {
    final long sequence = dao.next(newSql);
    final int daysSinceEpoch = epochDay.current();
    return formatter.format(config.getTenant(tenantId), sequence, daysSinceEpoch);
  }
}
