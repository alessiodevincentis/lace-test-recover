package com.woonders.lacemscommon.app.model;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientePreventivo {

	private ClienteViewModel clienteViewModel;
	private PreventivoViewModel preventivoViewModel;
}
