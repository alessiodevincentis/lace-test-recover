package com.woonders.lacemscommon.app.viewmodel;


import lombok.*;

/**
 * Created by Cristian on 02/11/21.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString
public class TimeParametersViewModel extends AbstractBaseViewModel {
	
	private long id;
	private String dataType;
	private String valueParameter;
	private String codeIdentify;
	private String description;
	
	@Override
	protected long getIdToCompare() {
		// TODO Auto-generated method stub
		return id;
	}

}
