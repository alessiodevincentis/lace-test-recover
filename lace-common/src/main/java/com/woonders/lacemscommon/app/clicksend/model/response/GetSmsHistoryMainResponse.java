package com.woonders.lacemscommon.app.clicksend.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.*;

/**
 * Created by Emanuele on 18/01/2017.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetSmsHistoryMainResponse extends AbstractBaseResponse {

	@SerializedName("data")
	private GetSmsHistoryDataResponse getSmsHistoryDataResponse;
}
