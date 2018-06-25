package pl.preclaw.recipies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import pl.preclaw.recipies.R;
import pl.preclaw.recipies.importData.Step;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
final private StepAdapter.ListItemClickListener mOnClickListener;


    private TextView stepShortDescription;
    private ImageView stepImage;
    private List<Step> stepList;

    public class StepViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        StepViewHolder(View view) {
            super(view);
            stepShortDescription = view.findViewById(R.id.step_short_descr);
            stepImage = view.findViewById(R.id.step_image);
            view.setOnClickListener(this);
        }

        void bind(int listIndex) {
            stepShortDescription.setText(stepList.get(listIndex).getShortDescription());
            if(!stepList.get(listIndex).getThumbnailURL().isEmpty()){
                Picasso.get()
                        .load(stepList.get(listIndex).getThumbnailURL())
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.error_image)
                        .into(stepImage);
            }
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public StepAdapter(List<Step> steps, StepAdapter.ListItemClickListener listener) {
        stepList= steps;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = 0;
        try {
            layoutIdForListItem = R.layout.item_step;

        } catch (Exception e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    @Override
    public long getItemId(int position) {
        return stepList.get(position).hashCode();
    }
}

