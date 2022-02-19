package com.woonders.lacemscommon.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.exception.EmailFatturaNonInviataException;
import com.woonders.lacemscommon.exception.FatturaNonCreataException;
import com.woonders.lacemscommon.fattureincloud.FattureInCloudClient;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Pagamento;
import com.woonders.lacemscommon.service.FattureInCloudService;

/**
 * Created by Emanuele on 21/02/2017.
 */
public class FattureInCloudServiceImplTest {

	private FattureInCloudService fattureInCloudService;

	@Before
	public void before() {
		FattureInCloudClient fattureInCloudClient = new FattureInCloudClient();
		fattureInCloudClient.init();
		fattureInCloudService = new FattureInCloudServiceImpl(fattureInCloudClient);
	}

	// DA USARE SOLO QUANDO FACCIAMO QUALCHE TEST ALTRIMENTI CREA FATTURE
	// ANDREBBE FATTO CON UN MOCK
	// @Test
	public void createNewDocTest() throws FatturaNonCreataException, EmailFatturaNonInviataException {
		AziendaViewModel aziendaViewModel = AziendaViewModel.builder().nomeAzienda("2Effe srl")
				.indirizzo("Via di flavio, 75").luogo("Roma").cap("00177").provincia("RM").piva("0101589547")
				.cf("9999999").build();
		List<Articolo> articoloList = new LinkedList<>();
		articoloList.add(Articolo.builder().nome("500 SMS").prezzoNetto(new BigDecimal("15")).tassabile(true).build());
		List<Pagamento> pagamentoList = new LinkedList<>();
		pagamentoList.add(Pagamento.builder().importo("18.30").dataScadenza(LocalDate.now()).dataSaldo(LocalDate.now())
				.metodo(Pagamento.Tipo.STRIPE.toString()).build());
		String id = fattureInCloudService.createNewDoc(aziendaViewModel,
				FattureInCloudService.OggettoFattura.ACQUISTO_SMS, articoloList, pagamentoList);
		fattureInCloudService.inviaMailFattura(id, "ema987@gmail.com",
				FattureInCloudService.OggettoFattura.ACQUISTO_SMS, "In allegato fattura {{allegati}}");
	}
}
