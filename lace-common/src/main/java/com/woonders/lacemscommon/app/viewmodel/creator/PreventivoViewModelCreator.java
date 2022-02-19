package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.db.entity.Preventivo;
import com.woonders.lacemscommon.db.entityutil.PreventivoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PreventivoViewModelCreator extends AbstractBaseViewModelCreator<Preventivo, PreventivoViewModel> {

    @Autowired
    private PreventivoUtil preventivoUtil;
    @Autowired
    private SimulatorTableViewModelCreator simulatorTableViewModelCreator;

    @Override
    public Preventivo createModel(final PreventivoViewModel viewModel) {
        if (viewModel != null) {
            final Preventivo preventivo = new Preventivo();
            preventivo.setIdPreventivo(viewModel.getIdPreventivo());
            preventivo.setDurata(viewModel.getDurata());
            preventivo.setImporto(viewModel.getImporto());
            preventivo.setRata(viewModel.getRata());
            preventivo.setTipoPratica(viewModel.getTipoPratica());
            preventivo.setTan(viewModel.getTan());
            preventivo.setTaeg(viewModel.getTaeg());
            preventivo.setTeg(viewModel.getTeg());
            preventivo.setTipoProvvigione(viewModel.getTipoProvvigione());
            preventivo.setProvvigione(viewModel.getProvvigione());
            preventivo.setPercProvvigione(viewModel.getPercProvvigione());
            preventivo.setTipologiaPreventivo(viewModel.getTipologiaPreventivo());
            preventivo.setDataCreazione(viewModel.getDataCreazione());
            preventivo.setNotificaPreventivo(viewModel.isNotificaPreventivo());
            preventivo.setSpese(viewModel.getSpese());
            preventivo.setSimulatorTable(simulatorTableViewModelCreator.createModel(viewModel.getSimulatorTableViewModel()));
            return preventivo;
        }
        return null;
    }

    @Override
    public PreventivoViewModel createViewModel(final Preventivo model) {
        if (model != null) {
            final PreventivoViewModel preventivoViewModel = new PreventivoViewModel();
            preventivoViewModel.setIdPreventivo(model.getIdPreventivo());
            preventivoViewModel.setDurata(model.getDurata());
            preventivoViewModel.setImporto(model.getImporto());
            preventivoViewModel.setRata(model.getRata());
            preventivoViewModel.setTipoPratica(model.getTipoPratica());
            preventivoViewModel.setTan(model.getTan());
            preventivoViewModel.setTaeg(model.getTaeg());
            preventivoViewModel.setTeg(model.getTeg());
            preventivoViewModel.setTipoProvvigione(model.getTipoProvvigione());
            preventivoViewModel.setProvvigione(model.getProvvigione());
            preventivoViewModel.setPercProvvigione(model.getPercProvvigione());
            preventivoViewModel.setTipologiaPreventivo(model.getTipologiaPreventivo());
            preventivoViewModel.setDataCreazione(model.getDataCreazione());
            preventivoViewModel.setNotificaPreventivo(model.isNotificaPreventivo());
            preventivoViewModel.setSpese(model.getSpese());
            preventivoViewModel.setProvTotale(preventivoUtil.calcProvvigioneTotale(model));
            preventivoViewModel.setInteressi(preventivoUtil.calcInteressi(model));
            preventivoViewModel.setSimulatorTableViewModel(simulatorTableViewModelCreator.createViewModel(model.getSimulatorTable()));
            return preventivoViewModel;
        }
        return null;
    }

}
