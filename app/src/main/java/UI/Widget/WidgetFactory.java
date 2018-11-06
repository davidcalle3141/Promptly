package UI.Widget;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Data.Database.AppDatabase;
import Data.Database.Prompt;
import calle.david.promptly.R;

public class WidgetFactory implements WidgetService.RemoteViewsFactory {
    private final Context context;
    private final AppDatabase appDatabase;
    private List<String> promptList;

    WidgetFactory(Context context) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context,
                AppDatabase.class,
                "PromptDB").build();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        promptList = new ArrayList<>();
        List<Prompt> tempIngredients = appDatabase.promptDao().loadPromptSnapshot();
        for (Prompt prompt : tempIngredients
                ) {
            promptList.add(
                    String.format(
                            Locale.getDefault(),
                            "%s  %s",
                            prompt.getName(),
                            prompt.getSavedDate()));

        }

    }


    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (promptList == null) return 0;
        return promptList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || promptList == null)
            return null;

        RemoteViews remoteViews =
                new RemoteViews(context.getPackageName(), R.layout.widget_list);
        remoteViews.setTextViewText(R.id.widget_saved_items, promptList.get(position));

        Intent fillIntent = new Intent();
        remoteViews.setOnClickFillInIntent(R.id.widget_saved_items, fillIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}