package com.wilson.demo.presenter;

import com.wilson.panellibrary.base.presenter.AbstractRxPresenter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends AbstractRxPresenter<MainPresenter.ViewModel> {

    public interface ViewModel {
        void callback(String result);
        void callbackCalc(String result);
    }

    @Override
    public void start() {
        addSubscription(Observable.create(e -> {
            e.onNext("test");
            e.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> viewModel().callback(o.toString())));
    }


    public void calc(){
        viewModel().callbackCalc("calc");
    }
}
