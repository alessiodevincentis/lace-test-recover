package com.woonders.lacemscommon.app.clicksend.model.response;

import java.math.BigDecimal;
import java.util.List;

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
public class SendSmsDataResponse {

	@SerializedName("total_price")
	private BigDecimal totalPrice;
	@SerializedName("total_count")
	private long totalCount;
	@SerializedName("queued_count")
	private long queuedCount;
	@SerializedName("messages")
	private List<SendSmsResponse> sendSmsResponseList;

}
