package UI.Fragments;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.view.FABRevealMenu;

import Utils.FragmentNavUtils;
import ViewModel.PromptsViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import calle.david.promptly.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedDialogue extends Fragment implements OnFABMenuSelectedListener {
    private static final int READ_REQUEST_CODE = 44;
    private static final String LOG_TAG = "PREVIEW_DIALOGUE";
    View mView;
    Context mContext;

    @BindView(R.id.prompt_dialog_preview_toolbar_title)TextView mToolbarTitle;
    @BindView(R.id.prompt_preview_text_view)TextView mPreviewText;
    @BindView(R.id.fab)FloatingActionButton mFab;
    @BindView(R.id.fabMenu)FABRevealMenu mFabMenu;

    private PromptsViewModel mViewModel;
    private FragmentManager mFragmentManager;
    private Activity mActivity;
    private InterstitialAd mInterstitialAd;


    public SavedDialogue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentManager = getFragmentManager();
        mContext = getContext();
        mActivity = getActivity();
        mView =  inflater.inflate(R.layout.fragment_preview_dialogue, container, false);
        ButterKnife.bind(this,mView);
        mFabMenu.setMenu(R.menu.fab_menue_saved);
        mFabMenu.bindAnchorView(mFab);
        mFabMenu.setOnFABMenuSelectedListener(this);

        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(PromptsViewModel.class);

        populateUI();
    }

    private void populateUI() {
        mViewModel.getPromptDetails().removeObservers(this);


        mViewModel.getPromptDetails().observe(this,
                promptDetails -> {
                    if (promptDetails != null) {
                        mToolbarTitle.setText(promptDetails[0].substring(0, promptDetails[0].lastIndexOf('.')));
                        mPreviewText.setText(promptDetails[1]);
                    }
                });



    }


    @OnClick(R.id.toolbar_back_arrow)
    public void goBack(){
        FragmentNavUtils.pop(mFragmentManager);
    }

    @Override
    public void onMenuItemSelected(View view, int id) {
        switch (id){
            case R.id.delete:
                deleteFile();
                break;
            case R.id.play:
                playPrompt();
                break;


        }
    }

    private void playPrompt() {
        mActivity.findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
        FragmentNavUtils.navigateToFragment(mFragmentManager,new TelePrompt(), R.id.fragment_container, "TELE_FRAG");
    }

    private void deleteFile() {
        mViewModel.deletePrompt();
        mFragmentManager.popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);

    }


}
