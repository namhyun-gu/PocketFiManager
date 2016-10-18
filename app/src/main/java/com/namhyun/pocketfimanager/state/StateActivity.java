package com.namhyun.pocketfimanager.state;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.namhyun.pocketfimanager.R;
import com.namhyun.pocketfimanager.data.DaggerServiceComponent;
import com.namhyun.pocketfimanager.data.PockFiService;
import com.namhyun.pocketfimanager.data.ServiceComponent;

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

public class StateActivity extends AppCompatActivity {
    @Inject PockFiService mPockFiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ServiceComponent component = DaggerServiceComponent.builder().build();
        component.inject(this);

        final TextView tvTest = (TextView) findViewById(R.id.tv_test);
        final EditText editPassword = (EditText) findViewById(R.id.edt_password);

        Button btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(view -> requestState(mPockFiService, tvTest, editPassword));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> mPockFiService.sendMobileSubmit(Credentials.basic("user", editPassword.getText().toString()), "pwof").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call12, Response<String> response) {
                Snackbar.make(view, response.body(), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show();
            }

            @Override
            public void onFailure(Call<String> call12, Throwable t) {
                t.printStackTrace();
                Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show();
            }
        }));

        requestState(mPockFiService, tvTest, editPassword);
    }

    private void requestState(PockFiService service, TextView textView, EditText editText) {
        Observable<String> stateObservable = service.getState(Credentials.basic("user", editText.getText().toString()));
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
                    textView.setText("");
                    textView.append("{\n");
                    for (String key : map.keySet()) {
                        String s = String.format("\"%s\": \"%s\",\n", key, map.get(key));
                        textView.append(s);
                    }
                    textView.append("}");
                }, throwable -> {
                    throwable.printStackTrace();
                    textView.setText(throwable.getMessage());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
