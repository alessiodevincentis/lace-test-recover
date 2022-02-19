package com.woonders.lacemscommon.app.model;

import java.util.List;

/**
 * Created by Emanuele on 30/01/2017.
 */
public interface ViewModelPage<T> {

	List<T> getContent();

	int getTotalPages();

	long getTotalElements();
}
