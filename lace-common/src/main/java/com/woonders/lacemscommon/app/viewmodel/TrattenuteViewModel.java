package com.woonders.lacemscommon.app.viewmodel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import lombok.*;

/**
 * Created by emanuele on 09/01/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "codStip", "viewId" }, callSuper = false)
@ToString
public class TrattenuteViewModel extends AbstractBaseViewModel {

	private final String viewId = UUID.randomUUID().toString();
	private long codStip;
	private String tipoTrat;
	private BigDecimal rataTrat = BigDecimal.ZERO;
	private Integer durataTrat = 0;
	private Date scadenzaTrat;
	private Date dataRinnovoTrat;
	private Integer rateScadenza = 0;
	private BigDecimal montanteTrat = BigDecimal.ZERO;
	private BigDecimal conteggioTrat = BigDecimal.ZERO;
	private Date busta;
	private String calcolaDat;
	private Date notiCoes;
	private String istitutoTrat;

	@Override
	protected long getIdToCompare() {
		return codStip;
	}
}
