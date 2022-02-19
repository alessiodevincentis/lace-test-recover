package com.woonders.lacemscommon.fattureincloud.network.response.newdoc;

import com.google.gson.annotations.SerializedName;
import com.woonders.lacemscommon.fattureincloud.network.response.BaseResponse;

import lombok.*;

/**
 * Created by Emanuele on 21/02/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NewDocResponse extends BaseResponse {

	/**
	 * Identificativo del documento creato
	 */
	@SerializedName("new_id")
	private String newId;
	/**
	 * Identificativo permanente del documento (rimane lo stesso anche a seguito
	 * di modiifche)
	 */
	@SerializedName("token")
	private String token;
}
