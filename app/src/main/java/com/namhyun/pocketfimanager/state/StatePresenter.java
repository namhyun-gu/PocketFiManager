package com.namhyun.pocketfimanager.state;

import com.namhyun.pocketfimanager.data.PocketFiService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StatePresenter implements StateContract.Presenter {

    private final PocketFiService mPocketFiService;

    private final StateContract.View mStateView;

    @Inject
    StatePresenter(PocketFiService pocketFiService, StateContract.View stateView) {
        this.mPocketFiService = pocketFiService;
        this.mStateView = stateView;
    }

    @Inject
    void setupListeners() {
        mStateView.setPresenter(this);
    }

    @Override
    public void start() {
        loadState(null);
    }

    @Override
    public void loadState(String password) {
        String credentials = Credentials.basic("user", password);
        Observable<String> stateObservable = mPocketFiService.getState(credentials);
        stateObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    Document document = Jsoup.parse(s);
                    Element scriptElement = document.getElementsByTag("script").first();

                    String[] htmlLineStrings = scriptElement.html().split("\n");
                    HashMap<String, String> variableMap = new HashMap<>();

                    final int FIRST_VARIABLE_LINE = 2;
                    final int LAST_VARIABLE_LINE = htmlLineStrings.length - 2;
                    for (int index = FIRST_VARIABLE_LINE; index < LAST_VARIABLE_LINE; index++) {
                        String line = htmlLineStrings[index].replaceAll("\\s", "");
                        String key = line.substring(line.indexOf("=") + 1, line.length());
                        String value = line.substring(1, line.lastIndexOf("\""));
                        variableMap.put(key, value);
                    }

                    return variableMap;
                })
                .subscribe(map -> {
                    final String NEW_LINE = System.getProperty("line.separator");

                    StringBuilder builder = new StringBuilder();
                    builder.append("{").append(NEW_LINE);
                    for (String key : map.keySet()) {
                        String s = String.format("\"%s\": \"%s\",\n", key, map.get(key));
                        builder.append(s);
                    }
                    builder.append("}");

                    mStateView.showState(builder.toString());
                }, throwable -> {
                    mStateView.showState(throwable.getMessage());
                });
    }

    @Override
    public void sendDeviceOffSignal(String password) {
        String credentials = Credentials.basic("user", password);
        mPocketFiService.sendMobileSubmit(credentials, "pwof").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mStateView.showPostResponse(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mStateView.showPostResponse(t.getMessage());
            }
        });
    }
}
