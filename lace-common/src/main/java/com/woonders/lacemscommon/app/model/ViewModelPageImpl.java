package com.woonders.lacemscommon.app.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 30/01/2017.
 */
@Builder
@Getter
@Setter
public class ViewModelPageImpl<T> implements ViewModelPage<T> {

	private final List<T> content;
	private final int totalPages;
	private final long totalElements;

	public ViewModelPageImpl(List<T> content, int totalPages, long totalElements) {
		this.content = content;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
	}

}
