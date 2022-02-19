package com.woonders.lacemsjsf.view.nominativo.statonominativo;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.service.StatoNominativoService;
import com.woonders.lacemscommon.service.impl.StatoNominativoServiceImpl;
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

@ManagedBean(name = StatoNominativoView.NAME)
@ViewScoped
@Getter
@Setter
public class StatoNominativoView implements Serializable {

    public static final String NAME = "statoNominativoView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private final String yellowColor = "Yellow";
    private final String whiteColor = "White";
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(StatoNominativoServiceImpl.JSF_NAME)
    private StatoNominativoService statoNominativoService;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    private List<String> operatoriSelezionati;
    private String operatore;
    


    public long getStatoNominativoSize(String statoNominativo) {
    	long countSize = 0;
    	if (operatoriSelezionati != null && operatoriSelezionati.size() > 0)
    	{
    		countSize = statoNominativoService.countNominativiByStatoNominativoAndOperators(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
                    httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ), statoNominativo, operatoriSelezionati);
    	}
    	else
    	{
    		countSize = statoNominativoService.countNominativiByStatoNominativo(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
	                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ), statoNominativo);
    	}
    	
    	return countSize;
    }
    

    public void putStatoNominativoParameter(String statoNominativo) throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.STATO_NOMINATIVO_PARAMETER, statoNominativo);
        passaggioParametriUtils.getParametri().put(Parametro.OPERATORS, operatoriSelezionati);
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Constants.getDatatableStatoNominativoPath(true));
    }

    public String getColorIcon(String statoNominativo) {
        if (statoNominativoService.isContainsDataRecallNominativoLessToday(statoNominativo,
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
                httpSessionUtil.getOperatorId(), aziendaSelectionView.getCurrentAziendaId())) {
            return yellowColor;
        } else {
            return whiteColor;
        }
    }
    

    public void onItemSelect() {
//    
    }
}
