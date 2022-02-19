package com.woonders.lacemscommon.app.viewmodel;

import lombok.*;

/**
 * Created by emanuele on 09/01/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString
public class FinanziariaViewModel extends AbstractBaseViewModel {

	private long id;
	private String name;

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
