package com.woonders.lacemscommon.service.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.PraticaViewModelCreator;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.QPratica;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.exception.CannotDeletePraticaException;
import com.woonders.lacemscommon.service.DeleteClienteService;
import com.woonders.lacemscommon.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DeleteClienteServiceImpl implements DeleteClienteService {

    public static final String NAME = "deleteClienteServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    @Qualifier(AmazonS3FileServiceImpl.NAME)
    private FileService fileService;

    //TODO da pensare bene. se posso eliminare il cliente, come faccio a sapere che non sia usato da un-altra agenzia??
    @PreAuthorize("hasAuthority('CLIENTI_DELETE_SUPER') or hasAuthority('CLIENTI_DELETE_ALL')")
    @Override
    @Transactional
    public void deleteByCf(String tenantId, String cf) {
        //first delete files
        Cliente cliente = clienteRepository.findByCfIgnoreCase(cf);
        fileService.deleteAllFilesInCategory(tenantId, cliente.getId(), FileService.FileCategory.ANAGRAFICA);

        //then find cliente, find pratiche and delete pratiche
        BooleanExpression allPraticheByClienteId = QPratica.pratica.cliente.id.eq(cliente.getId());

        List<Pratica> praticaList = Lists.newArrayList(praticaRepository.findAll(allPraticheByClienteId));

        for (Pratica pratica : praticaList) {
            deleteFilesForPratica(tenantId, pratica.getCodicePratica());
        }

        //in the end delete cliente
        clienteRepository.deleteByCfIgnoreCaseAndTipoIgnoreCase(cf, Tipo.CLIENTE.getValue());

    }

    private void deleteFilesForPratica(String tenantId, long praticaId) {
        List<FileService.FileCategory> fileCategoryList = Arrays.asList(FileService.FileCategory.ANTIRICICLAGGIO, FileService.FileCategory.PRATICA, FileService.FileCategory.REDDITO);
        for (FileService.FileCategory fileCategory : fileCategoryList) {
            fileService.deleteAllFilesInCategory(tenantId, praticaId, fileCategory);
        }
    }

    @Override
    public ClienteViewModel findByCf(String cf) {
        return clienteViewModelCreator
                .createViewModel(clienteRepository.findByCfIgnoreCaseAndTipoIgnoreCase(cf, Tipo.CLIENTE.getValue()));
    }

    @Override
    public List<ClientePratica> findPraticheToDeleteByCf(String cf, Role.RoleName clientiDeleteRoleName, long operatorId) throws CannotDeletePraticaException {
        List<Cliente> listClienti = null;

        if (Role.RoleName.CLIENTI_DELETE_ALL.equals(clientiDeleteRoleName)) {
            listClienti = clienteRepository.findDistinctByCfAndTipo(cf, Tipo.CLIENTE.getValue());
        } else {
            if (Role.RoleName.CLIENTI_DELETE_OWN.equals(clientiDeleteRoleName)) {
                listClienti = clienteRepository.findDistinctByCfAndTipoAndPratica_Operatore_Id(cf,
                        Tipo.CLIENTE.getValue(), operatorId);
            }
        }

        List<ClientePratica> listPraticheToDelete = setListToDelete(listClienti);
        if (listPraticheToDelete == null || listPraticheToDelete.size() <= 1) {
            throw new CannotDeletePraticaException();
        } else {
            return listPraticheToDelete;
        }
    }

    @PreAuthorize("hasAuthority('CLIENTI_DELETE_SUPER') or (hasAuthority('CLIENTI_DELETE_ALL') and @authorizationConditionChecker.isSameAziendaId(@authorizationConditionChecker.getAziendaIfFromCodicePratica(#codicePratica)))" +
            " or (hasAuthority('CLIENTI_DELETE_OWN') and @authorizationConditionChecker.isPraticaAssignedTo(authentication.name, #codicePratica))")
    @Override
    @Transactional
    public void deletePratica(String tenantId, long codicePratica) {
        //first delete files
        deleteFilesForPratica(tenantId, codicePratica);
        //in the end delete pratica
        praticaRepository.delete(codicePratica);
    }

    public List<ClientePratica> setListToDelete(final List<Cliente> listc) {

        final List<ClientePratica> listcp = new ArrayList<>();

        if (listc != null && !listc.isEmpty() && listc.iterator().next().getPratica().size() > 1) {
            for (final Cliente c : listc) {
                for (final Pratica p : c.getPratica()) {
                    setFieldListCliente(listcp, c, p);
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
}
