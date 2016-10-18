package com.namhyun.pocketfimanager.data;

import com.namhyun.pocketfimanager.state.StateActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by namhyun on 2016-10-18.
 */

@Singleton
@Component(modules = {NetworkModule.class, PockFiServiceModule.class})
public interface ServiceComponent {
    void inject(StateActivity activity);
}
