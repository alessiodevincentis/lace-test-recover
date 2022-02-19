package com.woonders.lacemsapi.model.app;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseMessage {
	private String message;
	private ResponseCode responseCode;

	public enum ResponseCode {
		OK(200), MISSING_NAME_SURNAME(401), MISSING_PHONE_NUMBER(402), INVALID_CHECKSUM(403), UNEXPECTED(
				500), MISSING_PARAMETER(400);

		private int value;

		ResponseCode(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
