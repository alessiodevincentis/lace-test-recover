package com.woonders.lacemscommon.app.viewmodel;

import java.time.LocalDateTime;

import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Emanuele on 28/03/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString
public class CampagnaSmsViewModel extends AbstractBaseViewModel {

	private long id;
	private String nomeDestinatario;
	private ClienteViewModel cliente;
	private String cognomeDestinatario;
	private String numeroDestinatario;
	private String messageId;
	private EsitoSmsNotificaLead esito;
	private LocalDateTime dataEsito;
	private CampagnaViewModel campagna;

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
