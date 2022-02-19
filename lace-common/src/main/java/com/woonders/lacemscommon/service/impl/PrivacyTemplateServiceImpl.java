package com.woonders.lacemscommon.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.PrivacyTemplateViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.PrivacyTemplateViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder;
import com.woonders.lacemscommon.db.QueryDSLHelper.TableField;
import com.woonders.lacemscommon.db.entity.ClientePrivacyTemplate;
import com.woonders.lacemscommon.db.entity.PrivacyTemplate;
import com.woonders.lacemscommon.db.repository.PrivacyTemplateRepository;
import com.woonders.lacemscommon.exception.PermissionDeniedException;
import com.woonders.lacemscommon.service.PrivacyTemplateService;
import com.woonders.lacemscommon.util.PredicateHelper;

/**
 * Created by Cristian on 16/11/21.
 */
@Service
@Transactional(readOnly = true)
public class PrivacyTemplateServiceImpl implements PrivacyTemplateService {

	public static final String NAME = "privacyTemplateServiceImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@Autowired
	private PrivacyTemplateRepository privacyTemplateRepository;
	@Autowired
	private PrivacyTemplateViewModelCreator privacyTemplateViewModelCreator;
	 @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private PredicateHelper predicateHelper;
	@Override
	@Transactional
	public PrivacyTemplateViewModel save(PrivacyTemplateViewModel privacyTemplateViewModel) {
		return privacyTemplateViewModelCreator
				.createViewModel(privacyTemplateRepository.save(privacyTemplateViewModelCreator.createModel(privacyTemplateViewModel)));
	}
	

	@Override
	@Transactional
	public PrivacyTemplate save(PrivacyTemplate privacyTemplate) {
		return privacyTemplateRepository.save(privacyTemplate);
	}

	@Override
	public PrivacyTemplateViewModel getOne(long id) {
		return privacyTemplateViewModelCreator.createViewModel(privacyTemplateRepository.findOne(id));
	}

	@Override
	public PrivacyTemplate findOne(long id) {
		return privacyTemplateRepository.findOne(id);
	}

	@Override
	public PrivacyTemplate findOneByFornitoreLead(String fornitoreLead) {
		// TODO Auto-generated method stub
		return privacyTemplateRepository.findByFornitoreLead(fornitoreLead);
	}


	@Override
	public List<PrivacyTemplateViewModel> findAll() {
		// TODO Auto-generated method stub
		return privacyTemplateViewModelCreator.createViewModelList(privacyTemplateRepository.findAll());
	}


	@Override
	public List<PrivacyTemplate> findEntityAll() {
		// TODO Auto-generated method stub
		return privacyTemplateRepository.findAll();
	}


	@Override
	public ViewModelPage<PrivacyTemplateViewModel> getPrivacyToDataTable(Date dateCreate, int firstElementIndex, int pageSize,
			String sortField, SortOrder sortOrder, Map<TableField, Object> filters) {
		
		final Page<PrivacyTemplate> privacyTemplatePage;

        final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        try {
        	privacyTemplatePage = privacyTemplateRepository.findAll(
                    predicateHelper.getPredicateForListPrivacy(dateCreate)
                            .and(queryDSLHelper.createFilterPredicate(filters)),
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

            return new ViewModelPageImpl<>(privacyTemplateViewModelCreator.createViewModelList(privacyTemplatePage.getContent()),
            		privacyTemplatePage.getTotalPages(), privacyTemplatePage.getTotalElements());
        } catch (final Exception e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }
        
	}


	@Override
	public PrivacyTemplate findOneByProvenienza(String provenienza) {
		// TODO Auto-generated method stub
				return privacyTemplateRepository.findByProvenienza(provenienza);
	}

}
