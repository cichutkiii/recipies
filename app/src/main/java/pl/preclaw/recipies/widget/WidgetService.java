package pl.preclaw.recipies.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import pl.preclaw.recipies.R;

import static pl.preclaw.recipies.widget.BakingWidget.ingredientsList;

public class WidgetService extends RemoteViewsService {

    List<String> remoteIngredientsList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRVFactory(this.getApplicationContext(),intent);
    }
    class GridRVFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext = null;

        public GridRVFactory(Context context, Intent intent) {
            mContext = context;

        }
        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            remoteIngredientsList = ingredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {

            return remoteIngredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_view_item);

            views.setTextViewText(R.id.widget_item, remoteIngredientsList.get(position));

            Intent fillInIntent = new Intent();
            //fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

            return views;
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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }




    }


}

