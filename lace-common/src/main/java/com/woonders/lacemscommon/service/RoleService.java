package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.RoleViewModel;
import com.woonders.lacemscommon.exception.ItemNotFoundException;

import java.util.List;

/**
 * Created by Emanuele on 05/10/2016.
 */
// TODO now not used
public interface RoleService {

    List<RoleViewModel> findAll();

    void save(RoleViewModel roleViewModel);

    void delete(RoleViewModel roleViewModel);

    RoleViewModel findOne(final Long id) throws ItemNotFoundException;
}
