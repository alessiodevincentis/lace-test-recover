package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.*;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Estinzione;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.entityutil.PreventivoUtil;
import com.woonders.lacemscommon.db.repository.*;
import com.woonders.lacemscommon.exception.RinnovoNotSavedException;
import com.woonders.lacemscommon.service.PreventivoService;
import com.woonders.lacemscommon.util.PreventivoPraticaUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Getter
@Setter
@Transactional(readOnly = true)
public class PreventivoServiceImpl extends AbstractAppServiceImpl implements PreventivoService {

    public static final String NAME = "preventivoServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";
    @Autowired
    private PreventivoViewModelCreator preventivoViewModelCreator;

    @Autowired
    private PreventivoRepository preventivoRepository;

    @Autowired
    private PraticaRepository praticaRepository;

    @Autowired
    private TrattenuteRepository trattenuteRepository;

    @Autowired
    private EstinzioneRepository estinzioneRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PreferenzaStatoPraticaRepository preferenzaStatoPraticaRepository;

    @Autowired
    private PreventivoUtil util;

    @Autowired
    private PreventivoPraticaUtil preventivoPraticaUtil;

    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;

    @Autowired
    private TrattenuteViewModelCreator trattenuteViewModelCreator;

    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private EstinzioneViewModelCreator estinzioneViewModelCreator;

    @Override
    @Transactional
    public void deletePreventivo(final long preventivoId) {
        preventivoRepository.delete(preventivoId);
    }

    // Utilizzato per visualizzare tutte le pratiche che possono essere estinte
    // quando si crea un preventivo cliente
    @Override
    public List<PraticaViewModel> getAllPraticheAttiveEstinguibili(final long idCliente) {
        Collection<String> statoPraticaStringCollection = new LinkedList<>();
        statoPraticaStringCollection.add(StatoPratica.ESTINTA.getValue());
        statoPraticaStringCollection.add(StatoPratica.ANNULATA.getValue());
        statoPraticaStringCollection.add(StatoPratica.RESPINTA.getValue());
        statoPraticaStringCollection.add(StatoPratica.NON_ISTRUITA.getValue());
        return praticaViewModelCreator.createViewModelList(
                praticaRepository.findDistinctByCliente_IdAndStatoPraticaNotInAndRinnovata(idCliente,
                        statoPraticaStringCollection, false));
    }

    // Utilizzato per visualizzare tutte le coesistenze/trattenute che possono
    // essere estinte quando si crea un preventivo cliente
    @Override
    public List<TrattenuteViewModel> getAllTrattenuteEstinguibili(final long idCliente) {
        return trattenuteViewModelCreator.createViewModelList(trattenuteRepository.findDistinctByCliente_Id(idCliente));
    }

    @Override
    @Transactional
    public void creaPratica(final PreventivoViewModel preventivoViewModel,
                            final List<PraticaViewModel> praticheDaEstinguereViewModelList,
                            final List<TrattenuteViewModel> coesistenzeDaEstinguereViewModelList, final long clienteId, final long aziendaId, final long operatorId)
            throws RinnovoNotSavedException {

        Cliente cliente = clienteRepository.findOne(clienteId);

        PraticaViewModel praticaDaCreareViewModel = preventivoPraticaUtil.setPraticaDaPreventivo(preventivoViewModel, operatorId);
        praticaDaCreareViewModel.setStatoPratica(StatoPratica.PREISTRUTTORIA.getValue());

        praticaDaCreareViewModel
                .setDataNotificaStatoPratica(getDataNotificaByStatoPraticaAndAziendaId(StatoPratica.PREISTRUTTORIA.getValue(), aziendaId));

        final List<EstinzioneViewModel> impegniDaEstinguere = new ArrayList<>();
        if (coesistenzeDaEstinguereViewModelList != null) {
            impegniDaEstinguere
                    .addAll(preventivoPraticaUtil.setEstinzioneDaTrattenuta(coesistenzeDaEstinguereViewModelList));
            for (final TrattenuteViewModel coesistenzaEstinta : coesistenzeDaEstinguereViewModelList) {
                trattenuteRepository.delete(coesistenzaEstinta.getCodStip());
            }
        }

        if (praticheDaEstinguereViewModelList != null) {
            for (final PraticaViewModel praticaViewModel : praticheDaEstinguereViewModelList) {
                praticaViewModel.setRinnovata(true);
                praticaViewModel.setStatoPratica(StatoPratica.ESTINTA.getValue());
                praticaViewModel.setDataNotificaStatoPratica(null);
            }
            impegniDaEstinguere.addAll(preventivoPraticaUtil.setEstinzioneDaPratica(praticheDaEstinguereViewModelList));
        }

        Pratica praticaDaCreare = praticaViewModelCreator.createModel(praticaDaCreareViewModel);
        Set<Pratica> praticheEstinte = praticaViewModelCreator.fromList(praticheDaEstinguereViewModelList);
        Set<Estinzione> estinzioneSet = estinzioneViewModelCreator.fromList(impegniDaEstinguere);

        Set<Pratica> praticaSet = new HashSet<>();
        praticaSet.add(praticaDaCreare);

        praticaDaCreare.setCliente(cliente);
        cliente.setPratica(praticaSet);
        praticaDaCreare = praticaRepository.save(praticaDaCreare);

        praticaDaCreare.setEstinzione(estinzioneSet);
        if (estinzioneSet != null) {
            for (final Estinzione estinzione : estinzioneSet) {
                estinzione.setPratica(praticaDaCreare);
                estinzioneRepository.save(estinzione);
            }
        }

        if (praticheEstinte != null) {
            for (Pratica pratica : praticheEstinte) {
                pratica.setCliente(cliente);
                praticaRepository.save(pratica);
            }
        }

        preventivoRepository.delete(preventivoViewModel.getIdPreventivo());

    }
}
