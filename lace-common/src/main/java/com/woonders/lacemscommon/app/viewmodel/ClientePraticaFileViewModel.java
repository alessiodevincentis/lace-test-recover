package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.service.FileService;
import lombok.*;

/**
 * Created by ema98 on 9/15/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class ClientePraticaFileViewModel extends AbstractBaseViewModel {

    private long id;
    private Cliente cliente;
    private Pratica pratica;
    private FileService.FileCategory fileCategory;
    private String fileName;
    private int length;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
