package com.woonders.lacemscommon.app.model;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientePratica {

    private ClienteViewModel clienteViewModel;
    private PraticaViewModel praticaViewModel;
    private TrattenuteViewModel trattenuteViewModel;
}
