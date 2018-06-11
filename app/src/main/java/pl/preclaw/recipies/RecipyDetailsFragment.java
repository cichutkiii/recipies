package pl.preclaw.recipies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.preclaw.recipies.importData.Recipy;
import pl.preclaw.recipies.importData.RecipyList;
import pl.preclaw.recipies.utilities.IngredientAdapter;


public class RecipyDetailsFragment extends Fragment implements IngredientAdapter.ListItemClickListener{

    public static String RECIPE_DETAIL= "detail";
    public static String RECIPE_INDEX= "index";
    public static String RECIPE_BUNDLE= "bundle";
    @BindView(R.id.ingredients_rv)
    RecyclerView ingredientsRv;
    @BindView(R.id.recipe_details_title)
    TextView recipeDetailsTitle;
    Unbinder unbinder;
    IngredientAdapter ingredientAdapter;
    private RecipyList recipyList;
    private Recipy recipy;
    OnStepClickListener mCallback;

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }
    public RecipyDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            Log.d("TAG", "not null");
            for (String key : bundle.keySet()) {
                Log.d("TAG", key + " => " + bundle.get(key) + ";");
            }
            int index= bundle.getBundle(RECIPE_BUNDLE).getInt(RECIPE_INDEX);
            recipeDetailsTitle.setText(String.valueOf(index));
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipy_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
//        if (getArguments() != null) {
//            recipy = getArguments().getBundle(RECIPE_BUNDLE).getParcelable(RECIPE_BUNDLE);
//        }
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        ingredientAdapter = new IngredientAdapter(recipy, this);
//        ingredientsRv.setLayoutManager(linearLayoutManager);
//        ingredientsRv.setAdapter(ingredientAdapter);


        return rootView;
    }








    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



}
