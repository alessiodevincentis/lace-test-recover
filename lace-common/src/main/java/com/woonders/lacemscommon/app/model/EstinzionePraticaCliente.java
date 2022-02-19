package com.woonders.lacemscommon.app.model;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstinzionePraticaCliente {
    private PraticaViewModel praticaViewModel;
    private EstinzioneViewModel estinzioneViewModel;
    private ClienteViewModel clienteViewModel;
}
