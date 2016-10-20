package com.namhyun.pocketfimanager.state;

import com.namhyun.pocketfimanager.data.PocketFiServiceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PocketFiServiceModule.class, StatePresenterModule.class})
public interface StateComponent {
    void inject(StateActivity activity);
}
