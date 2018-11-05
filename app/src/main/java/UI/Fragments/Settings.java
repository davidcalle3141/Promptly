package UI.Fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import calle.david.promptly.R;


public class Settings extends Fragment {
    View mView;
    Context mContext;
    Activity mActivity;
    SharedPreferences sharedPreferences;
    @BindView(R.id.settings_scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.settings_seek_bar_speed)
    SeekBar mSeekBarScrollSpeed;
    @BindView(R.id.settings_seek_bar)SeekBar mSeekBar;
    @BindView(R.id.settings_text_view)TextView mTextView;
    private ObjectAnimator mAnimator;

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
        String text = getResources().getString(R.string.settings_text);
        mTextView.setText(text);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);

        mScrollView.post(this::populateUI);


    }

    private void populateUI() {

        int currentSize = (int) mTextView.getTextSize();
        float defaultValueSize = currentSize / getResources().getDisplayMetrics().scaledDensity;
        float sp = sharedPreferences.getFloat("PROMPTER_TEXT_SIZE", defaultValueSize);


        mTextView.setTextSize(sp);

        ObjectAnimator textSizeAnimator =
                ObjectAnimator.ofInt(mSeekBar, "progress", (int) sp - 30);
        textSizeAnimator.setInterpolator(new LinearInterpolator());
        textSizeAnimator.setDuration(1000);
        textSizeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                handleText();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        int defaultValueScrollSpeed = 5;
        int scrollSpeed = sharedPreferences.getInt("PROMPTER_SCROLL_SPEED", defaultValueScrollSpeed);

        ObjectAnimator scrollSpeedAnimator =
                ObjectAnimator.ofInt(mSeekBarScrollSpeed, "progress", scrollSpeed);
        scrollSpeedAnimator.setInterpolator(new LinearInterpolator());
        scrollSpeedAnimator.setDuration(1000);
        scrollSpeedAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                handleScroll();
                startScrollAnimation(scrollSpeed);
                startScroll();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        textSizeAnimator.start();
        scrollSpeedAnimator.start();


    }


    private void startScrollAnimation(int scrollSpeed) {

        mAnimator = ObjectAnimator.ofInt(mScrollView, "scrollY", mTextView.getBottom());
        mAnimator.setDuration(mTextView.getLineCount() * (scrollSpeed * 100));
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mAnimator.addPauseListener(new Animator.AnimatorPauseListener() {
            @Override
            public void onAnimationPause(Animator animator) {
                startScrollAnimation(scrollSpeed);
            }

            @Override
            public void onAnimationResume(Animator animator) {

            }
        });


    }

    private void handleScroll() {
        mSeekBarScrollSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                startScrollAnimation(i + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sharedPreferences.edit().putInt("PROMPTER_SCROLL_SPEED", seekBar.getProgress() + 1).apply();
                startScroll();
            }
        });
    }

    public void handleText() {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = i + 30;
                mTextView.setTextSize((float) i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mAnimator.end();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                startScroll();
                editor.putFloat("PROMPTER_TEXT_SIZE", progress + 30);
                editor.apply();

            }
        });

    }

    private void startScroll() {
        if (mAnimator.isPaused()) mAnimator.resume();
        else if (!mAnimator.isRunning()) mAnimator.start();


    }

}
