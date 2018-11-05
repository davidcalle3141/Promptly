package UI.Fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.toolbar_menu_button)
    ImageButton mMenuButton;
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
        mMenuButton.setVisibility(View.VISIBLE);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);

        mScrollView.post(this::populateUI);


    }


    @OnClick(R.id.toolbar_menu_button)
    public void menuButtonClick() {
        PopupMenu popupMenu = new PopupMenu(mContext, mMenuButton);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout:

                        FirebaseAuth.getInstance().signOut();
                        Intent i = mActivity.getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(mActivity.getBaseContext().getPackageName());
                        Objects.requireNonNull(i).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        return true;
                }
                return false;
            }
        });


        popupMenu.show();


    }


    private void populateUI() {

        int currentSize = (int) mTextView.getTextSize();
        float defaultValueSize = currentSize / getResources().getDisplayMetrics().scaledDensity;
        float sp = sharedPreferences.getFloat("PROMPTER_TEXT_SIZE", defaultValueSize);


        mTextView.setTextSize(sp);

        ObjectAnimator textSizeAnimator =
                ObjectAnimator.ofInt(mSeekBar, "progress", (int) sp - 30);
        textSizeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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

        int defaultValueScrollSpeed = 50;
        int scrollSpeed = sharedPreferences.getInt("PROMPTER_SCROLL_SPEED", defaultValueScrollSpeed);


        ObjectAnimator scrollSpeedAnimator =
                ObjectAnimator.ofInt(mSeekBarScrollSpeed, "progress", 100 - scrollSpeed + 10);
        scrollSpeedAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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
        mAnimator.setDuration((long) ((mTextView.getLineCount() * ((scrollSpeed / 10) * 100)) * 2.5));
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
                startScrollAnimation((100 - i) + 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sharedPreferences.edit().putInt("PROMPTER_SCROLL_SPEED", (100 - seekBar.getProgress()) + 10).apply();
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
