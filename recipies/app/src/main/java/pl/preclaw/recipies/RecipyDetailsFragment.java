package pl.preclaw.recipies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.preclaw.recipies.importData.Ingredient;
import pl.preclaw.recipies.importData.Recipy;
import pl.preclaw.recipies.importData.RecipyList;
import pl.preclaw.recipies.utilities.IngredientAdapter;
import pl.preclaw.recipies.utilities.StepAdapter;
import pl.preclaw.recipies.widget.UpdateWidgetService;

import static java.lang.Boolean.FALSE;


public class RecipyDetailsFragment extends Fragment implements StepAdapter.ListItemClickListener{

    public static String RECIPE_DETAIL = "detail";
    public static String RECIPE_INDEX = "index";
    public static String RECIPE_BUNDLE = "bundle";
    @BindView(R.id.ingredients_rv)
    RecyclerView ingredientsRv;
    @BindView(R.id.recipe_details_title)
    TextView recipeDetailsTitle;
    Unbinder unbinder;
    IngredientAdapter ingredientAdapter;
    StepAdapter stepAdapter;
    @BindView(R.id.steps_rv)
    RecyclerView stepsRv;
    private Recipy recipy;
    OnStepClickListener mCallback;

    @Override
    public void onListItemClick(int clickedItemIndex) {
//        Toast.makeText(getContext(),String.valueOf(clickedItemIndex),Toast.LENGTH_LONG).show();
        mCallback.onStepSelected(clickedItemIndex);
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipy_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Bundle bundle = getActivity().getIntent().getExtras();
        RecipyList recipyList = new RecipyList();
        if (bundle != null) {
            recipyList.setRecipies(bundle.<Recipy>getParcelableArrayList(RECIPE_DETAIL));
            recipy = recipyList.getRecipies().get(bundle.getInt(RECIPE_INDEX));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        ArrayList<String> widgetIngredients= new ArrayList<>();
       List<Ingredient> ingredients = recipy.getIngredients();
        for(int i=0;i<ingredients.size();i++){
            String sumText = recipy.getIngredients().get(i).getQuantity().toString()
                    + " "
                    + getUnit(recipy.getIngredients().get(i).getMeasure())
                    + " of "
                    +recipy.getIngredients().get(i).getIngredient();
            widgetIngredients.add(sumText);
        }

        ingredientAdapter = new IngredientAdapter(recipy);
        stepAdapter = new StepAdapter(recipy.getSteps(),this);
        ingredientsRv.setLayoutManager(linearLayoutManager);
        ingredientsRv.setAdapter(ingredientAdapter);
        stepsRv.setNestedScrollingEnabled(false);
        ingredientsRv.setNestedScrollingEnabled(false);
        stepsRv.setHasFixedSize(true);
        stepsRv.setLayoutManager(stepLayoutManager);
        stepsRv.setAdapter(stepAdapter);
        UpdateWidgetService.startBakingService(getContext(),widgetIngredients);

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
