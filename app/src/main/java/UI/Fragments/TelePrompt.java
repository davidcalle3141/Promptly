package UI.Fragments;


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
import android.widget.TextView;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import calle.david.promptly.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TelePrompt extends Fragment {

    View mView;
    Context mContext;
    Activity mActivity;
    String text=null;
    LinkedList<String> hotWordLL;

    float sp;

    @BindView(R.id.tele_prompt_text_view)TextView mTextView;


    public TelePrompt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tele_prompt, container, false);
        mContext = getContext();
        mActivity = getActivity();
        ButterKnife.bind(this,mView);

        // Inflate the layout for this fragment
        return mView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        text = mActivity.getResources().getString(R.string.test_tele_string);
        hotWordLL = new LinkedList<>();
        SharedPreferences sharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
        sp = sharedPreferences.getFloat("PROMPTER_TEXT_SIZE",50);
        mTextView.setTextSize(sp);
        mTextView.setText(text);

        mTextView.post(() -> splitIntoLines());



    }

    private void splitIntoLines() {
        int numOfLines = mTextView.getLayout().getLineCount();
        String tempString = null;
        String tempStringAry[];
        for(int i = 0; i<numOfLines;i++){
        tempString = text.substring(mTextView.getLayout().getLineStart(i),mTextView.getLayout().getLineEnd(i));
        tempString = tempString.trim();
        tempStringAry = tempString.split("\\s+");
        hotWordLL.add(tempStringAry[tempStringAry.length-1]);
        }

    }


}
