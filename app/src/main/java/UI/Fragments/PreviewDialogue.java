package UI.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import calle.david.promptly.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewDialogue extends Fragment {


    public PreviewDialogue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview_dialogue, container, false);
    }

}
