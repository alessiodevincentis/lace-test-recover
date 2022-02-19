package com.woonders.lacemscommon.app.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 30/03/2017.
 */
@Getter
@Setter
@Builder
public class SendCampagnaDTO {

	private long campagnaId;
	private String tenantName;
	private String numeroMittente;
	private long idOperator;
	private long currentAziendaId;

}
