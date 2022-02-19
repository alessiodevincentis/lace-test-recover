package com.woonders.lacemscommon.fattureincloud.network.request.dettaglidoc;

import com.google.gson.annotations.SerializedName;
import com.woonders.lacemscommon.fattureincloud.network.request.BaseRequestBody;

import lombok.*;

/**
 * Created by Emanuele on 26/02/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DettagliDocRequestBody extends BaseRequestBody {

	@SerializedName("id")
	public String id;
	@SerializedName("token")
	public String token;
}
