package UI.Fragments;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import Utils.InjectorUtils;
import ViewModel.PreviewDialogueViewModel;
import ViewModel.PreviewDialogueViewModelFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import calle.david.promptly.R;


public class PreviewDialogue extends Fragment {
    private static final int READ_REQUEST_CODE = 43;
    View mView;
    Context mContext;

    @BindView(R.id.prompt_dialog_preview_toolbar_title)TextView mToolbarTitle;
    @BindView(R.id.prompt_preview_text_view)TextView mPreviewText;
    @BindView(R.id.prompt_preview_start_button)Button mStartButton;
    @BindView(R.id.prompt_preview_choose_another_button)Button mChooseAnotherButton;

    private PreviewDialogueViewModel mViewModel;


    public PreviewDialogue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        mView =  inflater.inflate(R.layout.fragment_preview_dialogue, container, false);
        ButterKnife.bind(this,mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        PreviewDialogueViewModelFactory factory = InjectorUtils.providePreviewDialogueFactory(Objects.requireNonNull(getActivity()));
        mViewModel = ViewModelProviders.of(getActivity(),factory).get(PreviewDialogueViewModel.class);

        populateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);

    }

    private void populateUI() {
        mViewModel.getPrompt().observe(this,
                promptDetails ->{
            if(promptDetails!=null){
                mToolbarTitle.setText(promptDetails[0]);
                mPreviewText.setText(promptDetails[1]);
            }
                });
    }

    @OnClick(R.id.prompt_preview_choose_another_button)
    public void selectFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = null;
        String[] promptData = new String[2];
        if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            try {
                promptData[0] = getFileName(uri);
                promptData[0] = promptData[0].substring(0, promptData[0].lastIndexOf('.'));
                promptData[1] =  readTextFromUri(uri);
                mViewModel.setPromptData(promptData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
        if(inputStream!=null){
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            return stringBuilder.toString();}
        return null;
    }

    private String getFileName(Uri uri) {
        String returnName = null;
        Cursor returnCursor=
                Objects.requireNonNull(getActivity()).getContentResolver().query(uri,null,null,null,null);
        if(returnCursor!=null){
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            returnName= returnCursor.getString(nameIndex);
            returnCursor.close();}
        return returnName;
    }
}
