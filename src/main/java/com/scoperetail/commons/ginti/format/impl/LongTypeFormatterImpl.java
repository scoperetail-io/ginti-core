package com.scoperetail.commons.ginti.format.impl;

import com.scoperetail.commons.ginti.config.Tenant;
import com.scoperetail.commons.ginti.format.Formatter;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.leftPad;

/**
 * Default format is DDDDDTTNNNNNNNNNNN
 * <li>DDDDD - 5 digits represent the current day since the epoch. For example – January 20th, #
 *     2022 is day number 19,011 since January 1st, 1970
 * <li>XX - 2 digit division ID. Can be used to detect the correct shard.
 * <li>NNNNNNNNNNN - 11 digits sequence number that cycles between “00000000001” and “99999999999”
 *     (1 short of 100 Billion)
 *
 *     <p>You can override this class or implment the interface to change the default format
 */
@Service
public class LongTypeFormatterImpl implements Formatter<Long> {

  /**
   * We are not adding any validations to avoid the performance impact. It is the responsibility of
   * the caller of this API to ensure the format is correct and sequence object is set up.
   *
   * @param tenant
   * @param sequence
   * @param daysSinceEpoch
   * @return
   */
  @Override
  public Long format(final Tenant tenant, final long sequence, final int daysSinceEpoch) {
    StringBuilder builder = new StringBuilder(18);
    return Long.valueOf(
        builder
            .append(daysSinceEpoch)
            .append(tenant.getPrefix())
            .append(leftPad(Long.toString(sequence), 11, "0"))
            .toString());
  }
}
