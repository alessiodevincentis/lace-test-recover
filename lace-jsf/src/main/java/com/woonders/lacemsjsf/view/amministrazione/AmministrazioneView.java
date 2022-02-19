package com.woonders.lacemsjsf.view.amministrazione;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.FinanziariaViewModel;
import com.woonders.lacemscommon.app.viewmodel.ValutazioneAmministrazioneViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.AmministrazioneService;
import com.woonders.lacemscommon.service.FinanziariaService;
import com.woonders.lacemscommon.service.ValutazioneAmministrazioneService;
import com.woonders.lacemscommon.service.impl.AmministrazioneServiceImpl;
import com.woonders.lacemscommon.service.impl.FinanziariaServiceImpl;
import com.woonders.lacemscommon.service.impl.ValutazioneAmministrazioneServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.util.ConstantResolverView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataAccessException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "amministrazioneView")
@ViewScoped
@Getter
@Setter
public class AmministrazioneView implements Serializable {

    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;

    @ManagedProperty(ConstantResolverView.JSF_NAME)
    private ConstantResolverView constantResolverView;

    @ManagedProperty(AmministrazioneServiceImpl.JSF_NAME)
    private AmministrazioneService amministrazioneService;

    @ManagedProperty(ValutazioneAmministrazioneServiceImpl.JSF_NAME)
    private ValutazioneAmministrazioneService valutazioneAmministrazioneService;

    @ManagedProperty("#{passaggioParametriUtils}")
    private PassaggioParametriUtils passaggioParametriUtils;

    @ManagedProperty(FinanziariaServiceImpl.JSF_NAME)
    private FinanziariaService finanziariaService;

    private AmministrazioneViewModel amministrazioneViewModel;
    private FinanziariaViewModel selectedFinanziariaViewModel;

    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;

    @PostConstruct
    public void init() {
        populateAmministration();
    }

    public void saveNewAmministrazione() {
        try {
            amministrazioneViewModel = amministrazioneService.save(amministrazioneViewModel);
            FacesUtil.addMessage("Amministrazione salvata con successo");
        } catch (final DataAccessException e) {
            FacesUtil.addMessage("Errore nell'inserimento dell'Amministrazione", FacesMessage.SEVERITY_ERROR);
        }

    }

    public void populateAmministration() {
        amministrazioneViewModel = (AmministrazioneViewModel) passaggioParametriUtils.getParametri()
                .get(Parametro.AMMINISTRAZIONE_VIEWMODEL_PARAMETER);

        if (amministrazioneViewModel != null) {
            amministrazioneViewModel.setValutazioneAmministrazioneViewModelList(valutazioneAmministrazioneService
                    .findDistinctByAmministrazione_CodiceAmm(amministrazioneViewModel.getCodiceAmm()));
        } else {
            amministrazioneViewModel = new AmministrazioneViewModel();
        }

    }

    public void printPdfAmministration() {
        // TODO controllare lato server stampa pdf abilitata
        passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_PDF_SCHEDA_ATC_PARAMETER, amministrazioneViewModel);
        passaggioParametriUtils.getParametri().put(Parametro.SELECTED_FINANZIARIA, selectedFinanziariaViewModel);
        FacesUtil.openNewWindow(Constants.getPdfAmministrazionePath(false));
    }

    public void removeValutazioneAction(final ValutazioneAmministrazioneViewModel valutazioneAmministrazioneViewModel) {
        try {
            if (valutazioneAmministrazioneViewModel.getId() != 0) {
                valutazioneAmministrazioneService.delete(valutazioneAmministrazioneViewModel.getId());
            }
            amministrazioneViewModel.removeValutazioneAmministrazioneViewModel(valutazioneAmministrazioneViewModel);
        } catch (final DataAccessException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addValutazioneAction() {
        final ValutazioneAmministrazioneViewModel valutazioneAmministrazioneViewModel = new ValutazioneAmministrazioneViewModel();
        amministrazioneViewModel.addValutazioneAmministrazioneViewModel(valutazioneAmministrazioneViewModel);
    }

    public boolean isMaxLimitValutazioni() {
        return amministrazioneViewModel != null
                && amministrazioneViewModel.getValutazioneAmministrazioneViewModelList() != null
                && amministrazioneViewModel.getValutazioneAmministrazioneViewModelList().size() == constantResolverView
                .getValutazioneListMaxSize();
    }

    public boolean isDisabledButtonPdf() {
        return !(amministrazioneViewModel != null && amministrazioneViewModel.getCodiceFiscale() != null
                && amministrazioneViewModel.getRagioneSociale() != null && amministrazioneViewModel.getPiva() != null
                && !isPrintPdfDisabled());
    }

    // TODO superclasse con metodo in comune
    public boolean isPrintPdfDisabled() {
        return !httpSessionUtil.isPrintPdfEnabled();
    }

    // TODO superclasse con metodo in comune?
    public List<FinanziariaViewModel> getFinanziariaList() {
        return finanziariaService.findAll();
    }

    public boolean isAmministrazioneButtonRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.AMMINISTRAZIONI_WRITE);
    }

}
