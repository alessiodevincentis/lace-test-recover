package com.woonders.lacemsjsf.view.antiriciclaggio;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.service.AntiriciclaggioService;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import lombok.Getter;
import lombok.Setter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Date;

@ManagedBean(name = "antiRiciclaggioView")
@ViewScoped
@Getter
@Setter
public class AntiRiciclaggioView implements Serializable {

	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	private Date dataCarStart;
	private Date dataCarEnd;
	private String cf;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;

	public String searchDataCaricamentoNonIstruita() {
		passaggioParametriUtils.getParametri().put(Parametro.DATACARICAMENTO_START_ANTIRICICLAGGIO_PARAMETER, dataCarStart);
		passaggioParametriUtils.getParametri().put(Parametro.DATACARICAMENTO_END_ANTIRICICLAGGIO_PARAMETER, dataCarEnd);
		return searchAntiriciclaggio(AntiriciclaggioService.TipoRicercaAntiriciclaggio.DATA);
	}

	public String searchByCf() {
		passaggioParametriUtils.getParametri().put(Parametro.CF_ANTIRICICLAGGIO_PARAMETER, cf);
		return searchAntiriciclaggio(AntiriciclaggioService.TipoRicercaAntiriciclaggio.CF);
	}

	private String searchAntiriciclaggio(AntiriciclaggioService.TipoRicercaAntiriciclaggio tipoRicercaAntiriciclaggio) {
		passaggioParametriUtils.getParametri().put(Parametro.TIPORICERCA_ANTIRICICLAGGIO_PARAMETER, tipoRicercaAntiriciclaggio);
		if (!httpSessionUtil.hasAuthority(RoleName.ANTI_RICICLAGGIO_OUT)) {
			return Constants.getDatatableAntiriciclaggioAdminPath(true);
		}
		return Constants.getDatatableAntiriciclaggioPath(true);
	}
}
