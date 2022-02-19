package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.app.viewmodel.creator.*;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.entityenum.Provenienza;
import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.entityutil.EstinzioneUtil;
import com.woonders.lacemscommon.db.entityutil.TrattenutaUtil;
import com.woonders.lacemscommon.db.repository.*;
import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.service.LogService;
import com.woonders.lacemscommon.service.NominativoService;
import com.woonders.lacemscommon.service.OperatorService;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
public class NominativoServiceImpl extends AbstractAppServiceImpl implements NominativoService {

    public static final String NAME = "nominativoServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";
    @Autowired
    private AmministrazioneViewModelCreator amministrazioneViewModelCreator;
    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private TrattenuteRepository trattenuteRepository;
    @Autowired
    private ResidenzaRepository residenzaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ContoRepository contoRepository;
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private EstinzioneRepository estinzioneRepository;
    @Autowired
    private NotificaLeadSmsRepository notificaLeadSmsRepository;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private ResidenzaViewModelCreator residenzaViewModelCreator;
    @Autowired
    private TrattenuteViewModelCreator trattenuteViewModelCreator;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private EstinzioneViewModelCreator estinzioneViewModelCreator;
    @Autowired
    private NotificaLeadSmsViewModelCreator notificaLeadSmsViewModelCreator;
    @Autowired
    private AmministrazioneRepository amministrazioneRepository;
    @Autowired
    private LogService logService;
    @Autowired
    private CalendarioAppuntamentoRepository calendarioAppuntamentoRepository;
    @Autowired
    private OperatorService operatorService;
    @Autowired
    private TrattenutaUtil trattenutaUtil;
    @Autowired
    private EstinzioneUtil estinzioneUtil;
    @Autowired
    private OperatorRepository operatorRepository;

    @PreAuthorize("hasAuthority('NOMINATIVI_WRITE_SUPER') or (hasAuthority('NOMINATIVI_WRITE_ALL') and @authorizationConditionChecker.isSameAziendaId(#nominativoViewModel.operatoreNominativo.azienda.id)) or (hasAuthority('NOMINATIVI_WRITE_OWN') && #nominativoViewModel.operatoreNominativo.username.equals(authentication.name))")
    @Override
    @Transactional
    public ClienteViewModel saveNominativo(final ClienteViewModel nominativoViewModel,
                                           final ResidenzaViewModel residenzaViewModel, final List<PraticaViewModel> preventivoViewModelList,
                                           final List<TrattenuteViewModel> trattenuteNominativoViewModelList,
                                           final List<AmministrazioneViewModel> amministrazioneViewModelList, final String commentoLog, final long operatorId) {

        final Cliente nominativo = clienteViewModelCreator.createModel(nominativoViewModel);
        Residenza residenza = residenzaViewModelCreator.createModel(residenzaViewModel);
        if (residenza == null) {
            residenza = new Residenza();
        }

        nominativo.setResidenza(residenza);
        residenza.setCliente(nominativo);

        final boolean newNominativo = nominativo.getId() == 0;

        if (!newNominativo) {
            saveNominativoRelatedFields(nominativo, preventivoViewModelList, trattenuteNominativoViewModelList, amministrazioneViewModelList);
        }
        

        final Cliente nominativoSalvato = clienteRepository.save(nominativo);
        residenza = residenzaRepository.save(residenza);
        nominativoSalvato.setResidenza(residenza);

        if (newNominativo) {
            saveNominativoRelatedFields(nominativo, preventivoViewModelList, trattenuteNominativoViewModelList, amministrazioneViewModelList);
        }
        
        logService.log(nominativoSalvato, commentoLog, operatorId);

        final ClienteViewModel clienteViewModel = clienteViewModelCreator.createViewModel(nominativoSalvato);
        clienteViewModel
                .setResidenzaViewModel(residenzaViewModelCreator.createViewModel(nominativoSalvato.getResidenza()));
        clienteViewModel.setAmministrazioneViewModelList(
                amministrazioneViewModelCreator.fromSet(nominativoSalvato.getAmministrazione()));
        clienteViewModel
                .setTrattenutaViewModelList(trattenuteViewModelCreator.fromSet(nominativoSalvato.getTrattenuta()));
        clienteViewModel.setPraticaViewModelList(praticaViewModelCreator.fromSet(nominativoSalvato.getPratica()));
        return clienteViewModel;
    }

    @Override
    @Transactional
    public void updateNominativiOperator(final ArrayList<ClienteViewModel> nominativoViewModelList, long operatorId)
    {
        Operator operator = operatorRepository.findOne(operatorId);
        for (ClienteViewModel cwm: nominativoViewModelList) {
            if (cwm != null){
                Cliente cliente = clienteRepository.findById(cwm.getId());
                cliente.setOperatoreNominativo(operator);
                clienteRepository.save(cliente);
            }
        }
    }

    private void saveNominativoRelatedFields(final Cliente nominativo, final List<PraticaViewModel> preventivoViewModelList, final List<TrattenuteViewModel> trattenuteNominativoViewModelList,
                                             final List<AmministrazioneViewModel> amministrazioneViewModelList) {

        if (amministrazioneViewModelList != null) {
            nominativo
                    .setAmministrazione(amministrazioneViewModelCreator.fromList(amministrazioneViewModelList));
            for (final Amministrazione amministrazione : nominativo.getAmministrazione()) {
                final Set<Cliente> nominativoSet = new HashSet<>();
                nominativoSet.add(nominativo);
                amministrazione.setCliente(nominativoSet);
            }
            amministrazioneRepository.save(nominativo.getAmministrazione());
        }

        if (trattenuteNominativoViewModelList != null) {
            nominativo.setTrattenuta(trattenuteViewModelCreator.fromList(trattenuteNominativoViewModelList));
            for (final Trattenute trattenuta : nominativo.getTrattenuta()) {
                trattenuta.setCliente(nominativo);
            }
            trattenuteRepository.save(nominativo.getTrattenuta());
        }

        if (preventivoViewModelList != null) {
            nominativo.setPratica(praticaViewModelCreator.fromList(preventivoViewModelList));
            for (final Pratica preventivo : nominativo.getPratica()) {
                preventivo.setDataCaricamento(new Date());
                preventivo.setStatoPratica(StatoPratica.PREISTRUTTORIA.getValue());
                preventivo.setCliente(nominativo);
            }
            praticaRepository.save(nominativo.getPratica());
        }
    }

    @Override
    @Transactional
    public void deletePreventivo(final long idPreventivo) {
        praticaRepository.delete(idPreventivo);
    }

    @Override
    @Transactional
    public void deleteTrattenuta(final TrattenuteViewModel trattenuteViewModel) {
        trattenuteRepository.delete(trattenuteViewModel.getCodStip());
    }

    @Override
    @Transactional
    public void creaPraticaDaPreventivoNominativo(final ClienteViewModel clienteViewModel,
                                                  final ResidenzaViewModel residenzaViewModel, final List<TrattenuteViewModel> trattenuteViewModelList,
                                                  final List<TrattenuteViewModel> impegniSelezionati, final List<PraticaViewModel> preventivoViewModelList,
                                                  final PraticaViewModel preventivoDaCreare, final long operatorId, final long aziendaId) {

        // setto l'anagrafica e la pratica da creare
        clienteViewModel.setTipo(Tipo.CLIENTE.getValue());
        clienteViewModel.setDataCliente(new Date());
        if (clienteViewModel.getProvenienza().equalsIgnoreCase(Provenienza.WEB.getValue())) {
            preventivoDaCreare.setWeb(true);
        }
        preventivoDaCreare.setDataCaricamento(new Date());
        preventivoDaCreare.setStatoPratica(StatoPratica.PREISTRUTTORIA.getValue());
        preventivoDaCreare
                .setDataNotificaStatoPratica(getDataNotificaByStatoPraticaAndAziendaId(StatoPratica.PREISTRUTTORIA.getValue(), aziendaId));
        preventivoDaCreare.setOperatore(operatorService.getOne(operatorId));
        // TODO perche il calcolo sta anche qui????
        if (clienteViewModel.getNettoMensileNominativo() != null) {
            preventivoDaCreare.setQuintoMax(clienteViewModel.getNettoMensileNominativo()
                    .divide(new BigDecimal("5"), BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        preventivoDaCreare.getDataRinnovo();
        // TODO non so perche famo sta cosa ma per ora la lascio cosi
        preventivoDaCreare.setSpese(new BigDecimal(616.00));

        // elimina dalla lista dei preventivi il preventivo da creare
        preventivoViewModelList.remove(preventivoDaCreare);
        // elimina tutti i preventivi residui
        praticaRepository.delete(praticaViewModelCreator.createModelList(preventivoViewModelList));

        // setto le trattenute estinte
        final List<EstinzioneViewModel> estinzioneViewModelList = getEstinzioniFromImpegniInCorsoNominativo(
                impegniSelezionati);

        // elimino trattenute estinte
        for (final TrattenuteViewModel trattenuteEstinta : impegniSelezionati) {
            trattenuteViewModelList.remove(trattenuteEstinta);
            trattenuteRepository.delete(trattenuteViewModelCreator.createModel(trattenuteEstinta));
        }

        final Conto conto = new Conto();
        final Documento documento = new Documento();
        Residenza residenza = residenzaViewModelCreator.createModel(residenzaViewModel);
        if (residenza == null) {
            residenza = new Residenza();
        }

        final Cliente cliente = clienteViewModelCreator.createModel(clienteViewModel);
        final Pratica pratica = praticaViewModelCreator.createModel(preventivoDaCreare);

        cliente.setConto(conto);
        conto.setCliente(cliente);
        contoRepository.save(conto);
        cliente.setDocumento(documento);
        documento.setCliente(cliente);
        documentoRepository.save(documento);
        cliente.setResidenza(residenza);
        residenza.setCliente(cliente);
        residenzaRepository.save(residenza);

        final Set<Pratica> praticaSet = new HashSet<>();
        praticaSet.add(pratica);
        cliente.setPratica(praticaSet);
        pratica.setCliente(cliente);
        final Pratica praticaSalvata = praticaRepository.save(pratica);

        if (estinzioneViewModelList != null) {
            final Set<Estinzione> estinzioneSet = estinzioneViewModelCreator.fromList(estinzioneViewModelList);
            pratica.setEstinzione(estinzioneSet);
            for (final Estinzione estinzione : estinzioneSet) {
                estinzione.setPratica(pratica);
                estinzioneRepository.save(estinzione);
            }
        }

        clienteRepository.save(cliente);

        logService.log(praticaSalvata, "Creata pratica da nominativo", operatorId);

        final List<CalendarioAppuntamento> calendarioAppuntamentoList = calendarioAppuntamentoRepository.findByNominativo_IdAndPratica_CodicePraticaIsNull(cliente.getId());
        for (final CalendarioAppuntamento calendarioAppuntamento : calendarioAppuntamentoList) {
            calendarioAppuntamento.setPratica(pratica);
            calendarioAppuntamentoRepository.save(calendarioAppuntamento);
        }

    }

    @Override
    @Transactional
    public void delete(final ClienteViewModel nominativoViewModel) throws UnableToDeleteException {
        if (nominativoViewModel == null
                || !Tipo.NOMINATIVO.getValue().equalsIgnoreCase(nominativoViewModel.getTipo())) {
            throw new UnableToDeleteException();
        }
        clienteRepository.delete(nominativoViewModel.getId());
    }

    private List<EstinzioneViewModel> getEstinzioniFromImpegniInCorsoNominativo(
            final List<TrattenuteViewModel> impegniSelezionati) {
        final List<EstinzioneViewModel> estinzioneViewModelList = new LinkedList<>();
        for (final TrattenuteViewModel trattenuteDaEstinguere : impegniSelezionati) {
            final EstinzioneViewModel estinzione = new EstinzioneViewModel();
            estinzione.setTipoEstinzione(trattenuteDaEstinguere.getTipoTrat());
            estinzione.setRataEstinzione(trattenuteDaEstinguere.getRataTrat());
            estinzione.setDurataEstinzione(trattenuteDaEstinguere.getDurataTrat());
            estinzione.setCalcolaScadenza(trattenuteDaEstinguere.getCalcolaDat());
            estinzione.setScadenzaEstinzione(trattenuteDaEstinguere.getScadenzaTrat());
            estinzione.setMontanteEstinzione(trattenuteDaEstinguere.getMontanteTrat());
            estinzione.setIstitutoEst(trattenuteDaEstinguere.getIstitutoTrat());
            estinzione.setDataBustapaga(trattenuteDaEstinguere.getBusta());
            estinzione.setDataRinnovoEstinzione(trattenutaUtil.calcDataRinnovoTrat(estinzione.getScadenzaEstinzione(),
                    estinzione.getDurataEstinzione(), estinzione.getTipoEstinzione()));
            estinzione.setDataNotificaConteggioEstinzione(null);
            estinzione.setStatoConteggioEstinzione(StatoConteggioEstinzione.RICHIESTA_CONTEGGIO);
            estinzioneViewModelList.add(estinzione);
        }
        return estinzioneViewModelList;
    }

    @Override
    public NotificaLeadSmsViewModel getNotificaLeadSms(final long nominativoId) {
        final NotificaLeadSmsViewModel notificaLeadSmsViewModel = notificaLeadSmsViewModelCreator
                .createViewModel(notificaLeadSmsRepository.findByCliente_Id(nominativoId));
        return notificaLeadSmsViewModel;
    }

	@Override
	public List<String> getAllProvince() {
		// TODO Auto-generated method stub
		return residenzaRepository.getDistinctProvice();
	}

}
