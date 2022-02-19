package com.woonders.lacemscommon.app.clicksend.model.response;

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
public class GetSmsHistoryDataResponse {

	@SerializedName("data")
	List<GetSmsHistoryResponse> getSmsHistoryResponseList;
	private long total;
	@SerializedName("per_page")
	private int perPage;
	@SerializedName("current_page")
	private long currentPage;
	@SerializedName("last_page")
	private long lastPage;
	@SerializedName("next_page_url")
	private String nextPageUrl;
	@SerializedName("prev_page_url")
	private String prevPageUrl;
	private int from;
	private int to;
}
