package com.namhyun.pocketfimanager.state;

import javax.inject.Inject;

public class StatePresenter implements StateContract.Presenter {
    private final StateContract.View mStateView;

    @Inject
    StatePresenter(StateContract.View mStateView) {
        this.mStateView = mStateView;
    }

    @Inject
    void setupListeners() {
        mStateView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
