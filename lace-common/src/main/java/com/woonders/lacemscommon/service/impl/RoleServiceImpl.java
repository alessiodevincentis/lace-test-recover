package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.RoleViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.RoleViewModelCreator;
import com.woonders.lacemscommon.db.repository.RoleRepository;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Emanuele on 05/10/2016.
 */
// TODO ora non usato, da riusare per dashboard sysadmin
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    public static final String NAME = "roleServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";
    @Autowired
    private RoleViewModelCreator roleViewModelCreator;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleViewModel> findAll() {
        return roleViewModelCreator.createViewModelList(roleRepository.findAll());
    }

    @Override
    @Transactional
    public void save(final RoleViewModel roleViewModel) {

    }

    @Override
    @Transactional
    public void delete(final RoleViewModel roleViewModel) {

    }

    @Override
    public RoleViewModel findOne(final Long id) throws ItemNotFoundException {
        try {
            return roleViewModelCreator.createViewModel(roleRepository.findOne(id));
        } catch (final DataAccessException e) {
            throw new ItemNotFoundException(e.getRootCause().getMessage());
        }
    }
}
