package com.woonders.lacemscommon.db.entityenum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by emanuele on 08/01/17.
 */
public enum StatoPratica {

    PREISTRUTTORIA("Preistruttoria"), ISTRUTTORIA("Istruttoria"), ATTESA_CERTIFICATI_ALLEGATI("Attesa Certificati/Allegati"),
    ISTRUITA("Istruita"), PREACCETTAZIONE(
            "PreAccettazione"), NON_ISTRUITA(
            "Non Istruita"), IN_VALUTAZIONE("In Valutazione"), DELIBERA("Deliberata"), POLIZZA(
            "Richiesta Polizza"), DA_NOTIFICARE("Da Notificare"), AB("Attesa AB"), LIQUIDAZIONE("In Liquidazione"), APPROVATA(
            "Approvata"), LIQUIDATA("Liquidata"), DECORRENZA("Quietanzata"), PERFEZIONAMENTO(
            "Perfezionamento"), TERMINATA("Terminata"), ANNULATA(
            "Annullata"), RESPINTA("Respinta"), ESTINTA("Estinta");

    private final String value;

    private StatoPratica(final String value) {
        this.value = value;
    }

    public static StatoPratica fromValue(final String value) {
        if (value == null) {
            return null;
        }
        for (final StatoPratica stato : StatoPratica.values()) {
            if (stato.value.equalsIgnoreCase(value)) {
                return stato;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value);
    }

    public static List<StatoPratica> getStatiPraticaPrestito() {
        final List<StatoPratica> statiPraticaPrestito = new LinkedList<>();
        statiPraticaPrestito.add(ISTRUTTORIA);
        statiPraticaPrestito.add(IN_VALUTAZIONE);
        statiPraticaPrestito.add(APPROVATA);
        statiPraticaPrestito.add(LIQUIDATA);
        statiPraticaPrestito.add(TERMINATA);
        statiPraticaPrestito.add(ESTINTA);
        statiPraticaPrestito.add(RESPINTA);
        statiPraticaPrestito.add(ANNULATA);
        statiPraticaPrestito.add(NON_ISTRUITA);
        return statiPraticaPrestito;
    }

    public static List<StatoPratica> getStatiPraticaCessioneDelega() {
        final List<StatoPratica> statiPraticaCessioneDelega = new LinkedList<>();
        statiPraticaCessioneDelega.add(PREISTRUTTORIA);
        statiPraticaCessioneDelega.add(ISTRUTTORIA);
        statiPraticaCessioneDelega.add(ATTESA_CERTIFICATI_ALLEGATI);
        statiPraticaCessioneDelega.add(ISTRUITA);
        statiPraticaCessioneDelega.add(PREACCETTAZIONE);
        statiPraticaCessioneDelega.add(DELIBERA);
        statiPraticaCessioneDelega.add(POLIZZA);
        statiPraticaCessioneDelega.add(DA_NOTIFICARE);
        statiPraticaCessioneDelega.add(AB);
        statiPraticaCessioneDelega.add(LIQUIDAZIONE);
        statiPraticaCessioneDelega.add(LIQUIDATA);
        statiPraticaCessioneDelega.add(DECORRENZA);
        statiPraticaCessioneDelega.add(PERFEZIONAMENTO);
        statiPraticaCessioneDelega.add(TERMINATA);
        statiPraticaCessioneDelega.add(ANNULATA);
        statiPraticaCessioneDelega.add(RESPINTA);
        statiPraticaCessioneDelega.add(ESTINTA);
        statiPraticaCessioneDelega.add(NON_ISTRUITA);
        return statiPraticaCessioneDelega;
    }

    public static List<String> getStatiPraticheCaricate() {
        final List<String> statiPraticheCaricate = new ArrayList<>();
        statiPraticheCaricate.add(PREISTRUTTORIA.getValue());
        statiPraticheCaricate.add(ISTRUTTORIA.getValue());
        statiPraticheCaricate.add(IN_VALUTAZIONE.getValue());
        statiPraticheCaricate.add(ATTESA_CERTIFICATI_ALLEGATI.getValue());
        statiPraticheCaricate.add(ISTRUITA.getValue());
        statiPraticheCaricate.add(PREACCETTAZIONE.getValue());
        statiPraticheCaricate.add(DELIBERA.getValue());
        statiPraticheCaricate.add(POLIZZA.getValue());
        statiPraticheCaricate.add(DA_NOTIFICARE.getValue());
        statiPraticheCaricate.add(AB.getValue());
        statiPraticheCaricate.add(LIQUIDAZIONE.getValue());
        statiPraticheCaricate.add(APPROVATA.getValue());
        return statiPraticheCaricate;
    }

    public static List<String> getStatiPraticheLiquidate() {
        final List<String> statiPraticheLiquidate = new ArrayList<>();
        statiPraticheLiquidate.add(LIQUIDATA.getValue());
        statiPraticheLiquidate.add(ESTINTA.getValue());
        statiPraticheLiquidate.add(DECORRENZA.getValue());
        statiPraticheLiquidate.add(PERFEZIONAMENTO.getValue());
        statiPraticheLiquidate.add(TERMINATA.getValue());
        return statiPraticheLiquidate;
    }

    public static List<String> getMainStatiPratica() {
        final List<String> mainStatiPratica = new ArrayList<>();
        mainStatiPratica.add(PREISTRUTTORIA.getValue());
        mainStatiPratica.add(ISTRUTTORIA.getValue());
        mainStatiPratica.add(IN_VALUTAZIONE.getValue());
        mainStatiPratica.add(ATTESA_CERTIFICATI_ALLEGATI.getValue());
        mainStatiPratica.add(ISTRUITA.getValue());
        mainStatiPratica.add(PREACCETTAZIONE.getValue());
        mainStatiPratica.add(DELIBERA.getValue());
        mainStatiPratica.add(POLIZZA.getValue());
        mainStatiPratica.add(DA_NOTIFICARE.getValue());
        mainStatiPratica.add(AB.getValue());
        mainStatiPratica.add(LIQUIDAZIONE.getValue());
        mainStatiPratica.add(APPROVATA.getValue());
        mainStatiPratica.add(LIQUIDATA.getValue());
        mainStatiPratica.add(ESTINTA.getValue());
        mainStatiPratica.add(DECORRENZA.getValue());
        mainStatiPratica.add(PERFEZIONAMENTO.getValue());
        mainStatiPratica.add(TERMINATA.getValue());
        return mainStatiPratica;
    }

    public String getValue() {
        return value;
    }

}
