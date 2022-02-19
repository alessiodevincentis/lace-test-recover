package com.woonders.lacemscommon.app.clicksend.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.*;

/**
 * Created by Emanuele on 18/01/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class AbstractBaseResponse {

	@SerializedName("http_code")
	private int httpCode;
	@SerializedName("response_code")
	private String responseCode;
	@SerializedName("response_msg")
	private String responseMsg;

	public enum ResponseCode { SUCCESS }

	public enum HttpStatusCode { HTTP_200(200);

	private int value;

	HttpStatusCode(int value) {
		this.value = value;
	}

		public int getValue() {
			return value;
		}
	}

	public boolean isOk() {
		return httpCode == HttpStatusCode.HTTP_200.getValue() && ResponseCode.SUCCESS.toString().equalsIgnoreCase(responseCode);
	}
}
