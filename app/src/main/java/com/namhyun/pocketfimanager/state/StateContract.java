package com.namhyun.pocketfimanager.state;

import com.namhyun.pocketfimanager.BasePresenter;
import com.namhyun.pocketfimanager.BaseView;

public interface StateContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
