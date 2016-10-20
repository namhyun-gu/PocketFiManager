package com.namhyun.pocketfimanager.state;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.namhyun.pocketfimanager.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class StateFragment extends Fragment implements StateContract.View {

    private StateContract.Presenter mPresenter;

    private TextView mTestView;

    private Button mRefreshButton;

    private EditText mPasswordEdit;

    private FloatingActionButton mFab;

    public StateFragment() {
        // Empty constructor
    }

    public static StateFragment newInstance() {
        return new StateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull StateContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.state_fragment, container, false);

        mTestView = (TextView) rootView.findViewById(R.id.text_test);
        mPasswordEdit = (EditText) rootView.findViewById(R.id.edit_password);
        mRefreshButton = (Button) rootView.findViewById(R.id.btn_refresh);
        mRefreshButton.setOnClickListener(view -> {
            mPresenter.loadState(mPasswordEdit.getText().toString());
        });

        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setOnClickListener(view -> {
            mPresenter.sendDeviceOffSignal(mPasswordEdit.getText().toString());
        });
        return rootView;
    }

    @Override
    public void showState(String state) {
        mTestView.setText(state);
    }

    @Override
    public void showPostResponse(String response) {
        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
    }
}
