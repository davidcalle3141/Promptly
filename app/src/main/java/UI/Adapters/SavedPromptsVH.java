package UI.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import calle.david.promptly.R;

public class SavedPromptsVH extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.saved_prompt_row_title)TextView mTitleTV;
    @BindView(R.id.saved_prompt_row_date)TextView mDateTV;
    @BindView(R.id.saved_prompts_horizontal_line)View mHline;
    protected int id;

    private SavedPromptsAdapter.SavedPromptOnClickListener onItemClickListener;

    SavedPromptsVH(View view, SavedPromptsAdapter.SavedPromptOnClickListener listener){
        super(view);
        ButterKnife.bind(this,view);
        view.setOnClickListener(this);
        this.onItemClickListener = listener;

    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener!= null) onItemClickListener.onItemClick(getAdapterPosition(), id);
    }
}
