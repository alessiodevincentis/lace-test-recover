package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by emanuele on 09/01/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idEstinzione", "viewId"}, callSuper = false)
@ToString
public class EstinzioneViewModel extends AbstractBaseViewModel {

    private final String viewId = UUID.randomUUID().toString();
    private long idEstinzione;
    private String tipoEstinzione;
    private BigDecimal rataEstinzione = BigDecimal.ZERO;
    private Integer durataEstinzione = 0;
    private Date scadenzaEstinzione;
    private BigDecimal montanteEstinzione = BigDecimal.ZERO;
    private String istitutoEst;
    private Date dataBustapaga;
    private Integer mesiScadenza = 0;
    private BigDecimal conteggioEstinzione = BigDecimal.ZERO;
    private String calcolaScadenza;
    private Date dataRinnovoEstinzione;
    private Date dataNotificaConteggioEstinzione;
    private StatoConteggioEstinzione statoConteggioEstinzione;

    @Override
    protected long getIdToCompare() {
        return idEstinzione;
    }
}
