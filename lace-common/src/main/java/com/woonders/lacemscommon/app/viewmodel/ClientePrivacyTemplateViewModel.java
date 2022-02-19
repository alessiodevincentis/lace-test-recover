package com.woonders.lacemscommon.app.viewmodel;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientePrivacyTemplateViewModel {

	private long id;
	private long clienteId;
	private long privacyTemplatesId;
    private Date dateCreate = new Date();
	
}
