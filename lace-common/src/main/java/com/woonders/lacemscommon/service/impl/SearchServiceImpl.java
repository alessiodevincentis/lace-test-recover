package com.woonders.lacemscommon.service.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.AdvancedSearch;
import com.woonders.lacemscommon.app.model.AdvancedSearchViewModel;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.model.util.ClientePraticaUtil;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.AmministrazioneViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.PraticaViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.exception.PermissionDeniedException;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.SearchService;
import com.woonders.lacemscommon.util.DateConversionUtil;
import com.woonders.lacemscommon.util.PredicateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    public static final String NAME = "searchServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private PredicateHelper predicateHelper;
    @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private ClientePraticaUtil clientePraticaUtil;
    @Autowired
    private AmministrazioneViewModelCreator amministrazioneViewModelCreator;
    @Autowired
    private DateConversionUtil dateConversionUtil;
    @Autowired
    private OperatorService operatorService;

    @Override
    public List<ClientePratica> searchByCodiceFiscale(final String codiceFiscale) {

        final List<Cliente> clienteList = new ArrayList<>();
        final Cliente cliente = clienteRepository.findDistinctByCf(codiceFiscale);

        // non puoi fare add del risultato della query qui sopra direttamente
        // nella lista, se torna null scoppia tutto perche mette null nella
        // lista!!
        if (cliente != null) {
            clienteList.add(cliente);
        }

        return setList(clienteList);
    }

    @Override
    public List<ClientePratica> searchByCognome(final String cognome) {
        return setList(clienteRepository.findDistinctByCognome(cognome));
    }

    @Override
    public List<ClientePratica> findByTelefono(final String telefono) {
        return setList(clienteRepository.findDistinctByTelefonoContainingOrTelefonoFissoContaining(telefono, telefono));
    }

    @Override
    public List<ClientePratica> findByNrContratto(final String nrContratto) {
        return clientePraticaUtil.generateClientePraticaList(praticaRepository.findByNrctrEquals(nrContratto));
    }


    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#aziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    @Override
    public ViewModelPageImpl<ClientePratica> advancedSearchClienti(final long operatorId,
                                                                   final Role.RoleName clientiReadRoleName,
                                                                   final long aziendaId,
                                                                   final AdvancedSearchViewModel advancedSearchClientiViewModel,
                                                                   final int firstElementIndex, final int pageSize,
                                                                   final String sortField,
                                                                   final QueryDSLHelper.SortOrder sortOrder) {

        if (advancedSearchClientiViewModel != null) {

            try {
                final AdvancedSearch advancedSearchClienti = advancedSearchCreator(advancedSearchClientiViewModel);
                final Page<Pratica> praticaPage;
                final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

                final BooleanExpression predicate = predicateHelper.getPredicateForAdvancedSearchCliente(
                        operatorId, clientiReadRoleName, aziendaId, advancedSearchClienti);

                praticaPage = praticaRepository.findAll(predicate,
                        new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

                return new ViewModelPageImpl<>(clientePraticaUtil.generateClientePraticaList(praticaPage.getContent()),
                        praticaPage.getTotalPages(), praticaPage.getTotalElements());

            } catch (final PermissionDeniedException e) {
                return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
            }
        }
        return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
    }

    @PreAuthorize("hasAuthority('NOMINATIVI_READ_SUPER') or (hasAuthority('NOMINATIVI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#aziendaId))"
            + " or hasAuthority('NOMINATIVI_READ_OWN')")
    @Override
    public ViewModelPageImpl<ClienteViewModel> advancedSearchNominativi(final long operatorId,
                                                                        final Role.RoleName nominativiReadRoleName,
                                                                        final long aziendaId,
                                                                        final AdvancedSearchViewModel advancedSearchNominativiViewModel,
                                                                        final int firstElementIndex, final int pageSize,
                                                                        final String sortField,
                                                                        final QueryDSLHelper.SortOrder sortOrder,
                                                                        final Map<QueryDSLHelper.TableField, Object> filters) {

        if (advancedSearchNominativiViewModel != null) {

            try {
                final AdvancedSearch advancedSearchNominativi = advancedSearchCreator(advancedSearchNominativiViewModel);
                final Page<Cliente> clientePage;
                final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

                final BooleanExpression predicate = predicateHelper.getPredicateForAdvancedSearchNominativo(
                        operatorId, nominativiReadRoleName, aziendaId, advancedSearchNominativi);

                clientePage = clienteRepository.findAll(predicate.and(queryDSLHelper.createFilterPredicate(filters)),
                        new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

                return new ViewModelPageImpl<>(clienteViewModelCreator.createViewModelList(clientePage.getContent()),
                        clientePage.getTotalPages(), clientePage.getTotalElements());

            } catch (final PermissionDeniedException e) {
                return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
            }
        }
        return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
    }


    private AdvancedSearch advancedSearchCreator(final AdvancedSearchViewModel advancedSearch) {

        return AdvancedSearch.builder().amministrazione(amministrazioneViewModelCreator.createModel(
                advancedSearch.getAmministrazioneViewModel()))
                .aziendaId(advancedSearch.getAziendaId())
                .dataAssunzione(dateConversionUtil.fromYearsToDate(advancedSearch.getAnniAssunzione()))
                .dataInizioCaricamento(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataInizioCaricamento()))
                .dataFineCaricamento(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataFineCaricamento()))
                .dataInizioInserimento(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataInizioInserimento()))
                .dataFineInserimento(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataFineInserimento()))
                .dataNascitaInizio(dateConversionUtil.fromYearsToDate(advancedSearch.getEtaTo()))
                .dataNascitaFine(dateConversionUtil.fromYearsToDate(advancedSearch.getEtaFrom()))
                .dataRinnovoInizio(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataRinnovoInizio()))
                .dataRinnovoFine(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataRinnovoFine()))
                .impieghiList(advancedSearch.getImpieghiList())
                .operatorList(advancedSearch.getOperatorList())
                .provenienzaList(advancedSearch.getProvenienzaList())
                .provenienzaDescList(advancedSearch.getProvenienzaDescList())
                .provinciaResidenza(advancedSearch.getProvinciaResidenza())
                .sesso(advancedSearch.getSesso())
                .statoPraticaList(advancedSearch.getStatoPraticaList())
                .statoNominativoList(advancedSearch.getStatoNominativoList())
                .tipoRinnovo(advancedSearch.getTipoRinnovo())
                .dataRecallInizio(dateConversionUtil.calcDateFromLocalDateTime(advancedSearch.getDataRecallInizio()))
                .dataRecallFine(dateConversionUtil.calcDateFromLocalDateTime(advancedSearch.getDataRecallFine()))
                .dataIstruttoriaInizio(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataIstruttoriaInizio()))
                .dataIstruttoriaFine(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataIstruttoriaFine()))
                .dataIstruitaInizio(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataIstruitaInizio()))
                .dataIstruitaFine(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataIstruitaFine()))
                .dataDeliberaInizio(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataDeliberaInizio()))
                .dataDeliberaFine(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataDeliberaFine()))
                .dataFirmaContrattiInizio(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataFirmaContrattiInizio()))
                .dataFirmaContrattiFine(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataFirmaContrattiFine()))
                .dataLiquidazioneInizio(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataLiquidazioneInizio()))
                .dataLiquidazioneFine(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataLiquidazioneFine()))
                .dataDecorrenzaInizio(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataDecorrenzaInizio()))
                .dataDecorrenzaFine(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataDecorrenzaFine()))
                .dataPerfezionamentoInizio(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataPerfezionamentoInizio()))
                .dataPerfezionamentoFine(dateConversionUtil.calcDateFromLocalDate(advancedSearch.getDataPerfezionamentoFine())).build();
    }


    @Override
    public ClienteViewModel getSelectedClientePratica(final ClientePratica selectedClientePratica) {
        return clienteViewModelCreator
                .createViewModel(clienteRepository.findOne(selectedClientePratica.getClienteViewModel().getId()));
    }

    private List<ClientePratica> setList(final List<Cliente> listc) {

        final List<ClientePratica> listcp = new ArrayList<>();

        if (!listc.isEmpty()) {
            for (final Cliente c : listc) {
                if (Tipo.NOMINATIVO.getValue().equalsIgnoreCase(c.getTipo())) {
                    setFieldListNominativo(listcp, c);
                } else {
                    for (final Pratica p : c.getPratica()) {
                        setFieldListCliente(listcp, c, p);
                    }
                }
            }
        }
        return listcp;
    }

    private void setFieldListCliente(final List<ClientePratica> listcp, final Cliente cliente, final Pratica pratica) {
        final ClientePratica cp = new ClientePratica();
        cp.setClienteViewModel(clienteViewModelCreator.createViewModel(cliente));
        cp.setPraticaViewModel(praticaViewModelCreator.createViewModel(pratica));
        listcp.add(cp);
    }

    private void setFieldListNominativo(final List<ClientePratica> listcp, final Cliente cliente) {
        final ClientePratica cp = new ClientePratica();
        cp.setClienteViewModel(clienteViewModelCreator.createViewModel(cliente));
        listcp.add(cp);
    }

    @Override
    public List<ClientePratica> filterListByCliente(final List<ClientePratica> listClienteNominativo) {
        final List<ClientePratica> listCliente = new ArrayList<>();
        for (final ClientePratica clienteNominativo : listClienteNominativo) {
            if (Tipo.CLIENTE.getValue().equalsIgnoreCase(clienteNominativo.getClienteViewModel().getTipo()))
                listCliente.add(clienteNominativo);
        }
        return listCliente;
    }

    @Override
    public List<ClienteViewModel> completeClientiNominativi(final String query, final Role.RoleName clientiReadRoleName, final Role.RoleName nominativiReadRoleName, final long operatorId, final long aziendaId) {
        if (Role.RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName) || Role.RoleName.CLIENTI_READ_ALL.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_SUPER.equals(clientiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName) || Role.RoleName.NOMINATIVI_READ_ALL.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_SUPER.equals(nominativiReadRoleName)) {

            final Iterable<Cliente> clienteIterable = clienteRepository.findAll(predicateHelper.getPredicateForFindByCognomeContainingAndUsername(query,
                    clientiReadRoleName,
                    nominativiReadRoleName, operatorId, aziendaId, Tipo.CLIENTE));

            final Iterable<Cliente> nominativoIterable = clienteRepository.findAll(predicateHelper.getPredicateForFindByCognomeContainingAndUsername(query,
                    clientiReadRoleName,
                    nominativiReadRoleName, operatorId, aziendaId, Tipo.NOMINATIVO));

            final List<ClienteViewModel> resultList = new LinkedList<>();
            resultList.addAll(clienteViewModelCreator.createViewModelList(Lists.newArrayList(clienteIterable)));
            resultList.addAll(clienteViewModelCreator.createViewModelList(Lists.newArrayList(nominativoIterable)));

            return resultList;
        }

        return null;
    }

    @Override
    public ClienteViewModel findOne(final long id) throws ItemNotFoundException {
        final Cliente cliente = clienteRepository.findOne(id);
        if (cliente != null) {
            return clienteViewModelCreator.createViewModel(clienteRepository.findOne(id));
        } else {
            throw new ItemNotFoundException();
        }
    }

    @Override
    public ClientePratica getPratica(final long id) {
        final Pratica pratica = praticaRepository.findOne(id);
        return ClientePratica.builder().clienteViewModel(clienteViewModelCreator.createViewModel(pratica.getCliente()))
                .praticaViewModel(praticaViewModelCreator.createViewModel(pratica)).build();
    }

    @Override
    public List<String> getOperatorList(final Role.RoleName roleName, final long aziendaId) {
        switch (roleName) {
            case NOMINATIVI_READ_ALL:
            case CLIENTI_READ_ALL:
                return operatorService.findAppOperatorListByAziendaId(aziendaId).stream()
                        .map(OperatorViewModel::getUsername)
                        .collect(Collectors.toList());
            case NOMINATIVI_READ_SUPER:
            case CLIENTI_READ_SUPER:
                return operatorService.findAllAppOperator().stream()
                        .map(OperatorViewModel::getUsername)
                        .collect(Collectors.toList());
        }
        return new LinkedList<>();
    }

	@Override
	public List<String> getProvenienzaDescList() {
		final List<String> provenienzaDescList = clienteRepository.getDistinctProvenienzaDesc();

        return provenienzaDescList;
	}
}