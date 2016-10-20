package com.namhyun.pocketfimanager.state;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.namhyun.pocketfimanager.R;
import com.namhyun.pocketfimanager.data.PocketFiServiceModule;
import com.namhyun.pocketfimanager.util.ActivityUtils;

import javax.inject.Inject;

public class StateActivity extends AppCompatActivity {

    @Inject StatePresenter mStatePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_activity);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the fragment.
        StateFragment stateFragment = StateFragment.newInstance();
        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(), stateFragment, R.id.contentFrame
        );

        DaggerStateComponent.builder()
                .pocketFiServiceModule(new PocketFiServiceModule("http://192.168.1.1"))
                .statePresenterModule(new StatePresenterModule(stateFragment)).build()
                .inject(this);
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
