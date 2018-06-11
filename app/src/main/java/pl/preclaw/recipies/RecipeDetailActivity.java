package pl.preclaw.recipies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeDetailActivity extends AppCompatActivity implements RecipyDetailsFragment.OnStepClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
    }

    @Override
    public void onStepSelected(int position) {

    }
}
