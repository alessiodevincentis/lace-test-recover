package com.woonders.lacemscommon.app.model.util;

import com.google.common.collect.Lists;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ClientePreventivo;
import com.woonders.lacemscommon.app.model.EstinzionePraticaCliente;
import com.woonders.lacemscommon.app.viewmodel.creator.*;
import com.woonders.lacemscommon.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Emanuele on 22/06/2017.
 */
@Component
public class ClientePraticaUtil {

    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private TrattenuteViewModelCreator trattenuteViewModelCreator;
    @Autowired
    private PreventivoViewModelCreator preventivoViewModelCreator;
    @Autowired
    private EstinzioneViewModelCreator estinzioneViewModelCreator;

    public List<ClientePratica> generateClientePraticaList(final List<Pratica> praticaList) {
        return praticaList.stream()
                .map(pratica -> ClientePratica.builder()
                        .clienteViewModel(clienteViewModelCreator.createViewModel(pratica.getCliente()))
                        .praticaViewModel(praticaViewModelCreator.createViewModel(pratica)).build())
                .collect(Collectors.toList());
    }

    public List<ClientePratica> generateClientePraticaList(final Iterable<Pratica> praticaIterable) {
        return generateClientePraticaList(Lists.newArrayList(praticaIterable));
    }

    public List<EstinzionePraticaCliente> generateEstinzionePraticaListFromEstinzioneList(final List<Estinzione> estinzioneList) {
        return estinzioneList.stream()
                .map(estinzione -> EstinzionePraticaCliente.builder()
                        .estinzioneViewModel(estinzioneViewModelCreator.createViewModel(estinzione))
                        .praticaViewModel(praticaViewModelCreator.createViewModel(estinzione.getPratica()))
                        .clienteViewModel(clienteViewModelCreator.createViewModel(estinzione.getPratica().getCliente())).build())
                .collect(Collectors.toList());
    }

    public List<EstinzionePraticaCliente> generateEstinzionePraticaListFromEstinzioneList(final Iterable<Estinzione> estinzioneIterable) {
        return generateEstinzionePraticaListFromEstinzioneList(Lists.newArrayList(estinzioneIterable));
    }

    // TODO perche riguardiamo i permessi? boh!
    public List<ClientePratica> generateClienteTrattenuteViewModel(final Iterable<Trattenute> trattenuteIterable,
                                                                   final Long currentAziendaId, final long operatorId, final Role.RoleName clientiReadRoleName) {
        final List<ClientePratica> clientePraticaList = new LinkedList<>();
        trattenuteIterable.forEach(trattenute -> {
            final ClientePratica clientePratica = new ClientePratica();
            clientePratica.setTrattenuteViewModel(trattenuteViewModelCreator.createViewModel(trattenute));
            clientePratica.setClienteViewModel(clienteViewModelCreator.createViewModel(trattenute.getCliente()));
            if (trattenute.getCliente().getPratica() != null && !trattenute.getCliente().getPratica().isEmpty()) {
                for (final Pratica pratica : trattenute.getCliente().getPratica()) {
                    // se ha piu pratiche potrebbe prendere una di un altro
                    // operatore mentre deve prendere la propria!!
                    if ((Role.RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName)
                            && pratica.getOperatore().getId() == operatorId)
                            || (Role.RoleName.CLIENTI_READ_ALL.equals(clientiReadRoleName)
                            && pratica.getOperatore().getAzienda().getId().equals(currentAziendaId))
                            || Role.RoleName.CLIENTI_READ_SUPER.equals(clientiReadRoleName)) {
                        if (trattenute.getCliente().getPratica().size() > 1
                                && pratica.getOperatore().getId() == operatorId && (currentAziendaId == null
                                || pratica.getOperatore().getAzienda().getId().equals(currentAziendaId))) {
                            clientePratica.setPraticaViewModel(praticaViewModelCreator.createViewModel(pratica));
                            break;
                        } else {
                            clientePratica.setPraticaViewModel(praticaViewModelCreator.createViewModel(pratica));
                            // break intenzionalmente NON messo!
                            // quello che dovrebbe fare questo codice e che se
                            // quel cliente ha piu pratiche con piu operatori,
                            // viene mostrata la pratica
                            // dell operatore corrente se stiamo vedendo la sua
                            // azienda (o qualsiasi), altrimenti se sta vedendo
                            // un-altra azienda viene mostrata la pratica di un
                            // altro operatore
                            // visto che lui non fa parte di quell-azienda
                            // TODO in ogni caso, va pensata una visualizzazione
                            // Trattenuta perche e completamente sbagliato far
                            // vedere una pratica, qui stiamo cercando le
                            // trattenute!
                            // (non dovevi da retta a flavio!! XD)
                        }
                    }
                }

            }
            clientePraticaList.add(clientePratica);
        });
        return clientePraticaList;
    }

    public List<ClientePreventivo> generateClientePreventivoList(final Iterable<Preventivo> preventivoIterable) {
        return generateClientePreventivoList(Lists.newArrayList(preventivoIterable));
    }

    public List<ClientePreventivo> generateClientePreventivoList(final List<Preventivo> preventivoList) {
        return preventivoList.stream()
                .map(preventivo -> ClientePreventivo.builder()
                        .clienteViewModel(clienteViewModelCreator.createViewModel(preventivo.getCliente()))
                        .preventivoViewModel(preventivoViewModelCreator.createViewModel(preventivo)).build())
                .collect(Collectors.toList());
    }
}
