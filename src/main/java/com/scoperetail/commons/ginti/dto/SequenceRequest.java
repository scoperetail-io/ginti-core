package com.scoperetail.commons.ginti.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SequenceRequest {

	private String sequenceObject;
	private String sequenceFormat;
	private String alias;
}
