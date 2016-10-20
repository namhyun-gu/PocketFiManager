package com.namhyun.pocketfimanager.state;

import com.namhyun.pocketfimanager.BasePresenter;
import com.namhyun.pocketfimanager.BaseView;

public interface StateContract {
    interface View extends BaseView<Presenter> {
        void showState(String state);

        void showPostResponse(String response);
    }

    interface Presenter extends BasePresenter {
        void loadState(String password);

        void sendDeviceOffSignal(String password);
    }
}
