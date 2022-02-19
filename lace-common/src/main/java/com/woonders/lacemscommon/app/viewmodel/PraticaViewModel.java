package com.woonders.lacemscommon.app.viewmodel;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by emanuele on 09/01/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"codicePratica", "viewId"}, callSuper = false)
@ToString
public class PraticaViewModel extends AbstractBaseViewModel {

    private final String viewId = UUID.randomUUID().toString();
    private long codicePratica;
    private String nrsecci;
    private String nrctr;
    private BigDecimal rata = BigDecimal.ZERO;
    private Integer durata = 0;
    private String tipoPratica;
    private BigDecimal montante = BigDecimal.ZERO;
    private BigDecimal importoErogato = BigDecimal.ZERO;
    private BigDecimal spese = BigDecimal.ZERO;
    private BigDecimal tan = BigDecimal.ZERO;
    private BigDecimal taeg = BigDecimal.ZERO;
    private BigDecimal teg = BigDecimal.ZERO;
    private String garanzia;
    private BigDecimal quintoMax = BigDecimal.ZERO;
    private Date dataCaricamento = new Date();
    private Date dataRinnovo;
    private OperatorViewModel operatore;
    private String tipoProvvigione;
    private BigDecimal provvigione = BigDecimal.ZERO;
    private BigDecimal percProv = BigDecimal.ZERO;
    private BigDecimal provTotale = BigDecimal.ZERO;
    private boolean perfezionata;
    private boolean cinquanta;
    private boolean confermaProv;
    private Date dataPerf;
    private Date notiRinnovo;
    private boolean web;
    private Date dataLiquidazione;
    private ClienteViewModel clienteViewModel;
    private BigDecimal nettoRicavo = BigDecimal.ZERO;
    private BigDecimal nettoMensile = BigDecimal.ZERO;
    private BigDecimal interessi = BigDecimal.ZERO;
    private String statoPratica;
    private String notePratica;
    private Date notiRecall;
    private Date dataRecall;
    private Date decorrenza;
    private boolean rinnovata;
    private Date scadenzaPratica;
    private boolean antiriciclaggioFileCaricato;
    private BigDecimal conteggioEstinzioneTot = BigDecimal.ZERO;
    private int rateScadenza = 0;
    private BigDecimal debitoResiduo = BigDecimal.ZERO;
    private List<EstinzioneViewModel> estinzioneViewModelList = new LinkedList<>();
    private List<CoobbligatoViewModel> coobbligatoViewModelList = new LinkedList<>();
    private FinanziariaViewModel finanziariaViewModel;
    private Date dataNotificaStatoPratica;
    private Date dataIstruttoria;
    private Date dataIstruita;
    private Date dataDelibera;
    private Date dataFirmaContratti;
    private String capitaleFinanziato;
    private SimulatorTableViewModel simulatorTableViewModel;
    private List<ClientePraticaFileViewModel> praticaFileViewModelList;

    @Override
    protected long getIdToCompare() {
        return codicePratica;
    }

}
