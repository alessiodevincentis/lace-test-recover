package com.woonders.lacemscommon.app.viewmodel;

import java.util.Date;
import java.util.UUID;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id", "viewId" }, callSuper = false)
@ToString
public class ValutazioneAmministrazioneViewModel extends AbstractBaseViewModel {

	private final String viewId = UUID.randomUUID().toString();
	private long id;
	private String compagnia;
	private String valutazione;
	private Date dataValutazione;
	private String infoAggiuntive;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompagnia() {
		return compagnia;
	}

	public void setCompagnia(String compagnia) {
		this.compagnia = compagnia;
	}

	public String getValutazione() {
		return valutazione;
	}

	public void setValutazione(String valutazione) {
		this.valutazione = valutazione;
	}

	public Date getDataValutazione() {
		return dataValutazione;
	}

	public void setDataValutazione(Date dataValutazione) {
		this.dataValutazione = dataValutazione;
	}

	public String getInfoAggiuntive() {
		return infoAggiuntive;
	}

	public void setInfoAggiuntive(String infoAggiuntive) {
		this.infoAggiuntive = infoAggiuntive;
	}

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
