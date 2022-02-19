package com.woonders.lacemscommon.app.viewmodel.creator;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by emanuele on 09/01/17.
 */
public abstract class AbstractBaseViewModelCreator<M, VM> implements BaseViewModelCreator<M, VM> {

    @Override
    public List<M> createModelList(List<VM> viewModelList) {
        List<M> modelList = null;
        if (viewModelList != null) {
            modelList = new LinkedList<>();
            for (VM viewModel : viewModelList) {
                modelList.add(createModel(viewModel));
            }
        }
        return modelList;
    }

    @Override
    public List<M> createModelList(Iterable<M> modelIterable) {
        return createModelList(Lists.newArrayList(modelIterable));
    }

    @Override
    public List<VM> createViewModelList(List<M> modelList) {
        List<VM> viewModelList = null;
        if (modelList != null) {
            viewModelList = new LinkedList<>();
            for (M model : modelList) {
                viewModelList.add(createViewModel(model));
            }
        }
        return viewModelList;
    }

    @Override
    public List<VM> createViewModelList(Iterable<M> modelIterable) {
        return createViewModelList(Lists.newArrayList(modelIterable));
    }

    @Override
    public List<VM> fromSet(Set<M> modelSet) {
        if (modelSet != null) {
            return modelSet.stream().map(this::createViewModel).sorted().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Set<M> fromList(List<VM> viewModelList) {
        if (viewModelList != null) {
            return viewModelList.stream().map(this::createModel).collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return null;
    }

    @Override
    public List<VM> fromModelList(List<M> modelList) {
        if (modelList != null) {
            return modelList.stream().map(this::createViewModel).sorted().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<M> fromViewModelList(List<VM> viewModelList) {
        if (viewModelList != null) {
            return viewModelList.stream().map(this::createModel).collect(Collectors.toList());
        }
        return null;
    }

    protected String trimIfNotNull(String string) {
        if (!StringUtils.isEmpty(string)) {
            return string.trim();
        }
        return string;
    }
}
