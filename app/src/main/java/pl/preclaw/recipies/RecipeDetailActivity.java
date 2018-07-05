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

        if(findViewById(R.id.step_linear_layout)!= null){
            mTwoPane = true;


            if(savedInstanceState == null) {
                // In two-pane mode, add initial BodyPartFragments to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Creating a new head fragment
//                BodyPartFragment headFragment = new BodyPartFragment();
//                headFragment.setImageIds(AndroidImageAssets.getHeads());
                // Add the fragment to its container using a transaction
//                fragmentManager.beginTransaction()
//                        .add(R.id.head_container, headFragment)
//                        .commit();


            }
        } else {

            mTwoPane = false;
        }
    }

    @Override
    public void onStepSelected(int position) {
//        Toast.makeText(this,"aaaa" + String.valueOf(position),Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getApplicationContext(), StepDetailActivity.class);
        bundle.putInt(STEP_INDEX,position);
        bundle.putParcelableArrayList(STEP_BUNDLE, steps);
        intent.putExtra("REAL",bundle);
        startActivity(intent);
//        intent.putExtra(STEP_INDEX,position);
//        intent.putParcelableArrayListExtra(STEP_BUNDLE,steps);


    }
}
