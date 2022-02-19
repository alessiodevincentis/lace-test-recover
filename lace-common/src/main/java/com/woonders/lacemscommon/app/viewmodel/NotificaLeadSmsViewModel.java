package com.woonders.lacemscommon.app.viewmodel;

import java.time.LocalDateTime;

import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString
public class NotificaLeadSmsViewModel extends AbstractBaseViewModel {

	private long id;
	private LocalDateTime dataEsito;
	private EsitoSmsNotificaLead esito;

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
