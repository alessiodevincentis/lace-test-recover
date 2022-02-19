package com.woonders.lacemscommon.db.entityutil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;

/**
 * Created by Emanuele on 26/02/2017.
 */
public class AziendaUtilTest {

	private AziendaUtil aziendaUtil = new AziendaUtil();

	@Test
	public void isDatiFatturazioneToBeCompletedTest() {
		AziendaViewModel.AziendaViewModelBuilder aziendaViewModelBuilder = AziendaViewModel.builder();

		assertTrue(aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaViewModelBuilder.build()));

		assertTrue(aziendaUtil
				.isDatiFatturazioneToBeCompleted(aziendaViewModelBuilder.nomeAzienda("NomeAzienda").build()));

		assertTrue(aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaViewModelBuilder.indirizzo("Indirizzo").build()));

		assertTrue(aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaViewModelBuilder.luogo("Luogo").build()));

		assertTrue(aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaViewModelBuilder.cap("CAP").build()));

		assertTrue(aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaViewModelBuilder.provincia("Provincia").build()));

		assertTrue(aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaViewModelBuilder.piva("Piva").build()));

		assertTrue(aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaViewModelBuilder.cf("CF").build()));

		assertFalse(aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaViewModelBuilder.telefono("Telefono").build()));
	}
}
