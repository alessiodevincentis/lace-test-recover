package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entityutil.ClienteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by emanuele on 09/01/17.
 */
@Component
public class ClienteViewModelCreator extends AbstractBaseViewModelCreator<Cliente, ClienteViewModel> {

    @Autowired
    private AmministrazioneViewModelCreator amministrazioneViewModelCreator;
    @Autowired
    private ContoViewModelCreator contoViewModelCreator;
    @Autowired
    private DocumentoViewModelCreator documentoViewModelCreator;
    @Autowired
    private PreventivoViewModelCreator preventivoViewModelCreator;
    @Autowired
    private ResidenzaViewModelCreator residenzaViewModelCreator;
    @Autowired
    private TrattenuteViewModelCreator trattenutaViewModelCreator;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private ClienteUtil clienteUtil;
    @Autowired
    private OperatorViewModelCreator operatorViewModelCreator;
    @Autowired
    private ClientePraticaFileViewModelCreator clientePraticaFileViewModelCreator;

    @Override
    public Cliente createModel(final ClienteViewModel clienteViewModel) {
        if (clienteViewModel != null) {
            return Cliente.builder()
                    .amministrazione(amministrazioneViewModelCreator
                            .fromList(clienteViewModel.getAmministrazioneViewModelList()))
                    .beni(clienteViewModel.getBeni()).capNascita(clienteViewModel.getCapNascita())
                    .cat(clienteViewModel.getCat()).cat2(clienteViewModel.getCat2()).cf(clienteViewModel.getCf())
                    .cognome(clienteViewModel.getCognome())
                    .conto(contoViewModelCreator.createModel(clienteViewModel.getContoViewModel()))
                    .dataCliente(clienteViewModel.getDataCliente()).dataInizio(clienteViewModel.getDataInizio())
                    .dataInizio2(clienteViewModel.getDataInizio2()).dataIns(clienteViewModel.getDataIns())
                    .dataNascita(clienteViewModel.getDataNascita())
                    .dataRecallNominativo(clienteViewModel.getDataRecallNominativo())
                    .leadId(clienteViewModel.getLeadId())
                    .documento(documentoViewModelCreator.createModel(clienteViewModel.getDocumentoViewModel()))
                    .durataRichiestaMesi(clienteViewModel.getDurataRichiestaMesi()).email(clienteViewModel.getEmail())
                    .ente(clienteViewModel.getEnte()).ente2(clienteViewModel.getEnte2()).id(clienteViewModel.getId())
                    .impiego(clienteViewModel.getImpiego()).impiego2(clienteViewModel.getImpiego2())
                    .importoRichiesto(clienteViewModel.getImportoRichiesto())
                    .luogoNascita(clienteViewModel.getLuogoNascita())
                    .nettoMensileNominativo(clienteViewModel.getNettoMensileNominativo())
                    .nome(clienteViewModel.getNome()).note(clienteViewModel.getNote())
                    .nrComponentiFamiliari(clienteViewModel.getNrComponentiFamiliari())
                    .occupazione(clienteViewModel.getOccupazione()).occupazione2(clienteViewModel.getOccupazione2())
                    .operatoreNominativo(
                            operatorViewModelCreator.createModel(clienteViewModel.getOperatoreNominativo()))
                    .pratica(praticaViewModelCreator.fromList(clienteViewModel.getPraticaViewModelList()))
                    .preventivo(preventivoViewModelCreator.fromList(clienteViewModel.getPreventivoViewModelList()))
                    .provenienza(clienteViewModel.getProvenienza())
                    .provenienzaDesc(clienteViewModel.getProvenienzaDesc())
                    .fornitoreLead(clienteViewModel.getFornitoreLead())
                    .provNascita(clienteViewModel.getProvNascita()).qualifica(clienteViewModel.getQualifica())
                    .qualifica2(clienteViewModel.getQualifica2())
                    .residenza(residenzaViewModelCreator.createModel(clienteViewModel.getResidenzaViewModel()))
                    .secondaOccupazionePredefinita(clienteViewModel.isSecondaOccupazionePredefinita())
                    .secondaOccupazioneRendered(clienteViewModel.isSecondaOccupazioneRendered())
                    .sesso(clienteViewModel.getSesso()).statoCivile(clienteViewModel.getStatoCivile())
                    .statoNominativo(clienteViewModel.getStatoNominativo()).telefono(clienteViewModel.getTelefono())
                    .telefonoFisso(clienteViewModel.getTelefonoFisso())
                    .telefonoLavoro(clienteViewModel.getTelefonoLavoro())
                    .telefonoLavoro2(clienteViewModel.getTelefonoLavoro2()).tipo(clienteViewModel.getTipo())
                    .tipoContrattoLavoro(clienteViewModel.getTipoContrattoLavoro())
                    .tipoContrattoLavoro2(clienteViewModel.getTipoContrattoLavoro2())
                    .trattenuta(trattenutaViewModelCreator.fromList(clienteViewModel.getTrattenutaViewModelList()))
                    .cittadinanza(clienteViewModel.getCittadinanza())
                    .nazioneNascita(clienteViewModel.getNazioneNascita())
                    .clienteFileSet(clientePraticaFileViewModelCreator.fromList(clienteViewModel.getClienteFileViewModelList()))
                    .build();
        }
        return null;
    }

    @Override
    public ClienteViewModel createViewModel(final Cliente cliente) {
        if (cliente != null) {
            return ClienteViewModel.builder()
                    .amministrazioneViewModelList(amministrazioneViewModelCreator.fromSet(cliente.getAmministrazione()))
                    .anniAssunzione(clienteUtil.calcAnniAssunzione(cliente.getDataInizio()))
                    .anniAssunzione2(clienteUtil.calcAnniAssunzione(cliente.getDataInizio2())).beni(cliente.getBeni())
                    .capNascita(cliente.getCapNascita()).cat(cliente.getCat()).cat2(cliente.getCat2())
                    .cf(cliente.getCf()).cognome(cliente.getCognome())
                    .contoViewModel(contoViewModelCreator.createViewModel(cliente.getConto()))
                    .dataCliente(cliente.getDataCliente()).dataInizio(cliente.getDataInizio())
                    .dataInizio2(cliente.getDataInizio2()).dataIns(cliente.getDataIns())
                    .dataNascita(cliente.getDataNascita()).dataRecallNominativo(cliente.getDataRecallNominativo())
                    .leadId(cliente.getLeadId())
                    .documentoViewModel(documentoViewModelCreator.createViewModel(cliente.getDocumento()))
                    .durataRichiestaMesi(cliente.getDurataRichiestaMesi()).email(cliente.getEmail())
                    .ente(cliente.getEnte()).ente2(cliente.getEnte2())
                    .eta(clienteUtil.calcEta(cliente.getDataNascita())).id(cliente.getId())
                    .impiego(cliente.getImpiego()).impiego2(cliente.getImpiego2())
                    .importoRichiesto(cliente.getImportoRichiesto()).luogoNascita(cliente.getLuogoNascita())
                    .nettoMensileNominativo(cliente.getNettoMensileNominativo()).nome(cliente.getNome())
                    .note(cliente.getNote()).nrComponentiFamiliari(cliente.getNrComponentiFamiliari())
                    .occupazione(cliente.getOccupazione()).occupazione2(cliente.getOccupazione2())
                    .operatoreNominativo(operatorViewModelCreator.createViewModel(cliente.getOperatoreNominativo()))
                    .praticaViewModelList(praticaViewModelCreator.fromSet(cliente.getPratica()))
                    .preventivoViewModelList(preventivoViewModelCreator.fromSet(cliente.getPreventivo()))
                    .provenienza(cliente.getProvenienza()).provenienzaDesc(cliente.getProvenienzaDesc())
                    .fornitoreLead(cliente.getFornitoreLead())
                    .provNascita(cliente.getProvNascita()).qualifica(cliente.getQualifica())
                    .qualifica2(cliente.getQualifica2())
                    .residenzaViewModel(residenzaViewModelCreator.createViewModel(cliente.getResidenza()))
                    .secondaOccupazionePredefinita(cliente.isSecondaOccupazionePredefinita())
                    .secondaOccupazioneRendered(cliente.isSecondaOccupazioneRendered()).sesso(cliente.getSesso())
                    .statoCivile(cliente.getStatoCivile()).statoNominativo(cliente.getStatoNominativo())
                    .telefono(cliente.getTelefono()).telefonoFisso(cliente.getTelefonoFisso())
                    .telefonoLavoro(cliente.getTelefonoLavoro()).telefonoLavoro2(cliente.getTelefonoLavoro2())
                    .tipo(cliente.getTipo()).tipoContrattoLavoro(cliente.getTipoContrattoLavoro())
                    .tipoContrattoLavoro2(cliente.getTipoContrattoLavoro2())
                    .trattenutaViewModelList(trattenutaViewModelCreator.fromSet(cliente.getTrattenuta()))
                    .cittadinanza(cliente.getCittadinanza()).nazioneNascita(cliente.getNazioneNascita())
                    .clienteFileViewModelList(clientePraticaFileViewModelCreator.fromSet(cliente.getClienteFileSet()))
                    .build();
        }
        return null;
    }

}
