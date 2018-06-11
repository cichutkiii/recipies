package pl.preclaw.recipies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import pl.preclaw.recipies.R;
import pl.preclaw.recipies.importData.Recipy;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    final private IngredientAdapter.ListItemClickListener mOnClickListener;


    TextView recipeIngredient;
    private Recipy recipy;
    private static int viewHolderCount;
    private Context context;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public IngredientAdapter(Recipy recipe, IngredientAdapter.ListItemClickListener listener) {
        recipy = recipe;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = 0;
        try {
            layoutIdForListItem = R.layout.item_ingredient;

        } catch (Exception e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        IngredientAdapter.IngredientViewHolder viewHolder = new IngredientAdapter.IngredientViewHolder(view);
        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return recipy.getIngredients().size();
    }


    class IngredientViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {



        public IngredientViewHolder(View itemView) {
            super(itemView);
            recipeIngredient = itemView.findViewById(R.id.ingredient);

            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            String sumText = recipy.getIngredients().get(listIndex).getQuantity().toString()
                    + " "
                    + getUnit(recipy.getIngredients().get(listIndex).getMeasure())
                    + " of "
                    +recipy.getIngredients().get(listIndex).getIngredient();


         recipeIngredient.setText(sumText);

        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }
    private String getUnit(String shortUnit){
        Map<String, String> map = new HashMap<String, String>();
        map.put("CUP", "cup");
        map.put("TBLSP", "table spoon");
        map.put("TSP", "tea spoon");
        map.put("G", "gram");
        map.put("UNIT", "unit");
        map.put("OZ", "oz");


        return map.get(shortUnit);
    }
}

