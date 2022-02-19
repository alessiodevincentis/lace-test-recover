package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.exception.RinnovoNotSavedException;

import java.util.List;

/**
 * Created by emanuele on 04/01/17.
 */

/**
 * Manages quotes created for new procedures
 */
public interface PreventivoService extends AppService {

    /**
     * delete quote
     */
    void deletePreventivo(final long preventivoId);


    //When we create a new quote, we can settle old procedures already active

    /**
     * return a list of own procedures still active
     */
    List<PraticaViewModel> getAllPraticheAttiveEstinguibili(final long idCliente);

    /**
     * return a list of procedures opened with another agency still active
     */
    List<TrattenuteViewModel> getAllTrattenuteEstinguibili(final long idCliente);

    /**
     * create a new procedure from a quote
     */
    void creaPratica(final PreventivoViewModel preventivoViewModel,
                     final List<PraticaViewModel> praticheDaEstinguereViewModelList,
                     final List<TrattenuteViewModel> coesistenzeDaEstinguereViewModelList, final long clienteId, final long aziendaId, final long operatorId)
            throws RinnovoNotSavedException;

}
