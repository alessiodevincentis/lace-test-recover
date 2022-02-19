package com.woonders.lacemscommon.db.repository;

import com.woonders.lacemscommon.db.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Emanuele on 07/11/2016.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, QueryDslPredicateExecutor<Cliente> {

    Cliente findDistinctByCf(String cf);

    List<Cliente> findDistinctByCognome(String cognome);

    List<Cliente> findDistinctByTelefonoContainingOrTelefonoFissoContaining(String telefono, String telefonoFisso);

    List<Cliente> findDistinctByTipoAndStatoNominativoAndOperatoreNominativo_UsernameAndDataInsBetween(String tipo,
                                                                                                       String statoNominativo, String operatoreNominativo, Date dataInizioInserimento, Date dataFineInserimento);

    List<Cliente> findDistinctByTipoAndOperatoreNominativo_UsernameAndDataInsBetween(String tipo,
                                                                                     String operatoreNominativo, Date dataInizioInserimento, Date dataFineInserimento);

    List<Cliente> findDistinctByTipoAndStatoNominativoAndDataInsBetween(String tipo, String statoNominativo,
                                                                        Date dataInizioInserimento, Date dataFineInserimento);

    List<Cliente> findDistinctByTipoAndDataInsBetween(String tipo, Date dataInizioInserimento,
                                                      Date dataFineInserimento);

    Cliente findByCfIgnoreCaseAndTipoIgnoreCase(String codiceFiscale, String tipo);

    Cliente findByCfIgnoreCase(String codiceFiscale);

    void deleteByCfIgnoreCaseAndTipoIgnoreCase(String cf, String tipoCliente);

    List<Cliente> findDistinctByCfAndTipoAndPratica_Operatore_Id(String cf, String tipoCliente, long currentUser);

    List<Cliente> findDistinctByCfAndTipo(String cf, String tipoCliente);

    @Deprecated
    Page<Cliente> findDistinctByStatoNominativoAndTipo(String statoNominativo, String tipo, Pageable pageable);

    @Deprecated
    Page<Cliente> findDistinctByStatoNominativoAndTipoAndOperatoreNominativo(String statoNominativo, String tipo,
                                                                             String operatore, Pageable pageable);

    Cliente findByPratica_CodicePratica(long praticaId);

    long countByCognomeAndNomeAndTelefono(String cognome, String nome, String telefono);

    List<Cliente> findByCognomeAndNomeAndTelefono(String cognome, String nome, String telefono);

    List<Cliente> findByTipo(String tipo);

    @Query("select distinct cliente.fornitoreLead from Cliente cliente where cliente.provenienza = 'Lead' and cliente.fornitoreLead is not null and cliente.fornitoreLead <> ''")
    List<String> getAllFornitoriLead();
    
    @Query("select distinct cliente.provenienzaDesc from Cliente cliente where cliente.provenienzaDesc is not null and cliente.provenienzaDesc <> ''")
    List<String> getDistinctProvenienzaDesc();

    Cliente findById(long id);
    
    Cliente findByLeadId(String leadId);
}
