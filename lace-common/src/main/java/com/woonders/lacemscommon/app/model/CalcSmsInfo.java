package com.woonders.lacemscommon.app.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 03/04/2017.
 */
@Getter
@Setter
@Builder
public class CalcSmsInfo {

	private int smsCount;
	private int destinatariCount;

}
