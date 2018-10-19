package UI.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.view.FABRevealMenu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import Utils.FragmentNavUtils;
import Utils.InjectorUtils;
import ViewModel.PromptsViewModel;
import ViewModel.PromptsViewModelFactory;
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

    private PromptsViewModel mSavedViewModel;
    private FragmentManager mFragmentManager;


    public SavedDialogue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentManager = getFragmentManager();
        mContext = getContext();
        mView =  inflater.inflate(R.layout.fragment_preview_dialogue, container, false);
        ButterKnife.bind(this,mView);
        mFabMenu.setMenu(R.menu.fab_menue_saved);
        mFabMenu.bindAnchorView(mFab);
        mFabMenu.setOnFABMenuSelectedListener(this);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PromptsViewModelFactory factory = InjectorUtils.provideSavedPromptsFactory(Objects.requireNonNull(getActivity()));
        mSavedViewModel = ViewModelProviders.of(getActivity(),factory).get(PromptsViewModel.class);

        populateUI();
    }

    private void populateUI() {
        mSavedViewModel.getFocusedPrompt().observe(this,
                focusedPrompt->{
                    if(focusedPrompt!= null){
                    String name = focusedPrompt.getName();
                    String fileContent = readFile(focusedPrompt.getPath());
                    mToolbarTitle.setText(name);
                    mPreviewText.setText(fileContent);
                    }
                });


    }

    private String readFile(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream in = Objects.requireNonNull(getContext()).openFileInput(path);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine())!= null){
                sb.append(line);
            }
            bufferedReader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return sb.toString();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);

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
        FragmentNavUtils.navigateToFragment(mFragmentManager,new TelePrompt(), R.id.fragment_container, "TELE_FRAG");
    }

    private void deleteFile() {
    }
}
