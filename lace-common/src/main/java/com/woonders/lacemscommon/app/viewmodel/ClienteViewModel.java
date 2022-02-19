package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.FornitoriLead;
import com.woonders.lacemscommon.util.DateToCalendar;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by emanuele on 09/01/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id", "nome", "cognome", "telefono"})
public class ClienteViewModel extends AbstractBaseViewModel {

    private long id;
    private String cf;
    private String cognome;
    private String nome;
    private Date dataNascita;
    private String luogoNascita;
    private String provNascita;
    private String capNascita;
    private String nazioneNascita;
    private String telefono;
    private String email;
    private String statoCivile;
    private String beni;
    private String impiego;
    private String qualifica;
    private String occupazione;
    private Date dataInizio;
    private String ente;
    private String cat;
    private String note;
    private Date dataIns = new Date();
    private ContoViewModel contoViewModel;
    private DocumentoViewModel documentoViewModel;
    private ResidenzaViewModel residenzaViewModel;
    private List<TrattenuteViewModel> trattenutaViewModelList;
    private List<PraticaViewModel> praticaViewModelList;
    private String eta;
    private DateToCalendar opt = new DateToCalendar();
    private Integer anniAssunzione = 0;
    private Cliente.Sesso sesso;
    private String tipo;
    private Date dataCliente;
    private String provenienza;
    private String provenienzaDesc;
    private String fornitoreLead;
    private OperatorViewModel operatoreNominativo;
    private String statoNominativo;
    private Date dataRecallNominativo;
    private String telefonoFisso;
    private String tipoContrattoLavoro;
    private String telefonoLavoro;
    private Integer nrComponentiFamiliari = 0;
    private BigDecimal nettoMensileNominativo = BigDecimal.ZERO;
    private BigDecimal importoRichiesto = BigDecimal.ZERO;
    private String impiego2;
    private String qualifica2;
    private String occupazione2;
    private Date dataInizio2;
    private String ente2;
    private String cat2;
    private String tipoContrattoLavoro2;
    private String telefonoLavoro2;
    private boolean secondaOccupazionePredefinita;
    private boolean secondaOccupazioneRendered;
    private Integer durataRichiestaMesi;
    private Integer anniAssunzione2 = 0;
    private List<AmministrazioneViewModel> amministrazioneViewModelList;
    private String leadId;
    private List<PreventivoViewModel> preventivoViewModelList;
    private String cittadinanza;
    private List<ClientePraticaFileViewModel> clienteFileViewModelList;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
