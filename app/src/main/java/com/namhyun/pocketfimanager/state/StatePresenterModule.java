package com.namhyun.pocketfimanager.state;

import dagger.Module;
import dagger.Provides;

@Module
public class StatePresenterModule {
    private final StateContract.View mView;

    public StatePresenterModule(StateContract.View view) {
        this.mView = view;
    }

    @Provides
    StateContract.View providesView() {
        return mView;
    }
}
