package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.app.viewmodel.creator.*;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.repository.*;
import com.woonders.lacemscommon.service.AppService;
import com.woonders.lacemscommon.util.DateToCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public abstract class AbstractAppServiceImpl implements AppService {

    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private ContoViewModelCreator contoViewModelCreator;
    @Autowired
    private DocumentoViewModelCreator documentoViewModelCreator;
    @Autowired
    private ResidenzaViewModelCreator residenzaViewModelCreator;
    @Autowired
    private TrattenuteViewModelCreator trattenuteViewModelCreator;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private EstinzioneViewModelCreator estinzioneViewModelCreator;
    @Autowired
    private PreventivoViewModelCreator preventivoViewModelCreator;
    @Autowired
    private CoobbligatoViewModelCreator coobbligatoViewModelCreator;
    @Autowired
    private PreferenzaStatoPraticaViewModelCreator preferenzaStatoPraticaViewModelCreator;
    @Autowired
    private PreventivoRepository preventivoRepository;
    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private TrattenuteRepository trattenuteRepository;
    @Autowired
    private EstinzioneRepository estinzioneRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private ResidenzaRepository residenzaRepository;
    @Autowired
    private ContoRepository contoRepository;
    @Autowired
    private CoobbligatoRepository coobbligatoRepository;
    @Autowired
    private PreferenzaStatoPraticaRepository preferenzaStatoPraticaRepository;

    @Override
    public ClienteViewModel findClienteByIdPratica(long praticaId) {
        return clienteViewModelCreator.createViewModel(clienteRepository.findByPratica_CodicePratica(praticaId));
    }

    @Override
    public List<PraticaViewModel> findPraticheByIdCliente(long clienteId) {
        return praticaViewModelCreator.fromModelList(praticaRepository.findDistinctByCliente_Id(clienteId));
    }

    @Override
    public List<TrattenuteViewModel> findTrattenuteByIdCliente(long clienteId) {
        return trattenuteViewModelCreator.fromModelList(trattenuteRepository.findDistinctByCliente_Id(clienteId));
    }

    @Override
    public ClienteViewModel findClienteByIdCliente(long clienteId) {
        return clienteViewModelCreator.createViewModel(clienteRepository.findOne(clienteId));
    }

    @Override
    public ResidenzaViewModel findResidenzaByIdCliente(final long clienteId) {
        return residenzaViewModelCreator.createViewModel(residenzaRepository.findDistinctByCliente_Id(clienteId));
    }

    @Override
    public ContoViewModel findContoByIdCliente(long clienteId) {
        return contoViewModelCreator.createViewModel(contoRepository.findDistinctByCliente_Id(clienteId));
    }

    @Override
    public DocumentoViewModel findDocumentoByIdCliente(long clienteId) {
        return documentoViewModelCreator.createViewModel(documentoRepository.findDistinctByCliente_Id(clienteId));
    }

    @Override
    public List<PreventivoViewModel> findPreventiviByIdCliente(final long idCliente) {
        return preventivoViewModelCreator.createViewModelList(
                preventivoRepository.findDistinctByCliente_IdAndCliente_Tipo(idCliente, Tipo.CLIENTE.getValue()));
    }

    @Override
    public List<EstinzioneViewModel> findEstinzioniByIdPratica(long praticaId) {
        return estinzioneViewModelCreator
                .fromModelList(estinzioneRepository.findDistinctByPratica_CodicePratica(praticaId));
    }

    @Override
    public List<CoobbligatoViewModel> findCoobbligatoByIdPratica(long praticaId) {
        return coobbligatoViewModelCreator
                .fromModelList(coobbligatoRepository.findDistinctByPratica_CodicePratica(praticaId));
    }

    @Override
    public PraticaViewModel findPraticaByCodicePratica(long praticaId) {
        return praticaViewModelCreator.createViewModel(praticaRepository.findOne(praticaId));
    }

    @Override
    public Date getDataNotificaByStatoPraticaAndAziendaId(String statoPratica, long aziendaId) {
        if (statoPratica != null && !statoPratica.isEmpty()) {
            StatoPratica stato = StatoPratica.fromValue(statoPratica);
            PreferenzaStatoPraticaViewModel preferenzaStatoPraticaViewModel = preferenzaStatoPraticaViewModelCreator
                    .createViewModel(preferenzaStatoPraticaRepository.findByNomeStatoPraticaAndAzienda_Id(stato, aziendaId));
            if (preferenzaStatoPraticaViewModel != null) {
                int giorniNotifica = preferenzaStatoPraticaViewModel.getGiorniNotifica();
                Date dataNotifica = DateToCalendar
                        .getDateWithoutTime(DateToCalendar.addDays(new Date(), giorniNotifica));
                return dataNotifica;
            }
        }
        return null;
    }
}
