package com.woonders.lacemscommon.app.viewmodel;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by diegopizzo on 16/09/16.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "codiceAmm" }, callSuper = false)
@ToString
public class AmministrazioneViewModel extends AbstractBaseViewModel {

	private long codiceAmm;
	private String ragioneSociale;
	private String piva;
	private String codiceFiscale;
	private boolean cqs;
	private boolean dlg;
	private String indirizzoSedeLegale;
	private String luogoSedeLegale;
	private String provSedeLegale;
	private String capSedeLegale;
	private String civicoSedeLegale;
	private String nazioneSedeLegale;
	private String indirizzoSedeNotifica;
	private String luogoSedeNotifica;
	private String provSedeNotifica;
	private String capSedeNotifica;
	private String civicoSedeNotifica;
	private String nazioneSedeNotifica;
	private boolean notificaPec;
	private String indirizzoPec;
	private String referente;
	private String ruolo;
	private String telefono;
	private String fax;
	private String email;
	private String note;
	private List<ValutazioneAmministrazioneViewModel> valutazioneAmministrazioneViewModelList;
	private List<ClienteViewModel> clienteViewModelList;

	public void addValutazioneAmministrazioneViewModel(
			ValutazioneAmministrazioneViewModel valutazioneAmministrazioneViewModel) {
		if (valutazioneAmministrazioneViewModelList == null) {
			valutazioneAmministrazioneViewModelList = new LinkedList<>();
		}
		valutazioneAmministrazioneViewModelList.add(valutazioneAmministrazioneViewModel);
	}

	public void removeValutazioneAmministrazioneViewModel(
			ValutazioneAmministrazioneViewModel valutazioneAmministrazioneViewModel) {
		if (valutazioneAmministrazioneViewModelList != null) {
			valutazioneAmministrazioneViewModelList.remove(valutazioneAmministrazioneViewModel);
		}
	}

	@Override
	protected long getIdToCompare() {
		return codiceAmm;
	}
}
