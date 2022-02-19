package com.woonders.lacemscommon.app.model;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by emanuele on 10/01/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteTrattenuta {

	private ClienteViewModel clienteViewModel;
	private TrattenuteViewModel trattenuteViewModel;
}
