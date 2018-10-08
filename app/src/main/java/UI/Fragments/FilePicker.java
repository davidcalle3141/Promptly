package UI.Fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Objects;

import Utils.FragmentNavUtils;
import Utils.InjectorUtils;
import ViewModel.PreviewDialogueViewModel;
import ViewModel.PreviewDialogueViewModelFactory;
import ViewModel.SavedPromptsViewModel;
import ViewModel.SavedPromptsViewModelFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import calle.david.promptly.R;


public class FilePicker extends Fragment {
    View mView;
    Context mContext;
    FragmentManager mFragmentManager;
    PreviewDialogueViewModel mViewModel;

    private static final int READ_REQUEST_CODE = 42;
    //@BindView(R.id.file_picker_button)Button mFilePickerButton;


    public FilePicker() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_file_picker, container, false);
        mContext = getContext();
        ButterKnife.bind(this,mView);
        mFragmentManager=getFragmentManager();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PreviewDialogueViewModelFactory factory = InjectorUtils.providePreviewDialogueFactory(Objects.requireNonNull(getActivity()));
        mViewModel = ViewModelProviders.of(getActivity(),factory).get(PreviewDialogueViewModel.class);



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
                FragmentNavUtils.navigateToFragment(mFragmentManager,new PreviewDialogue(),R.id.fragment_container,"Preview Dialogue");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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


    @OnClick(R.id.file_picker_button)
    public void selectFile(){
        //TODO file preview Fragment, pass through viewmodel text file stuff
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, READ_REQUEST_CODE);

    }
}
