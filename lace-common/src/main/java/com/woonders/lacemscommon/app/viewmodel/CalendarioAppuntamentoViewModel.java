package com.woonders.lacemscommon.app.viewmodel;

import java.time.LocalDateTime;

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
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString
public class CalendarioAppuntamentoViewModel extends AbstractBaseViewModel {

	private long id;
	private String titolo;
	private LocalDateTime inizioAppuntamento;
	private String descrizione;
	private ClienteViewModel nominativo;
	private PraticaViewModel pratica;
	private OperatorViewModel operator;
	private OperatorViewModel operatorAssegnato;
	private LocalDateTime fineAppuntamento;
	private boolean invioSmsClienteNominativo;
	private boolean invioSmsOperatoreAssegnato;

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
