package com.woonders.lacemscommon.fattureincloud.network.response.dettaglidoc;

import com.google.gson.annotations.SerializedName;
import com.woonders.lacemscommon.fattureincloud.network.response.BaseResponse;

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
public class DettagliDocResponse extends BaseResponse {

	@SerializedName("dettagli_documento")
	DettagliDocumento dettagliDocumento;
}
