package com.namhyun.pocketfimanager.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class PockFiServiceModule {
    @Provides
    @Singleton
    PockFiService providesService(Retrofit retrofit) {
        return retrofit.create(PockFiService.class);
    }
}
