package com.woonders.lacemscommon.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.OperatorViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.CalendarioAppuntamento;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.QOperator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.entity.TimeParameters;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.db.repository.RoleRepository;
import com.woonders.lacemscommon.db.repository.TimeParametersRepository;
import com.woonders.lacemscommon.exception.*;
import com.woonders.lacemscommon.network.NetworkUtil;
import com.woonders.lacemscommon.security.CustomSecurityUser;
import com.woonders.lacemscommon.service.FileService;
import com.woonders.lacemscommon.service.LogService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.ResetPasswordService;
import com.woonders.lacemscommon.util.PredicateHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedProperty;

@Service
@Slf4j
@Transactional(readOnly = true)
public class OperatorServiceImpl implements OperatorService {

    public static final String NAME = "operatorServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";
    
    
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
	private OperatorService operatorService;
    
    @Autowired
    private ResetPasswordService resetPasswordService;

    @Autowired
    private OperatorRepository operatorRepository;
    
    @Autowired
    private TimeParametersRepository timeParametersRepository;

    @Autowired
    private NetworkUtil networkUtil;

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OperatorViewModelCreator operatorViewModelCreator;
    @Autowired
    private PredicateHelper predicateHelper;
        
    
    @Override
    @Transactional
    public void changeData(final String nome, final String cognome, final String mail, final String telefono,
                           final long currentOperatorId) throws UnableToUpdateException {
        final Operator operator = operatorRepository.findOne(currentOperatorId);
        operator.setFirstName(nome);
        operator.setLastName(cognome);
        operator.setEmail(mail);
        operator.setPhoneNumber(telefono);
        operatorRepository.save(operator);
    }

    @Override
    public OperatorViewModel findByUsernameViewModel(final String user) {
        return operatorViewModelCreator.createViewModel(operatorRepository.findByUsername(user));
    }

    @Override
    @Transactional
    public void save(final Operator loginOperator) throws UnableToSaveException {
        operatorRepository.save(loginOperator);
    }

    @Transactional
    @Override
    public void save(final OperatorViewModel operatorViewModel) throws UnableToSaveException {
        save(operatorViewModelCreator.createModel(operatorViewModel));
    }

    @Override
    @Transactional
    public void delete(final Operator loginOperator) {
    	log.info("Deleting loginOperator:" + loginOperator.toString());
    	try { 
    		final Operator operator = operatorRepository.findOne(loginOperator.getId());
    		if (operator.getUsername() != null) {
    			log.info("Eliminato operatore: " + operator.getUsername());
            }
    		operatorRepository.delete(operator); 
    	} catch (final DataAccessException e) {
    		log.error("Unable to delete loginOperator", e); 
    	} catch (final Exception e) {
    		log.error("Unable to delete loginOperator", e); 
    	}
    }
    

	@Override
	public void delete(OperatorViewModel operatorViewModel) throws UnableToDeleteException {
		this.delete(operatorViewModelCreator.createModel(operatorViewModel));
	}

    @Override
    public List<String> mailList() {
        return findAppOperatorListByAziendaId(null).stream().map(OperatorViewModel::getEmail).collect(Collectors.toList());
    }

    @Override
    public List<OperatorViewModel> findAppOperatorListByAziendaId(final Long aziendaId) {
        final Collection<RoleName> roleToAvoidCollection = new LinkedList<>();
        roleToAvoidCollection.add(Role.RoleName.ANTI_RICICLAGGIO_OUT);

        final QOperator qOperator = QOperator.operator;
        BooleanExpression booleanExpression = qOperator.role.any().roleName.notIn(roleToAvoidCollection);

        if (aziendaId != null) {
            booleanExpression = booleanExpression.and(qOperator.azienda.id.eq(aziendaId));
        }

        return operatorViewModelCreator.createViewModelList(operatorRepository.findAll(booleanExpression));
    }

    @Override
    public List<OperatorViewModel> findAllAppOperator() {
        //come findAppOperatorListByAziendaId ma senza aziendaId, da usare SOLO nel cerca!
        //NON e vero lo usiamo anche quando hai i permessi SUPER perche devi vedere tutti gli operatori, cazz!
        final Collection<RoleName> roleToAvoidCollection = new LinkedList<>();
        roleToAvoidCollection.add(Role.RoleName.ANTI_RICICLAGGIO_OUT);

        final QOperator qOperator = QOperator.operator;
        final BooleanExpression booleanExpression = qOperator.role.any().roleName.notIn(roleToAvoidCollection);

        return operatorViewModelCreator.createViewModelList(operatorRepository.findAll(booleanExpression));
    }

    @Override
    public String mail(final String user) {
        return operatorRepository.findEmailByUsername(user);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final String ip = networkUtil.getRequestIp();
        if (loginAttemptService.isBlocked(ip)) {
            throw new BlockedIpException();
        }
        final Operator operator = operatorRepository.findByUsername(username);
        if (operator == null) {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return new CustomSecurityUser(operator.getUsername(), operator.getPassword(), getAuthorities(operator),
                operator.getId(), false, false, operator.getAzienda().getId());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Operator operator) {
        final Set<GrantedAuthority> authorities = operator.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().toString())).collect(Collectors.toSet());
        log.info("user authorities are " + authorities.toString());
        return authorities;
    }

    @Override
    @Transactional
    public void changePassword(final String oldPassword, final String newPassword, final String confirmNewPassword,
                               final long currentOperatorId)
            throws CannotChangePasswordException, InvalidCurrentPasswordException, ConfirmPasswordMismatchException {
        if (newPassword.equals(confirmNewPassword)) {
            final Operator operator = operatorRepository.findOne(currentOperatorId);
            if (passwordEncoder.matches(oldPassword, operator.getPassword())) {
                operator.setPassword(passwordEncoder.encode(newPassword));
                operatorRepository.save(operator);
                
                /*
                 * Recupero il numero di giorni della scadenza password dalla tabella time_parameters
                 */
                List<TimeParameters> listTimeParameters = timeParametersRepository.findAll();
                TimeParameters timeParameters = null;
                for (TimeParameters tp : listTimeParameters) {
                    if (tp.getCodeIdentify().equals("SP")) {
                    	timeParameters = tp;
                    }
                }
                //TimeParameters timeParameters = from(listTimeParameters).where("codeIdentity", eq("SP")).first();
                
                final Calendar now = Calendar.getInstance();
        		Date todaysDate = now.getTime();
        		
        		int timeChangePsw = (timeParameters == null) ? 30 : Integer.parseInt(timeParameters.getValueParameter());

        		resetPasswordService.saveResetPassword(operator, todaysDate, timeChangePsw, false);
//                resetPasswordService.saveResetPassword(operator, todaysDate, Integer.parseInt(timeParameters.getValueParameter()), false);
                
            } else {
                throw new InvalidCurrentPasswordException();
            }
        } else {
            throw new ConfirmPasswordMismatchException("Nuova password e conferma password devono essere uguali!");
        }
    }
    
    @Override
    @Transactional
    public String newPasswordEncoded(final String newPassword, final String confirmNewPassword)
            throws CannotChangePasswordException, InvalidCurrentPasswordException, ConfirmPasswordMismatchException {
        if (newPassword.equals(confirmNewPassword)) {
            return passwordEncoder.encode(newPassword);
        } else {
            throw new ConfirmPasswordMismatchException("Nuova password e conferma password devono essere uguali!");
        }
    }

    @Override
    public List<OperatorViewModel> findByRoleName(final Role.RoleName roleName) {
        return operatorViewModelCreator.createViewModelList(operatorRepository.findByRole_RoleName(roleName));
    }

    @Override
    public List<OperatorViewModel> findByRoleNameInAndAzienda_Id(final Collection<RoleName> roleNameCollection, final Long aziendaId) {
        return operatorViewModelCreator
                .createViewModelList(operatorRepository.findAll(predicateHelper.getPredicateForFindOperatorByRoleNameInAndAzienda_Id(roleNameCollection, aziendaId)));
    }

    @Override
    public OperatorViewModel findOperatorViewModelByUsername(final String user) throws UnableToFindException {
        return operatorViewModelCreator.createViewModel(operatorRepository.findByUsername(user));
    }

    @Override
    public OperatorViewModel getOne(final long id) {
        return operatorViewModelCreator.createViewModel(operatorRepository.findOne(id));
    }

    @Override
    public ViewModelPage<OperatorViewModel> findAll(final int firstElementIndex, final int pageSize, final String sortField,
                                                    final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters, final long currentOperatorId,
                                                    final boolean isGestionePermessiWrite, final Long currentAziendaId, final boolean isGestionePermessiWriteSuper) {

        final QSort operatorUsernameAscSort = new QSort(QOperator.operator.username.asc());

        final Page<Operator> operatorPage = operatorRepository.findAll(
                predicateHelper.getPredicateForOperatorByUsernameAndPermessiWrite(currentOperatorId, isGestionePermessiWrite, currentAziendaId, isGestionePermessiWriteSuper),
                new PageRequest(firstElementIndex / pageSize, pageSize, operatorUsernameAscSort));

        return new ViewModelPageImpl<>(operatorViewModelCreator.createViewModelList(operatorPage.getContent()),
                operatorPage.getTotalPages(), operatorPage.getTotalElements());

    }

    @Override
    public List<OperatorViewModel> getAllReceiveLeadEnabledOperatorByAziendaId(final long aziendaId) {
        return operatorViewModelCreator.createViewModelList(operatorRepository.findByReceiveLeadEnabledTrueAndAzienda_Id(aziendaId));
    }

    @Override
    @Transactional
    public void updateReceiveLeadEnabledOperatorsByAziendaId(final List<Long> selectedReceiveLeadOperatorIdsList, final long aziendaId) {
        //aggiorno le preferenze dei lead solo se le ho modificate, altrimenti si resetterebbe ogni volta cambiando qualsiasi altra preferenza
        final List<Long> operatorsIdThatCanReceiveLeads = operatorRepository.findByReceiveLeadEnabledTrueAndAzienda_Id(aziendaId).stream().map(Operator::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEqualCollection(operatorsIdThatCanReceiveLeads, selectedReceiveLeadOperatorIdsList)) {
            for (final Operator operator : operatorRepository.findByAzienda_Id(aziendaId)) {
                operator.setReceiveLeadEnabled(selectedReceiveLeadOperatorIdsList.contains(operator.getId()));
                //devo resettare il counter, probabilmente qualche operatore potrebbe ricevere nuovamente dei lead mentre altri no, quindi si salta un giro
                //ma nel caso in cui non resettassimo, se un operatore A arriva a 500 lead ricevuti, poi abilitiamo alla ricezione un operatore B,
                //l operatore B dovra ricevere 500 lead prima che ne venga assegnato di nuovo uno ad A, quindi preferisco resettare ogni volta che si cambiano le preferenze
                operator.setLeadRicevuti(0);
                operatorRepository.save(operator);
            }
        }
    }


}
