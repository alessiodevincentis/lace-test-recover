package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.ClientePraticaFileViewModel;
import com.woonders.lacemscommon.db.entity.ClientePraticaFile;
import org.springframework.stereotype.Component;

/**
 * Created by ema98 on 9/15/2017.
 */
@Component
public class ClientePraticaFileViewModelCreator extends AbstractBaseViewModelCreator<ClientePraticaFile, ClientePraticaFileViewModel> {

    @Override
    public ClientePraticaFile createModel(ClientePraticaFileViewModel viewModel) {
        if (viewModel != null) {
            return ClientePraticaFile.builder()
                    .id(viewModel.getId())
                    .cliente(viewModel.getCliente())
                    .pratica(viewModel.getPratica())
                    .fileCategory(viewModel.getFileCategory())
                    .fileName(viewModel.getFileName())
                    .length(viewModel.getLength())
                    .build();
        }
        return null;
    }

    @Override
    public ClientePraticaFileViewModel createViewModel(ClientePraticaFile model) {
        if (model != null) {
            return ClientePraticaFileViewModel.builder()
                    .id(model.getId())
                    .cliente(model.getCliente())
                    .pratica(model.getPratica())
                    .fileCategory(model.getFileCategory())
                    .fileName(model.getFileName())
                    .length(model.getLength())
                    .build();
        }
        return null;
    }
}
