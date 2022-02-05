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

import java.util.List;

/**
 * Main interface to be used by all consumers interested in generating sequences
 *
 * @author tushar agrawal
 */
public interface SequenceGenerator<ReturnType> {
  /**
   * Generate one sequence number for the given tenantId
   *
   * @param tenantId - key to identify the sequenceId and format for this sequence
   * @return Single sequence number formatted as per the configuration
   */
  ReturnType next(final String tenantId);
  /**
   * Generate one sequence number for the given tenantId and sequenceId
   *
   * @param tenantId - key to identify the sequenceId and format for this sequence
   * @param sequenceId - Specify the sequence object to be used in case the same tenant need more
   *     than one sequences.
   *     <p>For example: Tenant walmart need a sequence for Orders and Payment transaction. In that
   *     case we can configure two different database sequences for each type.
   * @return Single sequence number formatted as per the configuration
   */
  ReturnType next(final String tenantId, final String sequenceId);
  /**
   * Generate a list of sequence number for the given tenantId
   *
   * @param tenantId - key to identify the sequenceId and format for this sequence
   * @param count - Total number of sequences to be generated.
   * @return List of sequence number formatted as per the configuration
   */
  List<ReturnType> next(final String tenantId, final int count);
  /**
   * Generate one sequence number for the given tenantId and sequenceId
   *
   * @param tenantId - key to identify the sequenceId and format for this sequence
   * @param sequenceId - Specify the sequence object to be used in case the same tenant need more
   *     than one sequences.
   *     <p>For example: Tenant walmart need a sequence for Orders and Payment transaction. In that
   *     case we can configure two different database sequences for each type.
   * @param count - Total number of sequences to be generated.
   * @return List of sequence number formatted as per the configuration
   */
  List<ReturnType> next(final String tenantId, final String sequenceId, final int count);
}
