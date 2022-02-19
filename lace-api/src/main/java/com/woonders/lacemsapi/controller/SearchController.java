package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by ema98 on 8/9/2017.
 */
//@Controller
//@RequestMapping("/search/*")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = {ControllerConstants.V1 + "/searchByCodiceFiscale"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> searchByCodiceFiscale(final String codiceFiscale) {
        return searchService.searchByCodiceFiscale(codiceFiscale);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/searchByCognome"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> searchByCognome(final String cognome) {
        return searchService.searchByCognome(cognome);
    }

   /* @RequestMapping(value = {ControllerConstants.V1 + "/searchByImpiego"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> searchByImpiego(final String impiego, final Date dataInizio, final Date dataFine, final Role.RoleName clientiReadRoleName, final long operatorId) {
        return searchService.searchByImpiego(impiego, dataInizio, dataFine, clientiReadRoleName, operatorId);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/findByTelefono"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> findByTelefono(final String telefono) {
        return searchService.findByTelefono(telefono);
    }

   /* @RequestMapping(value = {ControllerConstants.V1 + "/searchByAmministrazione"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> searchByAmministrazione(final long idAmministrazione, final Role.RoleName clientiReadRoleName, final long operatorId) {
        return searchService.searchByAmministrazione(idAmministrazione, clientiReadRoleName, operatorId);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/searchByDataRinnovoAndTipoRinnovo"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> searchByDataRinnovoAndTipoRinnovo(final Date dataInizio, final Date dataFine, final String tipoRinnovo, final Role.RoleName clientiReadRoleName, final long operatorId) {
        return searchByDataRinnovoAndTipoRinnovo(dataInizio, dataFine, tipoRinnovo, clientiReadRoleName, operatorId);
    }

    /*@RequestMapping(value = {ControllerConstants.V1 + "/findByStatoPraticaAndOperatoreAndDataCaricamentoAndCinquanta"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> findByStatoPraticaAndOperatoreAndDataCaricamentoAndCinquanta(final String stato, final String operatore, final Date datainizio, final Date datafine, final boolean cinquanta) {
        return searchService.findByStatoPraticaAndOperatoreAndDataCaricamentoAndCinquanta(stato, operatore, datainizio, datafine, cinquanta);
    }*/

    /*@RequestMapping(value = {ControllerConstants.V1 + "/findByStatoNominativoAndOperatoreNominativoAndDataInserimento"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> findByStatoNominativoAndOperatoreNominativoAndDataInserimento(final String statoNominativo, final String operatoreNominativo, final Date dataInizioInserimento, final Date dataFineInserimento) {
        return searchService.findByStatoNominativoAndOperatoreNominativoAndDataInserimento(statoNominativo, operatoreNominativo, dataInizioInserimento, dataFineInserimento);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/getSelectedClientePratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel getSelectedClientePratica(final ClientePratica selectedClientePratica) {
        return searchService.getSelectedClientePratica(selectedClientePratica);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/filterListByCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> filterListByCliente(final List<ClientePratica> listClienteNominativo) {
        return searchService.filterListByCliente(listClienteNominativo);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/completeClientiNominativi"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClienteViewModel> completeClientiNominativi(final String query, final Role.RoleName clientiReadRoleName, final Role.RoleName nominativiReadRoleName, final long operatorId, final long aziendaId) {
        return searchService.completeClientiNominativi(query, clientiReadRoleName, nominativiReadRoleName, operatorId, aziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findOne"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel findOne(final long id) throws ItemNotFoundException {
        return searchService.findOne(id);
    }
}
