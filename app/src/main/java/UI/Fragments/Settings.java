package UI.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import calle.david.promptly.R;


public class Settings extends Fragment {
    View mView;
    Context mContext;
    Activity mActivity;

    @BindView(R.id.settings_seek_bar)SeekBar mSeekBar;
    @BindView(R.id.settings_text_view)TextView mTextView;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_settings, container, false);
        mContext = getContext();
        mActivity = getActivity();
        ButterKnife.bind(this,mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int currentSize = (int) mTextView.getTextSize();
        float defaultValue = currentSize / getResources().getDisplayMetrics().scaledDensity;
        SharedPreferences sharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
        float sp = sharedPreferences.getFloat("PROMPTER_TEXT_SIZE",defaultValue);
        mTextView.setTextSize(sp);

        mSeekBar.setProgress((int) (sp));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<15) i=15;
                mTextView.setTextSize((float) i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if(progress<15) progress = 15;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("PROMPTER_TEXT_SIZE", progress);
                editor.apply();

            }
        });
    }

}
