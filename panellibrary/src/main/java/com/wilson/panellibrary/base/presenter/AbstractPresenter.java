package com.wilson.panellibrary.base.presenter;

public abstract class AbstractPresenter<T> implements Presenter<T> {

    private T mViewModel;

    @Override
    public void setViewModel(T viewModel) {
        this.mViewModel = viewModel;
    }

    public T viewModel(){
        return mViewModel;
    }

    @Override
    public void stop() {
        mViewModel = null;
    }
}
