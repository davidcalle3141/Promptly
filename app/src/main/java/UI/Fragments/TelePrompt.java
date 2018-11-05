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
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ScrollView;
import android.widget.TextView;

import Utils.AppExecutors;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import calle.david.promptly.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TelePrompt extends Fragment {

    @BindView(R.id.tele_prompt_text_view)
    TextView mTextView;
    @BindView(R.id.tele_prompt_scroll_view)
    ScrollView mScrollview;

    private float sp;
    private View mView;
    private Context mContext;
    private Activity mActivity;
    private String text = null;
    private int SCREEN_SCROLL_STATUS = 0;
    private AppExecutors mExecutors;
    private ObjectAnimator animator;
    private SharedPreferences sharedPreferences;


    public TelePrompt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tele_prompt, container, false);
        mContext = getContext();
        mActivity = getActivity();
        ButterKnife.bind(this, mView);
        return mView;



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        text = mActivity.getResources().getString(R.string.test_tele_string);
        sharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
        sp = sharedPreferences.getFloat("PROMPTER_TEXT_SIZE", 50);
        mTextView.setTextSize(sp);
        mTextView.setText(text);
        mTextView.setMovementMethod(new ScrollingMovementMethod());
        mTextView.post(this::ScrollControl);
        mExecutors = AppExecutors.getInstance();


    }

    private void ScrollControl() {
        if (animator != null) animator.cancel();


        animator = ObjectAnimator.ofInt(mScrollview, "scrollY", mTextView.getBottom());
        animator.setDuration((long) ((mTextView.getLineCount() * ((sharedPreferences.getInt("PROMPTER_SCROLL_SPEED", 50) / 10) * 100)) * 2.5));

        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new Animator.AnimatorListener() {


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
        animator.addPauseListener(new Animator.AnimatorPauseListener() {

            @Override
            public void onAnimationPause(Animator animator) {
                ScrollControl();
            }

            @Override
            public void onAnimationResume(Animator animator) {

            }
        });


    }

    @OnClick(R.id.tele_prompt_text_view)
    public void clickScreen() {
        if (SCREEN_SCROLL_STATUS == 0) {
            SCREEN_SCROLL_STATUS = 1;
            startScroll();
        } else if (SCREEN_SCROLL_STATUS == 1) {
            SCREEN_SCROLL_STATUS = 0;
            pauseScroll();
        }
    }

    private void pauseScroll() {
        animator.pause();
    }

    private void startScroll() {
        if (animator.isPaused()) animator.resume();
        else if (!animator.isRunning()) animator.start();


    }


}
