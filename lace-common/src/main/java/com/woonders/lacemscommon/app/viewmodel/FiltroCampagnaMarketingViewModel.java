package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entity.Cliente.Sesso;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.laceenum.FiltroRinnoviPraticaCoesistenza;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class FiltroCampagnaMarketingViewModel extends AbstractBaseViewModel {

    private long id;
    private Tipo tipo;
    private Sesso sesso;
    private Integer etaFrom;
    private Integer etaTo;
    private String impiego;
    private Integer anniAssunzione;
    private Integer anniPensionamento;
    private String provinciaResidenza;
    private AmministrazioneViewModel amministrazioneViewModel;
    private String statoNominativo;
    private String statoPratica;
    private String provenienza;
    private String tipoPratica;
    private LocalDate dataInizioRinnovoPraticaCoesistenzaCliente;
    private LocalDate dataFineRinnovoPraticaCoesistenzaCliente;
    private LocalDate dataInizioRinnovoImpegnoNominativo;
    private LocalDate dataFineRinnovoImpegnoNominativo;
    private FiltroRinnoviPraticaCoesistenza filtroRinnoviPraticaCoesistenza;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
