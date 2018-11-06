package UI.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import UI.Adapters.SavedPromptsAdapter;
import Utils.FragmentNavUtils;
import Utils.InjectorUtils;
import ViewModel.PromptsViewModel;
import ViewModel.PromptsViewModelFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import calle.david.promptly.R;

public class SavedPrompts extends Fragment implements SavedPromptsAdapter.SavedPromptOnClickListener {
    View mView;
    Context mContext;
    SavedPromptsAdapter mAdapter;

    @BindView(R.id.saved_prompts_RV)RecyclerView mRecyclerView;
    private PromptsViewModel mSavedViewModel;
    private FragmentManager mFragmentManager;

    public SavedPrompts() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_saved_prompts, container, false);
        mContext = getContext();
        mFragmentManager = getFragmentManager();
        ButterKnife.bind(this,mView);

        LinearLayoutManager  layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new SavedPromptsAdapter(mContext,this );
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return mView;    }

    @Override
    public void onItemClick(int position, int id) {
        String[] promptDetail = new String[2];
        promptDetail[0] = mAdapter.getPrompt(position).getPath();
        promptDetail[1] = readFile(mAdapter.getPrompt(position).getPath());

        mSavedViewModel.setPromptData(promptDetail);
        mSavedViewModel.setPromptID(mAdapter.getPrompt(position).getId());


        // mSavedViewModel.setFocusedPrompt(mAdapter.getPrompt(position));
            FragmentNavUtils.navigateToFragment(mFragmentManager,new SavedDialogue(),R.id.fragment_container,"Saved Dialogue");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PromptsViewModelFactory sFactory = InjectorUtils.provideSavedPromptsFactory(Objects.requireNonNull(getActivity()));
        mSavedViewModel = ViewModelProviders.of(getActivity(),sFactory).get(PromptsViewModel.class);

        populateUI();
    }

    private String readFile(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream in = Objects.requireNonNull(getContext()).openFileInput(path);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return sb.toString();

    }

    private void populateUI() {
        mSavedViewModel.getPromptList().observe(this,
                prompts->{
            if(prompts!= null){
                mAdapter.addPromptList(prompts);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
