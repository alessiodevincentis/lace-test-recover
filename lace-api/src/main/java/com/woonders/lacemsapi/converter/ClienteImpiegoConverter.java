package com.woonders.lacemsapi.converter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

import com.woonders.lacemsapi.model.app.Lead;
import com.woonders.lacemscommon.db.entityenum.Impiego;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 12/11/2016.
 */
@Slf4j
public class ClienteImpiegoConverter implements Converter<String, Impiego> {

	private List<String> clientJobTypeStringList;
	private List<String> clienteImpiegoStringList;

	public ClienteImpiegoConverter() {
		clientJobTypeStringList = Arrays.stream(Lead.ClientJobType.values()).map(Lead.ClientJobType::getValue)
				.collect(Collectors.toList());
		clienteImpiegoStringList = Arrays.stream(Impiego.values()).map(Impiego::toString).collect(Collectors.toList());
	}

	@Override
	public Impiego convert(final String value) {
		Impiego clienteImpiego = null;
		try {
			if (clientJobTypeStringList.contains(value)) {
				clienteImpiego = fromDevisProx(value);
			} else if (clienteImpiegoStringList.contains(value)) {
				clienteImpiego = fromOurWebsites(value);
			} else {
				log.error("Unable to parse ClientJobType: " + value);
			}
		} catch (final IllegalArgumentException e) {
			log.error("Error in parsing ClientJobType: " + value);
		}
		return clienteImpiego;
	}

	private Impiego fromOurWebsites(String value) {
		return Impiego.valueOf(value);
	}

	private Impiego fromDevisProx(String value) {
		final Optional<Lead.ClientJobType> clientJobTypeOptional = Lead.ClientJobType.fromValue(value);
		if (clientJobTypeOptional.isPresent()) {
			switch (clientJobTypeOptional.get()) {
			case DIPENDENTE_STATALE:
				return Impiego.STATALE;
			case DIPENDENTE_PUBBLICO:
				return Impiego.PUBBLICO;
			case DIPENDENTE_PRIVATO_SPA:
				return Impiego.PRIVATO;
			case DIPENDENTE_PARAPUBBLICO:
				return Impiego.PARAPUBBLICO;
			case PENSIONATO:
				return Impiego.PENSIONATO;
			case ALTRO:
				return Impiego.ALTRO;
			case DIPENDENTE_PRIVATO_SRL:
				return Impiego.PRIVATO;
			case DIPENDENTE_COOPERATIVA:
				return Impiego.PRIVATO;
			case MILITARE:
				return Impiego.MILITARE;
			case LIBERO_PROFESSIONISTA:
				return Impiego.AUTONOMO;
			}
		}
		throw new IllegalArgumentException();
	}
}
