package com.wilson.demo.presenter;

import com.wilson.panellibrary.base.presenter.AbstractRxPresenter;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends AbstractRxPresenter<MainPresenter.ViewModel> {

    public interface ViewModel {
        void callback(String result);

        void callbackCalc(String result);
    }

    @Override
    public void start() {
        addSubscription(Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext("123");
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> viewModel().callback(s)));
    }


    public void calc() {
        viewModel().callbackCalc("calc");
    }
}
