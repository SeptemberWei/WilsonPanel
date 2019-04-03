package com.wilson.panellibrary.base.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class AbstractRxPresenter<T> extends AbstractPresenter<T> {
    final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void stop() {
        super.stop();
        clearSubscription();
    }

    protected void addSubscription(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    protected void clearSubscription() {
        mCompositeDisposable.clear();
    }
}
