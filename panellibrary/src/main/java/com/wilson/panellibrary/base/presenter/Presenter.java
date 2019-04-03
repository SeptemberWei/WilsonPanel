package com.wilson.panellibrary.base.presenter;

public interface Presenter<T> {

    void setViewModel(T viewModel);

    void start();

    void stop();
}
