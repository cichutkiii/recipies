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
import java.util.List;
import pl.preclaw.recipies.R;
import pl.preclaw.recipies.importData.Step;

import static android.support.constraint.Constraints.TAG;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {


//    final private StepAdapter.ListItemClickListener mOnClickListener;

    private List<Step> stepList;

    private int test = 0;

    public class StepViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView stepShortDescription;
        private ImageView stepImage;
        private TextView tv;
        StepViewHolder(View view) {
            super(view);
            stepShortDescription = view.findViewById(R.id.step_short_descr);
            stepImage = view.findViewById(R.id.step_image);
            tv = view.findViewById(R.id.textView);
            view.setOnClickListener(this);
        }

//        void Bind(int listIndex) {
//
//        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
//            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public StepAdapter(List<Step> steps) {
        stepList= steps;
//        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        Log.d(TAG, "onCreateViewHolder called" + test++);

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
        Log.d(TAG, "Position" + position);
        holder.stepShortDescription.setText(stepList.get(position).getShortDescription());
        holder.tv.setText(String.valueOf(position));

        if(!stepList.get(position).getThumbnailURL().isEmpty()){
            Picasso.get()
                    .load(stepList.get(position).getThumbnailURL())
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.error_image)
                    .into(holder.stepImage);
        }
//        holder.Bind(position);


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

