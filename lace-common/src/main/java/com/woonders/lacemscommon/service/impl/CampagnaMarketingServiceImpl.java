package com.woonders.lacemscommon.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;
import com.woonders.lacemscommon.app.event.SendCampagnaDTO;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.CampagnaSmsViewModel;
import com.woonders.lacemscommon.app.viewmodel.CampagnaViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.CampagnaSmsViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.CampagnaViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.config.async.AsyncConfig;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;
import com.woonders.lacemscommon.db.entityenum.Provenienza;
import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.entityutil.SettingUtil;
import com.woonders.lacemscommon.db.repository.CampagnaRepository;
import com.woonders.lacemscommon.db.repository.CampagnaSmsRepository;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.exception.NotEnoughCreditException;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.CampagnaMarketingService;
import com.woonders.lacemscommon.service.SettingService;
import com.woonders.lacemscommon.sms.SmsSenderExecutor;
import com.woonders.lacemscommon.util.PredicateHelper;
import com.woonders.lacemscommon.util.RequestUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 29/03/2017.
 */
@Slf4j
@Service
@Transactional
public class CampagnaMarketingServiceImpl implements CampagnaMarketingService {

	public static final String NAME = "campagnaMarketingServiceImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@Autowired
	private PredicateHelper predicateHelper;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private ClienteViewModelCreator clienteViewModelCreator;
	@Autowired
	private CampagnaRepository campagnaRepository;
	@Autowired
	private CampagnaSmsRepository campagnaSmsRepository;
	@Autowired
	private CampagnaViewModelCreator campagnaViewModelCreator;
	@Autowired
	private CampagnaSmsViewModelCreator campagnaSmsViewModelCreator;
	@Autowired
	private OperatorRepository operatorRepository;
	@Autowired
	private QueryDSLHelper queryDSLHelper;
	@Autowired
	private RequestUtil requestUtil;
	@Autowired
	private SmsSenderExecutor smsSenderExecutor;
	@Autowired
	private SettingUtil settingUtil;
	@Autowired
	private SettingService settingService;
	@Autowired
	private AziendaService aziendaService;

	@Transactional(rollbackFor = NotEnoughCreditException.class)
	@Override
	public CampagnaViewModel save(String nameCampagna, String descriptionCampagna, String messageToSend,
			Campagna.CampagnaType campagnaType, boolean campagnaImport, long idCreator,
			FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel,
			List<ClienteViewModel> clienteViewModelList, int totaleMessaggi, boolean isClientiReadSuper,
			boolean isNominativiReadSuper, boolean isClientiReadAll, boolean isNominativiReadAll,
			boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorAziendaId, Long currentAziendaId)
			throws NotEnoughCreditException {

		int numeroDestinatari;
		Iterable<Cliente> clienteIterable = null;
		// prendo lista clienti dal filtro
		if (filtroCampagnaMarketingViewModel != null) {
			clienteIterable = clienteRepository.findAll(predicateHelper.getPredicateForNuovaCampagnaMarketing(
					filtroCampagnaMarketingViewModel, isClientiReadSuper, isNominativiReadSuper, isClientiReadAll,
					isNominativiReadAll, isClientiReadOwn, isNominativiReadOwn, idCreator, currentOperatorAziendaId,
					currentAziendaId));
			numeroDestinatari = Iterables.size(clienteIterable);
		} else {
			numeroDestinatari = clienteViewModelList.size();
		}

		Operator senderOperator = operatorRepository.findOne(idCreator);

		if (senderOperator.getSmsBalance() < totaleMessaggi) {
			throw new NotEnoughCreditException();
		}

		// salvo la campagna
		Operator creatorOperator = operatorRepository.findOne(idCreator);
		Campagna campagna = Campagna.builder().name(nameCampagna).description(descriptionCampagna).text(messageToSend)
				.campagnaType(campagnaType).campagnaImport(campagnaImport).creationDateTime(LocalDateTime.now())
				.creatorOperator(creatorOperator).totaleDestinatari(numeroDestinatari).totaleMessaggi(totaleMessaggi)
				.build();
		campagna = campagnaRepository.save(campagna);

		// salvo tutti i messaggi della campagna con esito in spedizione
		if (filtroCampagnaMarketingViewModel != null) {
			for (Cliente cliente : clienteIterable) {
				campagnaSmsRepository.save(
						CampagnaSms.builder().campagna(campagna).cliente(cliente).nomeDestinatario(cliente.getNome())
								.cognomeDestinatario(cliente.getCognome()).numeroDestinatario(cliente.getTelefono())
								.dataEsito(LocalDateTime.now()).esito(EsitoSmsNotificaLead.IN_SPEDIZIONE).build());
			}
		} else {
			for (Cliente cliente : clienteViewModelCreator.createModelList(clienteViewModelList)) {
				campagnaSmsRepository.save(CampagnaSms.builder().campagna(campagna).nomeDestinatario(cliente.getNome())
						.cognomeDestinatario(cliente.getCognome()).numeroDestinatario(cliente.getTelefono())
						.dataEsito(LocalDateTime.now()).esito(EsitoSmsNotificaLead.IN_SPEDIZIONE).build());
			}
		}

		return campagnaViewModelCreator.createViewModel(campagna);
	}

	@Override
	public void startSmsSendingCampagna(long id, long idSender, long currentAziendaId) {
		// TODO utilizzare OperatorService e non OperatorRepository una volta
		// che abbiamo spostato OperatorService su common
		Operator senderOperator = operatorRepository.findOne(idSender);
		SendCampagnaDTO sendCampagnaDTO = SendCampagnaDTO.builder().campagnaId(id)
				.tenantName(requestUtil.getTenantName())
				.numeroMittente(settingService.getByAziendaId(currentAziendaId).getAlias())
				.idOperator(senderOperator.getId()).currentAziendaId(currentAziendaId).build();
		smsSenderExecutor.startSmsSending(sendCampagnaDTO);
	}

	@Transactional(rollbackFor = NotEnoughCreditException.class)
	@Override
	public void writeCampagnaInfoSent(long id, long idSender) throws NotEnoughCreditException {
		Operator senderOperator = operatorRepository.findOne(idSender);
		Campagna campagna = campagnaRepository.findOne(id);
		if (senderOperator.getSmsBalance() < campagna.getTotaleMessaggi()) {
			throw new NotEnoughCreditException();
		}
		operatorRepository.save(senderOperator);
		campagna.setSenderOperator(senderOperator);
		campagna.setSentDateTime(LocalDateTime.now());
		campagnaRepository.save(campagna);
	}

	@Transactional(rollbackFor = NotEnoughCreditException.class)
	@Override
	public CampagnaViewModel saveAndSendCampagna(String nameCampagna, String descriptionCampagna, String messageToSend,
			Campagna.CampagnaType campagnaType, boolean campagnaImport, long idCreator,
			FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel,
			List<ClienteViewModel> clienteViewModelList, int totaleMessaggi, boolean isClientiReadSuper,
			boolean isNominativiReadSuper, boolean isClientiReadAll, boolean isNominativiReadAll,
			boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorAziendaId, Long currentAziendaId)
			throws NotEnoughCreditException {
		CampagnaViewModel campagnaViewModel = save(nameCampagna, descriptionCampagna, messageToSend, campagnaType,
				campagnaImport, idCreator, filtroCampagnaMarketingViewModel, clienteViewModelList, totaleMessaggi,
				isClientiReadSuper, isNominativiReadSuper, isClientiReadAll, isNominativiReadAll, isClientiReadOwn,
				isNominativiReadOwn, currentOperatorAziendaId, currentAziendaId);
		writeCampagnaInfoSent(campagnaViewModel.getId(), idCreator);
		return campagnaViewModel;
	}

	@Override
	public ViewModelPage<CampagnaViewModel> getCampagnaList(Long currentAziendaId, int firstElementIndex, int pageSize,
			String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters) {
		// filters non usati ora nella tabella!

		// per ora ordina sempre in base alla data di creazione
		QSort dataCreazioneDescSort = new QSort(QCampagna.campagna.creationDateTime.desc());
		Page<Campagna> campagnaPage;
		if (currentAziendaId != null) {
			campagnaPage = campagnaRepository.findAll(
					QCampagna.campagna.creatorOperator.azienda.id.eq(currentAziendaId),
					new PageRequest(firstElementIndex / pageSize, pageSize, dataCreazioneDescSort));
		} else {
			campagnaPage = campagnaRepository
					.findAll(new PageRequest(firstElementIndex / pageSize, pageSize, dataCreazioneDescSort));
		}

		return new ViewModelPageImpl<>(campagnaViewModelCreator.createViewModelList(campagnaPage.getContent()),
				campagnaPage.getTotalPages(), campagnaPage.getTotalElements());
	}

	@Override
	public ViewModelPage<CampagnaSmsViewModel> getCampagnaSmsList(long idCampagna, int firstElementIndex, int pageSize,
			String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters) {

		QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

		Page<CampagnaSms> campagnaSmsPage = campagnaSmsRepository.findAll(
				predicateHelper.getPredicateForDettaglioCampagna(idCampagna)
						.and(queryDSLHelper.createFilterPredicate(filters)),
				new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

		return new ViewModelPageImpl<>(campagnaSmsViewModelCreator.createViewModelList(campagnaSmsPage.getContent()),
				campagnaSmsPage.getTotalPages(), campagnaSmsPage.getTotalElements());
	}

	@PreAuthorize("hasAnyAuthority('CLIENTI_READ_OWN', 'CLIENTI_READ_ALL', 'CLIENTI_READ_SUPER', 'NOMINATIVI_READ_OWN', 'NOMINATIVI_READ_ALL',"
			+ "'NOMINATIVI_READ_SUPER')")
	@Override
	public ViewModelPage<ClienteViewModel> getNuovaCampagnaResultList(int firstElementIndex, int pageSize,
			FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel, boolean isClientiReadSuper,
			boolean isNominativiReadSuper, boolean isClientiReadAll, boolean isNominativiReadAll,
			boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorId,
			long currentOperatorAziendaId, Long currentAziendaId) {

		// FIX simile a searchServiceImpl completeClientiNominativi
		// se non posso leggere niente, O, se non posso leggere quello che sto
		// cercando!!

		// AGGIUNTO il preauthorize ma da migliorare, vorrei rimuovere questo if
		// qui!!
		Tipo tipo = filtroCampagnaMarketingViewModel.getTipo();
		if ((!isClientiReadSuper && !isClientiReadAll && !isClientiReadOwn && !isNominativiReadSuper
				&& !isNominativiReadAll && !isNominativiReadOwn)
				|| (Tipo.CLIENTE.equals(tipo) && !isClientiReadSuper && !isClientiReadAll && !isClientiReadOwn)
				|| (Tipo.NOMINATIVO.equals(tipo) && !isNominativiReadSuper && !isNominativiReadAll
						&& !isNominativiReadOwn)) {
			// TODO sarebbe meglio lanciare eccezione, come in tutti gli altri
			// casi dove facciamo questa cosa, perche ad esempio qui sembra che
			// puoi vedere la categoria ma che non hai risultati, mentre invece
			// dovrebbe farti notare che non hai proprio accesso alla categoria
			// ricercata
			return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
		} else {
			Page<Cliente> clientePage = clienteRepository
					.findAll(
							predicateHelper.getPredicateForNuovaCampagnaMarketing(filtroCampagnaMarketingViewModel,
									isClientiReadSuper, isNominativiReadSuper, isClientiReadAll, isNominativiReadAll,
									isClientiReadOwn, isNominativiReadOwn, currentOperatorId, currentOperatorAziendaId,
									currentAziendaId),
							new PageRequest(firstElementIndex / pageSize, pageSize));

			return new ViewModelPageImpl<>(clienteViewModelCreator.createViewModelList(clientePage.getContent()),
					clientePage.getTotalPages(), clientePage.getTotalElements());
		}
	}

	@Override
	public ClienteViewModel getCliente(long id) {
		Cliente cliente = clienteRepository.findOne(id);
		return clienteViewModelCreator.createViewModel(cliente);
	}

	@Override
	public CampagnaViewModel findCampagna(long id) {
		return campagnaViewModelCreator.createViewModel(campagnaRepository.findOne(id));
	}

	@Override
	public CampagnaSmsViewModel findCampagnaSms(long id) {
		return campagnaSmsViewModelCreator.createViewModel(campagnaSmsRepository.findOne(id));
	}

	@Override
	public long countByEsitoCampagna(long idCampagna, EsitoSmsNotificaLead esito) {
		return campagnaSmsRepository.count(predicateHelper.getPredicateForEsitoCampagna(idCampagna, esito));
	}

	@Override
	public long countByEsitoCampagnaAndTipo(long idCampagna, EsitoSmsNotificaLead esito, Tipo tipo) {
		return campagnaSmsRepository
				.count(predicateHelper.getPredicateForEsitoCampagnaAndTipo(idCampagna, esito, tipo));
	}

	@Async(AsyncConfig.SINGLE_TASK_EXECUTOR)
	@Transactional
	@Override
	public void importaNominativi(long idCampagna) {
		// imposta campagna come importata
		Campagna campagna = campagnaRepository.findOne(idCampagna);
		campagnaRepository.save(campagna);
		Operator operator = campagna.getCreatorOperator();
		Date dataInserimento = new Date();

		// importa i nominativi
		Iterable<CampagnaSms> campagnaSmsList = campagnaSmsRepository
				.findAll(predicateHelper.getPredicateForCampagnaSmsNonClienti(idCampagna));
		log.info("Nominativi da importare " + Iterables.size(campagnaSmsList));
		Cliente cliente;
		for (CampagnaSms campagnaSms : campagnaSmsList) {
			long count = clienteRepository.countByCognomeAndNomeAndTelefono(campagnaSms.getCognomeDestinatario(),
					campagnaSms.getNomeDestinatario(), campagnaSms.getNumeroDestinatario());
			if (count == 0) {
				cliente = Cliente.builder().nome(campagnaSms.getNomeDestinatario())
						.cognome(campagnaSms.getCognomeDestinatario()).telefono(campagnaSms.getNumeroDestinatario())
						.tipo(Tipo.NOMINATIVO.getValue()).provenienza(Provenienza.CAMPAGNA_DA_FILE.getValue())
						.statoNominativo(StatoNominativo.DA_LAVORARE.getValue()).operatoreNominativo(operator)
						.dataIns(dataInserimento).build();
				cliente = clienteRepository.save(cliente);
				campagnaSms.setCliente(cliente);
				campagnaSmsRepository.save(campagnaSms);
			} else {
				List<Cliente> clienteList = clienteRepository.findByCognomeAndNomeAndTelefono(
						campagnaSms.getCognomeDestinatario(), campagnaSms.getNomeDestinatario(),
						campagnaSms.getNumeroDestinatario());
				campagnaSms.setCliente(clienteList.get(0));
				campagnaSmsRepository.save(campagnaSms);
			}
		}
		log.info("Importati tutti i nominativi della campagna " + idCampagna);
	}

	@Override
	public long countByListEsitoCampagnaSms(long idCampagna) {
		List<EsitoSmsNotificaLead> esitoList = new ArrayList<>();
		esitoList.add(EsitoSmsNotificaLead.DA_SPEDIRE);
		esitoList.add(EsitoSmsNotificaLead.NON_SPEDITO);
		return campagnaSmsRepository.count(predicateHelper.getPredicateForListEsitoCampagnaSms(idCampagna, esitoList));
	}

}
