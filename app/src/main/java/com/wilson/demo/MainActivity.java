package com.wilson.demo;

import android.widget.Button;
import android.widget.Toast;

import com.wilson.demo.presenter.MainPresenter;
import com.wilson.panellibrary.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainPresenter.ViewModel {
    private MainPresenter mPresenter;

    @BindView(R.id.helloBtn)
    Button helloBtn;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mPresenter = new MainPresenter();
    }

    @Override
    public void callback(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void callbackCalc(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.helloBtn)
    public void onClick() {
        mPresenter.calc();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }
}
