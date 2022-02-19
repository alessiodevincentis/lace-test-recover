package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.converter.StringAndBigDecimalConverter;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityutil.PraticaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by emanuele on 09/01/17.
 */
@Component
public class PraticaViewModelCreator extends AbstractBaseViewModelCreator<Pratica, PraticaViewModel> {

    @Autowired
    private CoobbligatoViewModelCreator coobbligatoViewModelCreator;
    @Autowired
    private EstinzioneViewModelCreator estinzioneViewModelCreator;
    @Autowired
    private StringAndBigDecimalConverter stringAndBigDecimalConverter;
    @Autowired
    private FinanziariaViewModelCreator finanziariaViewModelCreator;
    @Autowired
    private PraticaUtil praticaUtil;
    @Autowired
    private OperatorViewModelCreator operatorViewModelCreator;
    @Autowired
    private SimulatorTableViewModelCreator simulatorTableViewModelCreator;
    @Autowired
    private ClientePraticaFileViewModelCreator clientePraticaFileViewModelCreator;

    @Override
    public Pratica createModel(PraticaViewModel praticaViewModel) {
        if (praticaViewModel != null) {
            return Pratica.builder()
                    .antiriciclaggioFileCaricato(praticaViewModel.isAntiriciclaggioFileCaricato())
                    .cinquanta(praticaViewModel.isCinquanta()).codicePratica(praticaViewModel.getCodicePratica())
                    .confermaProv(praticaViewModel.isConfermaProv())
                    .coobbligato(coobbligatoViewModelCreator.fromList(praticaViewModel.getCoobbligatoViewModelList()))
                    .dataCaricamento(praticaViewModel.getDataCaricamento())
                    .dataLiquidazione(praticaViewModel.getDataLiquidazione()).dataPerf(praticaViewModel.getDataPerf())
                    .dataRecall(praticaViewModel.getDataRecall()).dataRinnovo(praticaViewModel.getDataRinnovo())
                    .decorrenza(praticaViewModel.getDecorrenza()).durata(praticaViewModel.getDurata())
                    .estinzione(estinzioneViewModelCreator.fromList(praticaViewModel.getEstinzioneViewModelList()))
                    .garanzia(praticaViewModel.getGaranzia()).importoErogato(praticaViewModel.getImportoErogato())
                    .notePratica(praticaViewModel.getNotePratica()).notiRecall(praticaViewModel.getNotiRecall())
                    .notiRinnovo(praticaViewModel.getNotiRinnovo()).nrctr(praticaViewModel.getNrctr())
                    .nrsecci(praticaViewModel.getNrsecci())
                    .operatore(operatorViewModelCreator.createModel(praticaViewModel.getOperatore()))
                    .percProv(praticaViewModel.getPercProv()).perfezionata(praticaViewModel.isPerfezionata())
                    .provvigione(praticaViewModel.getProvvigione()).quintoMax(praticaViewModel.getQuintoMax())
                    .rata(praticaViewModel.getRata()).rinnovata(praticaViewModel.isRinnovata())
                    .scadenzaPratica(praticaViewModel.getScadenzaPratica()).spese(praticaViewModel.getSpese())
                    .statoPratica(praticaViewModel.getStatoPratica()).taeg(praticaViewModel.getTaeg())
                    .tan(praticaViewModel.getTan()).teg(praticaViewModel.getTeg())
                    .tipoPratica(praticaViewModel.getTipoPratica())
                    .tipoProvvigione(praticaViewModel.getTipoProvvigione()).web(praticaViewModel.isWeb())
                    .dataNotificaStatoPratica(praticaViewModel.getDataNotificaStatoPratica())
                    .dataIstruttoria(praticaViewModel.getDataIstruttoria())
                    .dataIstruita(praticaViewModel.getDataIstruita()).dataDelibera(praticaViewModel.getDataDelibera())
                    .dataFirmaContratti(praticaViewModel.getDataFirmaContratti())
                    .capitaleFinanziato(stringAndBigDecimalConverter
                            .getBigDecimalFromStringValue(praticaViewModel.getCapitaleFinanziato()))
                    .finanziaria(finanziariaViewModelCreator.createModel(praticaViewModel.getFinanziariaViewModel()))
                    .simulatorTable(simulatorTableViewModelCreator.createModel(praticaViewModel.getSimulatorTableViewModel()))
                    .praticaFileSet(clientePraticaFileViewModelCreator.fromList(praticaViewModel.getPraticaFileViewModelList()))
                    .build();
        }
        return null;

    }

    @Override
    public PraticaViewModel createViewModel(Pratica pratica) {
        if (pratica != null) {
            return PraticaViewModel.builder()
                    .antiriciclaggioFileCaricato(pratica.isAntiriciclaggioFileCaricato())
                    .cinquanta(pratica.isCinquanta()).codicePratica(pratica.getCodicePratica())
                    .confermaProv(pratica.isConfermaProv())
                    .conteggioEstinzioneTot(praticaUtil.calcConteggioEstinzioneTotale(pratica))
                    .coobbligatoViewModelList(coobbligatoViewModelCreator.fromSet(pratica.getCoobbligato()))
                    .dataCaricamento(pratica.getDataCaricamento()).dataLiquidazione(pratica.getDataLiquidazione())
                    .dataPerf(pratica.getDataPerf()).dataRecall(pratica.getDataRecall())
                    .dataRinnovo(pratica.getDataRinnovo()).debitoResiduo(praticaUtil.calcDebitoResiduo(pratica))
                    .decorrenza(pratica.getDecorrenza()).durata(pratica.getDurata())
                    .estinzioneViewModelList(estinzioneViewModelCreator.fromSet(pratica.getEstinzione()))
                    .garanzia(pratica.getGaranzia()).importoErogato(pratica.getImportoErogato())
                    .interessi(praticaUtil.calcInteressi(pratica)).montante(praticaUtil.calcMontante(pratica))
                    .nettoMensile(praticaUtil.calcNettoMensile(pratica))
                    .nettoRicavo(praticaUtil.calcNettoRicavo(pratica)).notePratica(pratica.getNotePratica())
                    .notiRecall(pratica.getNotiRecall()).notiRinnovo(pratica.getNotiRinnovo()).nrctr(pratica.getNrctr())
                    .nrsecci(pratica.getNrsecci())
                    .operatore(operatorViewModelCreator.createViewModel(pratica.getOperatore()))
                    .percProv(pratica.getPercProv()).perfezionata(pratica.isPerfezionata())
                    .provTotale(praticaUtil.calcProvTotale(pratica)).provvigione(pratica.getProvvigione())
                    .quintoMax(pratica.getQuintoMax()).rata(pratica.getRata())
                    .rateScadenza(praticaUtil.calcRateScadenza(pratica)).rinnovata(pratica.isRinnovata())
                    .scadenzaPratica(pratica.getScadenzaPratica()).spese(pratica.getSpese())
                    .statoPratica(pratica.getStatoPratica()).taeg(pratica.getTaeg()).tan(pratica.getTan())
                    .teg(pratica.getTeg()).tipoPratica(pratica.getTipoPratica())
                    .tipoProvvigione(pratica.getTipoProvvigione()).web(pratica.isWeb())
                    .dataNotificaStatoPratica(pratica.getDataNotificaStatoPratica())
                    .dataIstruttoria(pratica.getDataIstruttoria()).dataIstruita(pratica.getDataIstruita())
                    .dataDelibera(pratica.getDataDelibera()).dataFirmaContratti(pratica.getDataFirmaContratti())
                    .capitaleFinanziato(
                            stringAndBigDecimalConverter.getStringFromBigDecimalValue(pratica.getCapitaleFinanziato()))
                    .finanziariaViewModel(finanziariaViewModelCreator.createViewModel(pratica.getFinanziaria()))
                    .simulatorTableViewModel(simulatorTableViewModelCreator.createViewModel(pratica.getSimulatorTable()))
                    .praticaFileViewModelList(clientePraticaFileViewModelCreator.fromSet(pratica.getPraticaFileSet()))
                    .build();
        }
        return null;
    }
}
