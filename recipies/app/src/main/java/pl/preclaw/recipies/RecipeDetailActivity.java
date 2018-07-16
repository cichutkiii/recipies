package pl.preclaw.recipies;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.preclaw.recipies.importData.Recipy;
import pl.preclaw.recipies.importData.RecipyList;
import pl.preclaw.recipies.importData.Step;

import static pl.preclaw.recipies.MainActivity.RECIPE_INDEX;
import static pl.preclaw.recipies.RecipyDetailsFragment.RECIPE_DETAIL;

public class RecipeDetailActivity extends AppCompatActivity implements RecipyDetailsFragment.OnStepClickListener{
    private Recipy recipy;
    private ArrayList<Step> steps;
    private int recipeIndex;
    public static String STEP_INDEX= "index";
    public static String STEP_BUNDLE= "bundle";
    public static String PANES= "panes";

    FragmentManager fragmentManager;
private StepDetailFragment stepDetailFragment;
    private boolean mTwoPane;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);



        Bundle bundle = getIntent().getExtras();
        RecipyList recipyList = new RecipyList();
        if (bundle != null) {
            recipeIndex = bundle.getInt(RECIPE_INDEX);
            recipyList.setRecipies(bundle.<Recipy>getParcelableArrayList(RECIPE_DETAIL));
            recipy = recipyList.getRecipies().get(recipeIndex);
            steps = (ArrayList<Step>) recipy.getSteps();
        }
        if(savedInstanceState ==null){
            if(findViewById(R.id.step_linear_layout)!= null){
            mTwoPane = true;
            fragmentManager = getSupportFragmentManager();
            stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setSteps(steps);
            stepDetailFragment.setStepIndex(0);
            fragmentManager.beginTransaction()
                .add(R.id.step_container, stepDetailFragment)
                .addToBackStack(null)
                .commit();

            } else {
                mTwoPane = false;
            }
        } else{
            if(findViewById(R.id.step_linear_layout)!= null) {
                mTwoPane = true;
                stepDetailFragment = new StepDetailFragment();


            }
            else {
                mTwoPane = false;
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PANES,mTwoPane);

    }

    @Override
    public void onStepSelected(int position) {
        if(!mTwoPane){
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getApplicationContext(), StepDetailActivity.class);
            bundle.putInt(STEP_INDEX,position);
            bundle.putParcelableArrayList(STEP_BUNDLE, steps);
            intent.putExtra("REAL",bundle);
            startActivity(intent);

        }else {
            StepDetailFragment newFragment = new StepDetailFragment();
            stepDetailFragment.releasePlayer();
            newFragment.releasePlayer();

            newFragment.setStepIndex(position);
            newFragment.setSteps(steps);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container,newFragment)
                    .addToBackStack(null)
                    .commit();
        }



    }
}
