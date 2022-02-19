package com.woonders.lacemsjsf.view.statopratiche;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import com.woonders.lacemscommon.service.StatoPraticheService;
import com.woonders.lacemscommon.service.impl.StatoPraticheServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import static com.woonders.lacemsjsf.view.statopratiche.StatoPraticheView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class StatoPraticheView implements Serializable {

    public static final String NAME = "statoPraticheView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private final String yellowColor = "Yellow";
    private final String whiteColor = "White";
    private final String CQS = Pratica.TipoPratica.CESSIONE_S.getValue();
    private final String PP = Pratica.TipoPratica.PRESTITO.getValue();
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(StatoPraticheServiceImpl.JSF_NAME)
    private StatoPraticheService statoPraticheService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    private List<String> operatoriSelezionati;
    private String operatore;

    public long getStatoPraticaSize(final String statoPratica, final String tipoPratica) {
    	long countSize = 0;
    	if (operatoriSelezionati != null && operatoriSelezionati.size() > 0)
    	{
    		return statoPraticheService.countByStatoPraticaAndTipoClienteAndUsernameAndOperators(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
                    httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ), statoPratica, tipoPratica, operatoriSelezionati);
    	}
    	else
    	{
    		return statoPraticheService.countByStatoPraticaAndTipoClienteAndUsername(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
                    httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ), statoPratica, tipoPratica);
    	}
        
    }

    public void putStatoPraticaParameter(final String statoPratica, final String tipoPratica) throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.STATO_PRATICA_PARAMETER, statoPratica);
        passaggioParametriUtils.getParametri().put(Parametro.TIPO_PRATICA_PANNELLO_STATO_PRATICA_PARAMETER,
                tipoPratica);
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getDatatableStatoPraticaPath(true));
    }

    public String getColorIcon(final String statoPratica, final String tipoPratica) {
        if (statoPraticheService.isContainsDatesToBeNotified(statoPratica, httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                httpSessionUtil.getOperatorId(), aziendaSelectionView.getCurrentAziendaId(), tipoPratica)) {
            return yellowColor;
        } else {
            return whiteColor;
        }
    }

    public long getStatoConteggioEstinzioneSize(final StatoConteggioEstinzione statoConteggioEstinzione, final String tipoEstinzione) {
    	long countSize = 0;
    	if (operatoriSelezionati != null && operatoriSelezionati.size() > 0)
    	{
    		return statoPraticheService.countEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsernameAndOperators(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
                    httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ), statoConteggioEstinzione, tipoEstinzione, operatoriSelezionati);
    	}
    	else
    	{
    		return statoPraticheService.countEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
                    httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ), statoConteggioEstinzione, tipoEstinzione);
    	}
    	
    	
    }

    public void putStatoConteggioEstinzioneParameter(final StatoConteggioEstinzione statoConteggioEstinzione, final String tipoEstinzione) throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.STATO_CONTEGGIO_ESTINZIONE_PARAMETER, statoConteggioEstinzione);
        passaggioParametriUtils.getParametri().put(Parametro.TIPO_ESTINZIONE_PANNELLO_STATO_CONTEGGIO_ESTINZIONE_PARAMETER,
                tipoEstinzione);
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getDatatableStatoConteggioEstinzionePath(true));
    }

    public String getColorIconConteggioEstinzione(final StatoConteggioEstinzione statoConteggioEstinzione, final String tipoEstinzione) {
        if (statoPraticheService.isContainsDatesToBeNotified(aziendaSelectionView.getCurrentAziendaId(),
                httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                statoConteggioEstinzione, tipoEstinzione)) {
            return yellowColor;
        } else {
            return whiteColor;
        }
    }
    
    public void onItemSelect() {
     
    }
}
