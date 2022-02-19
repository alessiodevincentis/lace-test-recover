package com.woonders.lacemscommon.app.viewmodel;

import java.io.Serializable;

/**
 * Created by emanuele on 12/01/17.
 */
public abstract class AbstractBaseViewModel implements Comparable<AbstractBaseViewModel>, Serializable {

	protected abstract long getIdToCompare();

	@Override
	public int compareTo(AbstractBaseViewModel o) {
		return Long.compare(getIdToCompare(), o.getIdToCompare());
	}
}
