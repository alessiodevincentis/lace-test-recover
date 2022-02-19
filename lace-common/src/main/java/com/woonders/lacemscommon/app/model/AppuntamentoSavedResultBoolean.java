package com.woonders.lacemscommon.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppuntamentoSavedResultBoolean {
	private boolean inviaNotificaClienteNominativo;
	private boolean inviaNotificaOperatore;
	private boolean smsInviatoClienteNominativo;
	private boolean smsInviatoOperatore;
	private boolean appuntamentoSalvato;
	private boolean smsNotSentErrorClienteNominativo;
	private boolean smsNotSentErrorOperatore;
}
