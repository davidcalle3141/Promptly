package UI.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ViewModel.PreviewDialogueViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import calle.david.promptly.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedDialogue extends Fragment {
    private static final int READ_REQUEST_CODE = 44;
    private static final String LOG_TAG = "PREVIEW_DIALOGUE";
    View mView;
    Context mContext;

    @BindView(R.id.prompt_dialog_preview_toolbar_title)TextView mToolbarTitle;
    @BindView(R.id.prompt_preview_text_view)TextView mPreviewText;
    @BindView(R.id.prompt_preview_start_button)Button mStartButton;
    @BindView(R.id.prompt_preview_choose_another_button)Button mChooseAnotherButton;

    private PreviewDialogueViewModel mViewModel;

    public SavedDialogue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        mView =  inflater.inflate(R.layout.fragment_preview_dialogue, container, false);
        ButterKnife.bind(this,mView);
        return mView;
    }

}
