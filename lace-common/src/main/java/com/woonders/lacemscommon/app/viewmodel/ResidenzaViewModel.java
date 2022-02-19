package com.woonders.lacemscommon.app.viewmodel;

import java.math.BigDecimal;
import java.util.Date;

import lombok.*;

/**
 * Created by emanuele on 09/01/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "codiceRes" }, callSuper = false)
@ToString
public class ResidenzaViewModel extends AbstractBaseViewModel {

	private BigDecimal speseAbitazione;
	private long codiceRes;
	private String luogoResidenza;
	private String provResidenza;
	private String capResidenza;
	private String indirizzoResidenza;
	private String civicoResidenza;
	private String nazioneResidenza;
	private String indirizzoDomicilio;
	private String luogoDomicilio;
	private String provDomicilio;
	private String capDomicilio;
	private String civicoDomicilio;
	private String nazioneDomicilio;
	private String corrispondenza;
	private Date dataInizioResidenza;
	private String tipoAbitazione;
	private boolean mutuo;
	private String indirizzoPrecedente;
	private String luogoPrecedente;
	private String provinciaPrecedente;
	private String capPrecedente;
	private String civicoPrecedente;
	private String nazionePrecedente;

	@Override
	protected long getIdToCompare() {
		return codiceRes;
	}
}
