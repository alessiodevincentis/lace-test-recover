package com.woonders.lacemsjsf.view.pratica;

import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityutil.EstinzioneUtil;
import com.woonders.lacemscommon.db.entityutil.PraticaUtil;
import com.woonders.lacemscommon.service.ClienteManagerService;
import com.woonders.lacemscommon.service.impl.ClienteManagerServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.estinzione.EstinzioneView;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.util.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import static com.woonders.lacemsjsf.view.pratica.PraticaView.NAME;

/**
 * Created by Emanuele on 04/02/2017.
 */
@Setter
@ViewScoped
@ManagedBean(name = NAME)
public class PraticaView implements Serializable {

    public static final String NAME = "praticaView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @ManagedProperty(PraticaUtil.JSF_NAME)
    private PraticaUtil praticaUtil;
    @ManagedProperty(EstinzioneUtil.JSF_NAME)
    private EstinzioneUtil estinzioneUtil;
    @ManagedProperty(EstinzioneView.JSF_NAME)
    private EstinzioneView estinzioneView;
    @ManagedProperty(ClienteManagerServiceImpl.JSF_NAME)
    private ClienteManagerService clienteManagerService;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;

    public boolean isTipoPraticaNotSelected(final PraticaViewModel praticaViewModel) {
        return StringUtils.isEmpty(praticaViewModel.getTipoPratica());
    }

    public void calcMontante(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setMontante(praticaUtil.calcMontante(praticaViewModel));
    }

    public void calcDataRinnovo(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setDataRinnovo(praticaUtil.calcDataRinnovo(praticaViewModel));
    }

    public void updateDatiPraticaFromDecorrenza(final PraticaViewModel praticaViewModel) {
        // TODO controllare ordine corretto
        calcDataRinnovo(praticaViewModel);
        calcRateScadenzaPratica(praticaViewModel);
        calcDebitoResiduo(praticaViewModel);
        calcScadenzaPratica(praticaViewModel);
    }

    public void calcScadenzaPratica(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setScadenzaPratica(praticaUtil.calcScadenzaPratica(praticaViewModel));
    }

    public void calcRateScadenzaPratica(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setRateScadenza(praticaUtil.calcRateScadenza(praticaViewModel));
    }

    public void calcDebitoResiduo(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setDebitoResiduo(praticaUtil.calcDebitoResiduo(praticaViewModel));
    }

    public void calcPercProv(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setPercProv(praticaUtil.calcPercProv(praticaViewModel));
    }

    public void calcProvTotale(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setProvTotale(praticaUtil.calcProvTotale(praticaViewModel));
    }

    public void calcInteressi(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setInteressi(praticaUtil.calcInteressi(praticaViewModel));
    }

    public void calcNettoMensile(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setNettoMensile(praticaUtil.calcNettoMensile(praticaViewModel));
    }

    public void calcNettoRicavo(final PraticaViewModel praticaViewModel, final List<EstinzioneViewModel> estinzioneViewModelList) {
        praticaViewModel.setNettoRicavo(praticaUtil.calcNettoRicavo(praticaViewModel, estinzioneViewModelList));
    }

    public void calcConteggioEstinzioneTotale(final PraticaViewModel praticaViewModel,
                                              final List<EstinzioneViewModel> estinzioneViewModelList) {
        praticaViewModel.setConteggioEstinzioneTot(
                praticaUtil.calcConteggioEstinzioneTotale(praticaViewModel, estinzioneViewModelList));
    }

    public void updateDatiEstinzioneAndPraticaFromScadenza(final EstinzioneViewModel estinzioneViewModel,
                                                           final PraticaViewModel praticaViewModel, final List<EstinzioneViewModel> estinzioneViewModelList) {
        estinzioneViewModel.setMesiScadenza(estinzioneUtil.calcMesiScadenza(estinzioneViewModel));
        estinzioneViewModel.setConteggioEstinzione(estinzioneUtil.calcConteggioEstinzione(estinzioneViewModel));
        calcNettoRicavo(praticaViewModel, estinzioneViewModelList);
        calcConteggioEstinzioneTotale(praticaViewModel, estinzioneViewModelList);
    }

    public void updateDatiEstinzioneFromDataBustaPaga(final EstinzioneViewModel estinzioneViewModel,
                                                      final PraticaViewModel praticaViewModel, final List<EstinzioneViewModel> estinzioneViewModelList) {
        estinzioneViewModel.setScadenzaEstinzione(estinzioneUtil.calcScadenzaEstinzione(estinzioneViewModel));
        estinzioneViewModel.setConteggioEstinzione(estinzioneUtil.calcConteggioEstinzione(estinzioneViewModel));
        estinzioneViewModel.setMesiScadenza(estinzioneUtil.calcMesiScadenza(estinzioneViewModel));
        calcNettoRicavo(praticaViewModel, estinzioneViewModelList);
        calcConteggioEstinzioneTotale(praticaViewModel, estinzioneViewModelList);
    }

    public void resetEstinzione(final EstinzioneViewModel estinzioneViewModel, final PraticaViewModel praticaViewModel,
                                final List<EstinzioneViewModel> estinzioneViewModelList) {
        resetEstinzione(estinzioneViewModel);
        calcNettoRicavo(praticaViewModel, estinzioneViewModelList);
        calcConteggioEstinzioneTotale(praticaViewModel, estinzioneViewModelList);
    }

    private void resetEstinzione(final EstinzioneViewModel estinzioneViewModel) {
        if (estinzioneViewModel != null) {
            if (estinzioneViewModel.getCalcolaScadenza() != null
                    && !estinzioneViewModel.getCalcolaScadenza().isEmpty()) {
                estinzioneViewModel.setRataEstinzione(BigDecimal.ZERO);
                estinzioneViewModel.setMontanteEstinzione(BigDecimal.ZERO);
                estinzioneViewModel.setDataBustapaga(null);
                estinzioneViewModel.setMesiScadenza(0);
                estinzioneViewModel.setScadenzaEstinzione(null);
                estinzioneViewModel.setConteggioEstinzione(BigDecimal.ZERO);
                estinzioneViewModel.setDataNotificaConteggioEstinzione(null);
                estinzioneViewModel.setStatoConteggioEstinzione(null);
                estinzioneViewModel.setDataRinnovoEstinzione(null);
            }
        }
    }

    public void removeEstinzione(final EstinzioneViewModel estinzioneViewModel, final PraticaViewModel praticaViewModel,
                                 final List<EstinzioneViewModel> estinzioneViewModelList) {
        try {
            if (estinzioneViewModel.getIdEstinzione() != 0) {
                clienteManagerService.deleteEstinzione(estinzioneViewModel);
            }
            estinzioneViewModelList.remove(estinzioneViewModel);
            //ABLE - UPDATE PRATICA
            praticaViewModel.setEstinzioneViewModelList(estinzioneViewModelList);
            calcNettoRicavo(praticaViewModel, estinzioneViewModelList);
            calcConteggioEstinzioneTotale(praticaViewModel, estinzioneViewModelList);
        } catch (final DataAccessException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void resetProvvigioneEuro(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setProvvigione(BigDecimal.ZERO);
    }

    public boolean isDataPerfezionamentoRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.FATTURAZIONE_WRITE, Role.RoleName.FATTURAZIONE_WRITE_SUPER);
    }

    public boolean isSimulatorTableUsed(final PraticaViewModel pratica) {
        return pratica.getSimulatorTableViewModel() != null;
    }

}
