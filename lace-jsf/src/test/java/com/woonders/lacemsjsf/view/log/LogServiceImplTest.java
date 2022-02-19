package com.woonders.lacemsjsf.view.log;

import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.NominativoLog;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.service.LogService;
import com.woonders.lacemscommon.service.impl.LogServiceImpl;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Emanuele on 08/02/2017.
 */
public class LogServiceImplTest {

	private LogServiceImpl logServiceImpl = new LogServiceImpl();

	@Test
	public void checkIfLastEntryIsSameTest() {
		Instant instant = Clock.systemUTC().instant();

		String commento = "CIAO";

		NominativoLog nominativoLog = NominativoLog.builder()
				.operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(LocalDateTime.ofInstant(instant, ZoneId.of("GMT")))
				.statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		Cliente nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(1L).build())
				.dataRecallNominativo(Date.from(instant)).statoNominativo("Attesa Documenti").build();

		assertTrue(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = "CIAO";

		nominativoLog = NominativoLog.builder().operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(LocalDateTime.ofInstant(instant, ZoneId.of("GMT")))
				.statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(2L).build()).dataRecallNominativo(Date.from(instant))
				.statoNominativo("Attesa Documenti").build();

		assertFalse(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = "CIAO";

		nominativoLog = NominativoLog.builder().operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(LocalDateTime.ofInstant(instant, ZoneId.of("GMT")))
				.statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(1L).build())
				.dataRecallNominativo(Date.from(Clock.systemUTC().instant())).statoNominativo("Attesa Documenti")
				.build();

		assertFalse(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = "CIAOOOOOOOOOOOOO";

		nominativoLog = NominativoLog.builder().operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(LocalDateTime.ofInstant(instant, ZoneId.of("GMT")))
				.statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(1L).build()).dataRecallNominativo(Date.from(instant))
				.statoNominativo("Attesa Documenti").build();

		assertTrue(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = "CIAOOOOOOOOOOOOO";

		nominativoLog = NominativoLog.builder().operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(LocalDateTime.ofInstant(instant, ZoneId.of("GMT")))
				.statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(2L).build()).dataRecallNominativo(Date.from(instant))
				.statoNominativo("Attesa Documenti").build();

		assertFalse(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = "CIAO";

		nominativoLog = NominativoLog.builder().operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(LocalDateTime.ofInstant(instant, ZoneId.of("GMT")))
				.statoNominativo(StatoNominativo.DA_RICHIAMARE).commento("CIAO")
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(1L).build()).dataRecallNominativo(Date.from(instant))
				.statoNominativo("Attesa Documenti").build();

		assertFalse(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = "CIAO";

		nominativoLog = NominativoLog.builder().operatoreAssegnato(null)
				.dataRecall(LocalDateTime.ofInstant(instant, ZoneId.of("GMT")))
				.statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(1L).build()).dataRecallNominativo(Date.from(instant))
				.statoNominativo("Attesa Documenti").build();

		assertFalse(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = "CIAO";

		nominativoLog = NominativoLog.builder().operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(null).statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(1L).build()).dataRecallNominativo(Date.from(instant))
				.statoNominativo("Attesa Documenti").build();

		assertFalse(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = "CIAO";

		nominativoLog = NominativoLog.builder().operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(null).statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(1L).build()).dataRecallNominativo(null)
				.statoNominativo("Attesa Documenti").build();

		assertTrue(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = "";

		nominativoLog = NominativoLog.builder().operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(LocalDateTime.ofInstant(instant, ZoneId.of("GMT")))
				.statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(1L).build()).dataRecallNominativo(Date.from(instant))
				.statoNominativo("Attesa Documenti").build();

		assertTrue(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));

		commento = null;

		nominativoLog = NominativoLog.builder().operatoreAssegnato(Operator.builder().id(1L).build())
				.dataRecall(LocalDateTime.ofInstant(instant, ZoneId.of("GMT")))
				.statoNominativo(StatoNominativo.ATTESA_DOC).commento(commento)
				.tipoLog(LogService.TipoLog.NOMINATIVO).build();

		nominativoSalvato = Cliente.builder().operatoreNominativo(Operator.builder().id(1).build()).dataRecallNominativo(Date.from(instant))
				.statoNominativo("Attesa Documenti").build();

		assertTrue(logServiceImpl.isLastEntrySame(nominativoLog, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(),
				nominativoSalvato.getOperatoreNominativo().getId()));
	}

}
