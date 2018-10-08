package UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import Data.Database.Prompt;
import calle.david.promptly.R;

public class SavedPromptsAdapter extends RecyclerView.Adapter<SavedPromptsVH> {
    private final Context mContext;
    private final SavedPromptOnClickListener onClickListener;

    private List<Prompt> mPromptList;

    public SavedPromptsAdapter(Context context, SavedPromptOnClickListener onClickListener){
        this.onClickListener = onClickListener;
        this.mContext = context;
        mPromptList = new ArrayList<>();
    }

    public void addPromptList(List<Prompt> promptList){
        this.mPromptList.clear();
        this.mPromptList.addAll(promptList);
    }


    public Prompt getPrompt(int position){
        return mPromptList.get(position);
    }

    @Override
    public int getItemCount() {
        return mPromptList.size();
    }

    @NonNull
    @Override
    public SavedPromptsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_saved_prompts_row,viewGroup,false);
        return new SavedPromptsVH(view,onClickListener);
    }
    @Override
    public void onBindViewHolder(@NonNull SavedPromptsVH savedPromptsVH, int i) {
        String name = mPromptList.get(i).getName();
        String date = mPromptList.get(i).getSavedDate();
        int id = mPromptList.get(i).getId();

        savedPromptsVH.mTitleTV.setText(name);
        savedPromptsVH.mDateTV.setText(date);
        savedPromptsVH.id = id;

        if(i==0)savedPromptsVH.mHline.setVisibility(View.GONE);
    }

    public interface SavedPromptOnClickListener{
        void onItemClick(int position, int id);
    }
}
