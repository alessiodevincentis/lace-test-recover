package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.AdvancedSearchViewModel;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

/**
 * Created by emanuele on 04/01/17.
 */

/**
 * Manages the logic for searching customers and procedures
 */
public interface SearchService {

    /**
     * return list of customers details and procedure by Tax code
     */
    List<ClientePratica> searchByCodiceFiscale(final String codiceFiscale);

    /**
     * return list of customers details and procedure by Lastname
     */
    List<ClientePratica> searchByCognome(final String cognome);

    /**
     * return list of customers details and procedure by telephone number
     */
    List<ClientePratica> findByTelefono(final String telefono);

    /**
     * return list of customer details and procedure by procedure legal id
     */
    List<ClientePratica> findByNrContratto(String nrContratto);

    /**
     * return paged list of customers details and procedure by an advanced search
     */
    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    ViewModelPageImpl<ClientePratica> advancedSearchClienti(long operatorId,
                                                            Role.RoleName clientiReadRoleName,
                                                            long aziendaId,
                                                            AdvancedSearchViewModel advancedSearchClienti,
                                                            int firstElementIndex, int pageSize,
                                                            String sortField,
                                                            QueryDSLHelper.SortOrder sortOrder);

    /**
     * return paged list of leads details and procedure by an advanced search
     */
    @PreAuthorize("hasAuthority('NOMINATIVI_READ_SUPER') or (hasAuthority('NOMINATIVI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#aziendaId))"
            + " or hasAuthority('NOMINATIVI_READ_OWN')")
    ViewModelPageImpl<ClienteViewModel> advancedSearchNominativi(long operatorId,
                                                                 Role.RoleName nominativiReadRoleName,
                                                                 long aziendaId,
                                                                 AdvancedSearchViewModel advancedSearchNominativiViewModel,
                                                                 int firstElementIndex, int pageSize,
                                                                 String sortField,
                                                                 QueryDSLHelper.SortOrder sortOrder,
                                                                 Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * return customer details and procedure when select it from the datatable result
     */
    ClienteViewModel getSelectedClientePratica(final ClientePratica selectedClientePratica);

    /**
     * TODO
     */
    List<ClientePratica> filterListByCliente(final List<ClientePratica> listClienteNominativo);

    /**
     * TODO
     */
    List<ClienteViewModel> completeClientiNominativi(final String query, Role.RoleName clientiReadRoleName, Role.RoleName nominativiReadRoleName, long operatorId, long aziendaId);

    /**
     * return customer details by id
     */
    ClienteViewModel findOne(long id) throws ItemNotFoundException;

    /**
     * return procedure by id
     */
    ClientePratica getPratica(long id);

    /**
     * return list of operators username
     */
    List<String> getOperatorList(Role.RoleName roleName, long aziendaId);
    
    /**
     * Mod Cristian 11-11-2021
     * return list of provenienzaDesc
     */
    List<String> getProvenienzaDescList();
}
