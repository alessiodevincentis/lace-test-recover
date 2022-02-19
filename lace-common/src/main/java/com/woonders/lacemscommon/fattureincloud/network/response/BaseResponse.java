package com.woonders.lacemscommon.fattureincloud.network.response;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.*;

/**
 * Created by Emanuele on 21/02/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseResponse implements Serializable {

	@SerializedName("success")
	private boolean success;
	@SerializedName("messaggio")
	private String messaggio;
	@SerializedName("limite_breve")
	private String limiteBreve;
	@SerializedName("limite_medio")
	private String limiteMedio;
	@SerializedName("limite_lungo")
	private String limiteLungo;
	@SerializedName("error")
	private String error;
	@SerializedName("error_code")
	private int errorCode;
}
