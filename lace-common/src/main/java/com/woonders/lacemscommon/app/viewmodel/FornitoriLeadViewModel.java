package com.woonders.lacemscommon.app.viewmodel;


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
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class FornitoriLeadViewModel extends AbstractBaseViewModel  {

	private long id;
	private String fornitoreLeadName;
    private String provenienza;
    private String provenienzaDesc;
    private int dataRetention;
	
	@Override
    protected long getIdToCompare() {
        return id;
    }
}
