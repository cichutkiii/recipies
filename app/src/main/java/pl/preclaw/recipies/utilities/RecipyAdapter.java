package pl.preclaw.recipies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import pl.preclaw.recipies.R;
import pl.preclaw.recipies.importData.RecipyList;

public class RecipyAdapter extends RecyclerView.Adapter<RecipyAdapter.RecipyViewHolder> {
    final private ListItemClickListener mOnClickListener;

    ImageView recipeImage;
    TextView recipeServings;
    TextView recipeTitle;
    private RecipyList recipyList;
    private static int viewHolderCount;
    private Context context;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public RecipyAdapter(RecipyList recipies, ListItemClickListener listener) {
        recipyList = recipies;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    @NonNull
    @Override
    public RecipyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = 0;
        try {
            layoutIdForListItem = R.layout.item_recipy;

        } catch (Exception e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        RecipyViewHolder viewHolder = new RecipyViewHolder(view);
        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipyViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return recipyList.getRecipies().size();
    }


    class RecipyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {



        public RecipyViewHolder(View itemView) {
            super(itemView);
            recipeServings = itemView.findViewById(R.id.recipe_servings);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            recipeTitle.setText(recipyList.getRecipies().get(listIndex).getName());
            Log.d("TAG", recipyList.getRecipies().get(listIndex).getName());
            recipeServings.setText(recipyList.getRecipies().get(listIndex).getServings().toString());
            if(!recipyList.getRecipies().get(listIndex).getImage().isEmpty()) {
                Picasso.get()
                        .load(recipyList.getRecipies().get(listIndex).getImage())
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(recipeImage);
            }

        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }
}

