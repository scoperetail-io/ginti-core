package com.scoperetail.commons.ginti.format;

import com.scoperetail.commons.ginti.config.Tenant;

public interface Formatter<ReturnType> {
  ReturnType format(final Tenant tenant, final long sequence, final int daysSinceEpoch);
}
