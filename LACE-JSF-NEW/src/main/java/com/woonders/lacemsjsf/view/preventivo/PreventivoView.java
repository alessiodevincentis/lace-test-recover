package com.woonders.lacemsjsf.view.preventivo;

import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.db.entityutil.PraticaUtil;
import com.woonders.lacemscommon.db.entityutil.PreventivoUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import lombok.Setter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;

import static com.woonders.lacemsjsf.view.preventivo.PreventivoView.NAME;

/**
 * Created by Emanuele on 03/02/2017.
 */
@Setter
@ViewScoped
@ManagedBean(name = NAME)
public class PreventivoView implements Serializable {

    public static final String NAME = "preventivoView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @ManagedProperty(PreventivoUtil.JSF_NAME)
    private PreventivoUtil preventivoUtil;
    @ManagedProperty(PraticaUtil.JSF_NAME)
    private PraticaUtil praticaUtil;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;

    public void updatePreventivoProvvigione(final PreventivoViewModel preventivoViewModel) {
        preventivoViewModel.setPercProvvigione(preventivoUtil.calcPercentualeProvvigione(preventivoViewModel));
    }

    public void updatePreventivoProvvigioneTotale(final PreventivoViewModel preventivoViewModel) {
        preventivoViewModel.setProvTotale(preventivoUtil.calcProvvigioneTotale(preventivoViewModel));
    }

    public void updatePreventivoInteressi(final PreventivoViewModel preventivoViewModel) {
        preventivoViewModel.setInteressi(preventivoUtil.calcInteressi(preventivoViewModel));
    }

    // TODO questo e tutto duplicato per dio, i preventivi vanno fatti in un
    // modo solo!!!
    public void updatePreventivoProvvigione(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setPercProv(preventivoUtil.calcPercentualeProvvigione(praticaViewModel));
    }

    public void updatePreventivoProvvigioneTotale(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setProvTotale(preventivoUtil.calcProvvigioneTotale(praticaViewModel));
    }

    public void resetProvvigioneEuro(final PreventivoViewModel preventivoViewModel) {
        preventivoViewModel.setProvvigione(BigDecimal.ZERO);
    }

    public void resetProvvigioneEuro(final PraticaViewModel praticaViewModel) {
        praticaViewModel.setProvvigione(BigDecimal.ZERO);
    }

    public boolean isDisabledCreaPratica(final long idPreventivo) {
        return idPreventivo == 0;
    }


    public boolean isSimulatorTableUsed(final PraticaViewModel preventivo) {
        return preventivo.getSimulatorTableViewModel() != null;
    }

    public boolean isSimulatorTableUsed(final PreventivoViewModel preventivo) {
        return preventivo.getSimulatorTableViewModel() != null;
    }

    public void calcInteressi(final PraticaViewModel preventivo) {
        preventivo.setInteressi(praticaUtil.calcInteressi(preventivo));
    }

}
