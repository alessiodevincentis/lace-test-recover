package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.app.viewmodel.creator.*;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.repository.*;
import com.woonders.lacemscommon.service.ClienteManagerService;
import com.woonders.lacemscommon.service.LogService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ClienteManagerServiceImpl extends AbstractAppServiceImpl implements ClienteManagerService {

    public static final String NAME = "clienteManagerServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private CoobbligatoViewModelCreator coobbligatoViewModelCreator;
    @Autowired
    private AmministrazioneViewModelCreator amministrazioneViewModelCreator;
    @Autowired
    private PreventivoViewModelCreator preventivoViewModelCreator;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EstinzioneRepository estinzioneRepository;
    @Autowired
    private TrattenuteRepository trattenuteRepository;
    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private CoobbligatoRepository coobbligatoRepository;
    @Autowired
    private PreventivoRepository preventivoRepository;
    @Autowired
    private ContoRepository contoRepository;
    @Autowired
    private ResidenzaRepository residenzaRepository;
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private ContoViewModelCreator contoViewModelCreator;
    @Autowired
    private DocumentoViewModelCreator documentoViewModelCreator;
    @Autowired
    private EstinzioneViewModelCreator estinzioneViewModelCreator;
    @Autowired
    private ResidenzaViewModelCreator residenzaViewModelCreator;
    @Autowired
    private TrattenuteViewModelCreator trattenuteViewModelCreator;
    @Autowired
    private AmministrazioneRepository amministrazioneRepository;
    @Autowired
    private LogService logService;
    @Autowired
    private ClientePraticaFileRepository clientePraticaFileRepository;
    @Autowired
    private ClientePraticaFileViewModelCreator clientePraticaFileViewModelCreator;

    @PreAuthorize("hasAuthority('CLIENTI_WRITE_SUPER') or (hasAuthority('CLIENTI_WRITE_ALL') and @authorizationConditionChecker.isSameAziendaId(#praticaViewModel.operatore.azienda.id)) or (hasAuthority('CLIENTI_WRITE_OWN') && #praticaViewModel.operatore.username.equals(authentication.name))")
    @Override
    @Transactional
    public ClientePratica saveNewClienteWithPratica(final ClienteViewModel clienteViewModel,
                                                    final ContoViewModel contoViewModel, final DocumentoViewModel documentoViewModel,
                                                    final ResidenzaViewModel residenzaViewModel, final List<TrattenuteViewModel> trattenuteViewModelList,
                                                    final List<EstinzioneViewModel> estinzioneViewModelList, final PraticaViewModel praticaViewModel,
                                                    final List<CoobbligatoViewModel> coobbligatoViewModelList,
                                                    final List<AmministrazioneViewModel> amministrazioneViewModelList,
                                                    final List<PreventivoViewModel> preventivoViewModelList, final String commentoLog, final long operatorId) {


        final boolean newCliente = clienteViewModel.getId() == 0;
        Cliente cliente = clienteViewModelCreator.createModel(clienteViewModel);
        final Conto conto = contoViewModelCreator.createModel(contoViewModel);
        final Documento documento = documentoViewModelCreator.createModel(documentoViewModel);
        final Residenza residenza = residenzaViewModelCreator.createModel(residenzaViewModel);
        Pratica pratica = praticaViewModelCreator.createModel(praticaViewModel);
        final Set<Pratica> praticaSet = new HashSet<>();
        praticaSet.add(pratica);
        
        cliente.setConto(conto);
        conto.setCliente(cliente);
        cliente.setDocumento(documento);
        documento.setCliente(cliente);
        cliente.setResidenza(residenza);
        residenza.setCliente(cliente);

        pratica.setCliente(cliente);
        cliente.setPratica(praticaSet);

        setTipoCliente(cliente);
        setPerfezionata(pratica);

        cliente = clienteRepository.save(cliente);
        pratica = praticaRepository.save(pratica);
        documentoRepository.save(documento);
        residenzaRepository.save(residenza);
        contoRepository.save(conto);


        if (!newCliente) {
            saveClientePraticaRelatedFields(cliente, pratica, trattenuteViewModelList, estinzioneViewModelList, coobbligatoViewModelList,
                    amministrazioneViewModelList, preventivoViewModelList);
        }

        if (newCliente) {
            saveClientePraticaRelatedFields(cliente, pratica, trattenuteViewModelList, estinzioneViewModelList, coobbligatoViewModelList,
                    amministrazioneViewModelList, preventivoViewModelList);
        }

        logService.log(pratica, commentoLog, operatorId);

        final ClientePratica clientePratica = new ClientePratica();
        clientePratica.setClienteViewModel(clienteViewModelCreator.createViewModel(cliente));
        clientePratica.setPraticaViewModel(praticaViewModelCreator.createViewModel(pratica));

        return clientePratica;
    }

    private void saveClientePraticaRelatedFields(final Cliente cliente, final Pratica pratica,
                                                 final List<TrattenuteViewModel> trattenuteViewModelList,
                                                 final List<EstinzioneViewModel> estinzioneViewModelList,
                                                 final List<CoobbligatoViewModel> coobbligatoViewModelList,
                                                 final List<AmministrazioneViewModel> amministrazioneViewModelList,
                                                 final List<PreventivoViewModel> preventivoViewModelList) {
        if (amministrazioneViewModelList != null) {
            cliente.setAmministrazione(amministrazioneViewModelCreator.fromList(amministrazioneViewModelList));
            for (final Amministrazione amministrazione : cliente.getAmministrazione()) {
                final Set<Cliente> clienteSet = new HashSet<>();
                clienteSet.add(cliente);
                amministrazione.setCliente(clienteSet);
            }
            amministrazioneRepository.save(cliente.getAmministrazione());
        }

        if (estinzioneViewModelList != null) {
            pratica.setEstinzione(estinzioneViewModelCreator.fromList(estinzioneViewModelList));
            for (final Estinzione estinzione : pratica.getEstinzione()) {
                estinzione.setPratica(pratica);
            }
            estinzioneRepository.save(pratica.getEstinzione());
        }

        if (trattenuteViewModelList != null) {
            cliente.setTrattenuta(trattenuteViewModelCreator.fromList(trattenuteViewModelList));
            for (final Trattenute trattenute : cliente.getTrattenuta()) {
                trattenute.setCliente(cliente);
            }
            trattenuteRepository.save(cliente.getTrattenuta());
        }

        if (coobbligatoViewModelList != null) {
            pratica.setCoobbligato(coobbligatoViewModelCreator.fromList(coobbligatoViewModelList));
            for (final Coobbligato coobbligato : pratica.getCoobbligato()) {
                coobbligato.setPratica(pratica);
            }
            coobbligatoRepository.save(pratica.getCoobbligato());
        }
        if (preventivoViewModelList != null) {
            cliente.setPreventivo(preventivoViewModelCreator.fromList(preventivoViewModelList));
            for (final Preventivo preventivo : cliente.getPreventivo()) {
                preventivo.setCliente(cliente);
            }
            preventivoRepository.save(cliente.getPreventivo());
        }
    }

    private void setTipoCliente(final Cliente cliente) {
        cliente.setTipo(Tipo.CLIENTE.getValue());
    }

    private void setPerfezionata(final Pratica pratica) {
        if (pratica.isPerfezionata() && pratica.getDataPerf() == null) {
            pratica.setDataPerf(new Date());
            pratica.setStatoPratica(StatoPratica.TERMINATA.getValue());
            pratica.setDataNotificaStatoPratica(null);
        }
    }

    @Override
    public ClienteViewModel findClienteByCodiceFiscale(final String codiceFiscale) {
        return clienteViewModelCreator.createViewModel(
                clienteRepository.findByCfIgnoreCase(codiceFiscale));
    }

    @Override
    @Transactional
    public void deleteEstinzione(final EstinzioneViewModel estinzioneViewModel) {
        estinzioneRepository.delete(estinzioneViewModel.getIdEstinzione());
    }

    @Override
    @Transactional
    public void deleteTrattenuta(final TrattenuteViewModel trattenuteViewModel) {
        trattenuteRepository.delete(trattenuteViewModel.getCodStip());
    }

    @Override
    @Transactional
    public void deleteSecondaOccupazione(final long clienteId) {
        final Cliente cliente = clienteRepository.findOne(clienteId);
        cliente.setImpiego2(null);
        cliente.setQualifica2(null);
        cliente.setDataInizio2(null);
        cliente.setEnte2(null);
        cliente.setCat2(null);
        cliente.setOccupazione2(null);
        cliente.setTelefonoLavoro2(null);
        cliente.setTipoContrattoLavoro2(null);
        cliente.setSecondaOccupazionePredefinita(false);
        cliente.setSecondaOccupazioneRendered(false);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void setAntiriciclaggioFileCaricato(final long praticaId, final boolean antiriciclaggioFileCaricato) {
        final Pratica pratica = praticaRepository.findOne(praticaId);
        pratica.setAntiriciclaggioFileCaricato(antiriciclaggioFileCaricato);
        praticaRepository.save(pratica);
    }

    @Override
    public PraticaViewModel getPraticaById(final long praticaId) {
        return praticaViewModelCreator.createViewModel(praticaRepository.getOne(praticaId));
    }

    @Override
    public List<ClientePraticaFileViewModel> findByCliente_Id(final Long clienteId) {
        return clientePraticaFileViewModelCreator.createViewModelList(clientePraticaFileRepository.findByCliente_Id(clienteId));
    }

    @Override
    public List<ClientePraticaFileViewModel> findByPratica_CodicePratica(final Long codicePratica) {
        return clientePraticaFileViewModelCreator.createViewModelList(clientePraticaFileRepository.findByPratica_CodicePratica(codicePratica));
    }

    public enum PdfCategory {
        ANAGRAFICA, PRIVACY, PRECONTRATTUALE, ALLEGATI, FATTURA;

        public static PdfCategory fromValue(final String value) {
            if (value == null) {
                return null;
            }
            for (final PdfCategory category : PdfCategory.values()) {
                if (category.toString().equalsIgnoreCase(value)) {
                    return category;
                }
            }
            throw new IllegalArgumentException("No constant with antiriciclaggioFileCaricato " + value);
        }
    }

}