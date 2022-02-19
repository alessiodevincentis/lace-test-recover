package com.woonders.lacemscommon.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 21/06/2017.
 */
@Builder
@Getter
@Setter
public class DevisProxTenantDto {

	// in teoria questo non serve
	private String devisProxTenantId;
	private String tenantName;
	private long aziendaId;
}
