package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entity.Preventivo.TipologiaPreventivo;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idPreventivo", "viewId"}, callSuper = false)
@ToString
public class PreventivoViewModel extends AbstractBaseViewModel {

    private final String viewId = UUID.randomUUID().toString();
    private long idPreventivo;
    private String tipoPratica;
    private BigDecimal rata = BigDecimal.ZERO;
    private Integer durata = 0;
    private BigDecimal importo = BigDecimal.ZERO;
    private BigDecimal tan = BigDecimal.ZERO;
    private BigDecimal taeg = BigDecimal.ZERO;
    private BigDecimal teg = BigDecimal.ZERO;
    private String tipoProvvigione;
    private BigDecimal provvigione = BigDecimal.ZERO;
    private BigDecimal percProvvigione = BigDecimal.ZERO;
    private TipologiaPreventivo tipologiaPreventivo;
    private Date dataCreazione = new Date();
    private boolean notificaPreventivo;
    private BigDecimal provTotale = BigDecimal.ZERO;
    private BigDecimal spese = BigDecimal.ZERO;
    private BigDecimal interessi = BigDecimal.ZERO;
    private SimulatorTableViewModel simulatorTableViewModel;

    @Override
    protected long getIdToCompare() {
        return idPreventivo;
    }
}
