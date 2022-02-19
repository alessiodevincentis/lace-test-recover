package com.woonders.lacemscommon.app.viewmodel;

import java.math.BigDecimal;
import java.util.Date;

import com.woonders.lacemscommon.db.entity.Cliente;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString
public class CoobbligatoViewModel extends AbstractBaseViewModel {

	private long id;
	private String codiceFiscale;
	private String cognome;
	private String nome;
	private Cliente.Sesso sesso;
	private Date dataNascita;
	private String luogoNascita;
	private String provinciaNascita;
	private String nazioneNascita;
	private String cittadinanza;
	private String indirizzo;
	private String civicoAbitazione;
	private String luogo;
	private String cap;
	private String provincia;
	private String nazioneAbitazione;
	private String telefono;
	private String email;
	private String impiego;
	private String occupazione;
	private Date dataAssunzione;
	private String tipoContrattoLavoro;
	private String telefonoLavoro;
	private String ente;
	private String cat;
	private String tipoDocumento;
	private String numeroDocumento;
	private Date dataRilascioDocumento;
	private String luogoRilascioDocumento;
	private String provinciaRilascioDocumento;
	private String nazioneRilascioDocumento;
	private Date scadenzaDocumento;
	private Integer nrComponentiFamiliari = new Integer(0);
	private BigDecimal nettoStipendio = BigDecimal.ZERO;

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
