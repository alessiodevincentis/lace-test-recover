package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.db.entity.Azienda;
import org.springframework.stereotype.Component;

@Component
public class AziendaViewModelCreator extends AbstractBaseViewModelCreator<Azienda, AziendaViewModel> {

    @Override
    public Azienda createModel(final AziendaViewModel viewModel) {
        if (viewModel != null) {
            final Azienda azienda = new Azienda();
            azienda.setCap(viewModel.getCap());
            azienda.setCf(viewModel.getCf());
            azienda.setCognomeTitolare(viewModel.getCognomeTitolare());
            azienda.setDevisProxId(viewModel.getDevisProxId());
            azienda.setEmail(viewModel.getEmail());
            azienda.setFax(viewModel.getFax());
            azienda.setGiorniNotificheCliente(viewModel.getGiorniNotificheCliente());
            azienda.setGiorniNotificheNominativi(viewModel.getGiorniNotificheNominativi());
            azienda.setId(viewModel.getId());
            azienda.setIndirizzo(viewModel.getIndirizzo());
            azienda.setLuogo(viewModel.getLuogo());
            azienda.setNomeAzienda(viewModel.getNomeAzienda());
            azienda.setNomeTitolare(viewModel.getNomeTitolare());
            azienda.setOam(viewModel.getOam());
            azienda.setPec(viewModel.getPec());
            azienda.setPiva(viewModel.getPiva());
            azienda.setPrintPdfEnabled(viewModel.isPrintPdfEnabled());
            azienda.setProvincia(viewModel.getProvincia());
            azienda.setSitoWeb(viewModel.getSitoWeb());
            azienda.setTelefono(trimIfNotNull(viewModel.getTelefono()));
            azienda.setCellulare(trimIfNotNull(viewModel.getCellulare()));
            azienda.setGiorniNotificaConteggioEstinzione(viewModel.getGiorniNotificaConteggioEstinzione());
            return azienda;
        }
        return null;
    }

    @Override
    public AziendaViewModel createViewModel(final Azienda model) {
        if (model != null) {
            final AziendaViewModel aziendaViewModel = new AziendaViewModel();
            aziendaViewModel.setCap(model.getCap());
            aziendaViewModel.setCf(model.getCf());
            aziendaViewModel.setCognomeTitolare(model.getCognomeTitolare());
            aziendaViewModel.setDevisProxId(model.getDevisProxId());
            aziendaViewModel.setEmail(model.getEmail());
            aziendaViewModel.setFax(model.getFax());
            aziendaViewModel.setGiorniNotificheCliente(model.getGiorniNotificheCliente());
            aziendaViewModel.setGiorniNotificheNominativi(model.getGiorniNotificheNominativi());
            aziendaViewModel.setId(model.getId());
            aziendaViewModel.setIndirizzo(model.getIndirizzo());
            aziendaViewModel.setLuogo(model.getLuogo());
            aziendaViewModel.setNomeAzienda(model.getNomeAzienda());
            aziendaViewModel.setNomeTitolare(model.getNomeTitolare());
            aziendaViewModel.setOam(model.getOam());
            aziendaViewModel.setPec(model.getPec());
            aziendaViewModel.setPiva(model.getPiva());
            aziendaViewModel.setPrintPdfEnabled(model.isPrintPdfEnabled());
            aziendaViewModel.setProvincia(model.getProvincia());
            aziendaViewModel.setSitoWeb(model.getSitoWeb());
            aziendaViewModel.setTelefono(model.getTelefono());
            aziendaViewModel.setCellulare(model.getCellulare());
            aziendaViewModel.setGiorniNotificaConteggioEstinzione(model.getGiorniNotificaConteggioEstinzione());
            return aziendaViewModel;
        }
        return null;
    }

}
