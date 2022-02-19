package com.woonders.lacemsjsf.view.campagnemarketing;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.service.CampagnaMarketingService;
import com.woonders.lacemscommon.service.impl.CampagnaMarketingServiceImpl;

import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = StatisticheCampagneMarketingView.NAME)
@ViewScoped
@Getter
@Setter
public class StatisticheCampagneMarketingView {

	public static final String NAME = "statisticheCampagneMarketingView";
	public static final String JSF_NAME = "#{" + NAME + "}";
	private final String NOMINATIVO_DA_FILE = "Da File";
	@ManagedProperty(CampagnaMarketingServiceImpl.JSF_NAME)
	private CampagnaMarketingService campagnaMarketingService;
	private PieChartModel pieModelEsitoTotale;
	private PieChartModel pieModelEsitoRicevutoAndTipo;
	private PieChartModel pieModelEsitoNonRicevutoAndTipo;

	public PieChartModel getPieModelEsitoTotale(long idCampagna) {
		if(pieModelEsitoTotale == null) {
			pieModelEsitoTotale = new PieChartModel();

			pieModelEsitoTotale.set("Ricevuti",
					campagnaMarketingService.countByEsitoCampagna(idCampagna, EsitoSmsNotificaLead.RICEVUTO));
			pieModelEsitoTotale.set("Non Ricevuti",
					campagnaMarketingService.countByEsitoCampagna(idCampagna, EsitoSmsNotificaLead.NON_RICEVUTO));
			pieModelEsitoTotale.set("Spediti",
					campagnaMarketingService.countByEsitoCampagna(idCampagna, EsitoSmsNotificaLead.SPEDITO));
			pieModelEsitoTotale.set("In spedizione",
					campagnaMarketingService.countByEsitoCampagna(idCampagna, EsitoSmsNotificaLead.IN_SPEDIZIONE));

			pieModelEsitoTotale.setShowDataLabels(true);
			pieModelEsitoTotale.setLegendPosition("ne");
			pieModelEsitoTotale.setExtender("skinPie");
		}
		return pieModelEsitoTotale;
	}

	public PieChartModel getPieModelEsitoRicevutoAndTipo(long idCampagna) {
		if(pieModelEsitoRicevutoAndTipo == null) {
			pieModelEsitoRicevutoAndTipo = new PieChartModel();

			pieModelEsitoRicevutoAndTipo.set(Tipo.CLIENTE.getValue(), campagnaMarketingService
					.countByEsitoCampagnaAndTipo(idCampagna, EsitoSmsNotificaLead.RICEVUTO, Tipo.CLIENTE));
			pieModelEsitoRicevutoAndTipo.set(Tipo.NOMINATIVO.getValue(), campagnaMarketingService
					.countByEsitoCampagnaAndTipo(idCampagna, EsitoSmsNotificaLead.RICEVUTO, Tipo.NOMINATIVO));
			pieModelEsitoRicevutoAndTipo.set(NOMINATIVO_DA_FILE,
					campagnaMarketingService.countByEsitoCampagnaAndTipo(idCampagna, EsitoSmsNotificaLead.RICEVUTO, null));

			pieModelEsitoRicevutoAndTipo.setShowDataLabels(true);
			pieModelEsitoRicevutoAndTipo.setLegendPosition("ne");
			pieModelEsitoRicevutoAndTipo.setExtender("skinPie");
		}
		return pieModelEsitoRicevutoAndTipo;
	}

	public PieChartModel getPieModelEsitoNonRicevutoAndTipo(long idCampagna) {
		if(pieModelEsitoNonRicevutoAndTipo == null) {
			pieModelEsitoNonRicevutoAndTipo = new PieChartModel();

			pieModelEsitoNonRicevutoAndTipo.set(Tipo.CLIENTE.getValue(), campagnaMarketingService
					.countByEsitoCampagnaAndTipo(idCampagna, EsitoSmsNotificaLead.NON_RICEVUTO, Tipo.CLIENTE));
			pieModelEsitoNonRicevutoAndTipo.set(Tipo.NOMINATIVO.getValue(), campagnaMarketingService
					.countByEsitoCampagnaAndTipo(idCampagna, EsitoSmsNotificaLead.NON_RICEVUTO, Tipo.NOMINATIVO));
			pieModelEsitoNonRicevutoAndTipo.set(NOMINATIVO_DA_FILE, campagnaMarketingService
					.countByEsitoCampagnaAndTipo(idCampagna, EsitoSmsNotificaLead.NON_RICEVUTO, null));

			pieModelEsitoNonRicevutoAndTipo.setShowDataLabels(true);
			pieModelEsitoNonRicevutoAndTipo.setLegendPosition("ne");
			pieModelEsitoNonRicevutoAndTipo.setExtender("skinPie");
		}
		return pieModelEsitoNonRicevutoAndTipo;
	}
}
