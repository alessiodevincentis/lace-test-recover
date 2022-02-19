package com.woonders.lacemscommon.app.viewmodel.creator;

import java.util.List;
import java.util.Set;

/**
 * Created by emanuele on 09/01/17.
 */
public interface BaseViewModelCreator<M, VM> {

    M createModel(final VM viewModel);

    VM createViewModel(final M model);

    List<M> createModelList(final List<VM> viewModelList);

    List<M> createModelList(Iterable<M> modelIterable);

    List<VM> createViewModelList(final List<M> modelList);

    List<VM> createViewModelList(Iterable<M> modelIterable);

    List<VM> fromSet(Set<M> modelSet);

    Set<M> fromList(List<VM> viewModelList);

    List<VM> fromModelList(List<M> modelList);

    List<M> fromViewModelList(List<VM> viewModelList);

}
