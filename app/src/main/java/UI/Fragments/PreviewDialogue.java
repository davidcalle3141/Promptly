package UI.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import Data.Database.Prompt;
import UI.Widget.HomeScreenWidget;
import Utils.FragmentNavUtils;
import ViewModel.PromptsViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import calle.david.promptly.R;


public class PreviewDialogue extends Fragment implements OnFABMenuSelectedListener{
    private static final int READ_REQUEST_CODE = 43;
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


    public PreviewDialogue() {
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
        mFabMenu.setMenu(R.menu.fab_menu_selected);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);

    }

    private void populateUI() {
        mViewModel.getPromptDetails().removeObservers(this);

        mViewModel.getPromptDetails().observe(this,
                promptDetails ->{
            if(promptDetails!=null){
                mToolbarTitle.setText(promptDetails[0].substring(0, promptDetails[0].lastIndexOf('.')));
                mPreviewText.setText(promptDetails[1]);
            }
                });
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


    @OnClick(R.id.toolbar_back_arrow)
    public void goBack(){
        FragmentNavUtils.pop(mFragmentManager);
    }

    public void selectFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void saveButton(){

        updateWidget();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());
        mViewModel.getPromptDetails().observe(this, promptDetail -> {
            if(promptDetail!=null) {
                saveFile(promptDetail);
                Prompt prompt = new Prompt(promptDetail[0].substring(0, promptDetail[0].lastIndexOf('.')),
                        promptDetail[0], date);

                mViewModel.savePrompt(prompt);
            }
        });
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(mContext, HomeScreenWidget.class)
        );
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widget_saved_list_items);
        HomeScreenWidget.updateAppWidget(mContext, appWidgetManager, widgetIds);
    }

    private void saveFile(String[] promptDetail) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = mContext.openFileOutput(promptDetail[0], Context.MODE_PRIVATE);
            fileOutputStream.write(promptDetail[1].getBytes());
            fileOutputStream.close();
        }catch (Exception e) {
            Log.d(LOG_TAG,"save file failed");
            e.printStackTrace();
        }

    }


    @Override
    public void onMenuItemSelected(View view, int id) {
        switch (id){
            case R.id.change:
                selectFile();
                break;
            case R.id.save:
                saveButton();
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



        FragmentNavUtils.navigateToFragment(mFragmentManager, new TelePrompt(), R.id.fragment_container, "TELE_FRAG");

    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);

    }
}
