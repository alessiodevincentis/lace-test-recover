package com.woonders.lacemscommon.fattureincloud.network.request;

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
public abstract class BaseRequestBody implements Serializable {

	@SerializedName("api_uid")
	public String apiUid;
	@SerializedName("api_key")
	public String apiKey;
}
