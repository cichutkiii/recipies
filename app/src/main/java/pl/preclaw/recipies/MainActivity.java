package pl.preclaw.recipies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.preclaw.recipies.importData.RecipeInteface;
import pl.preclaw.recipies.importData.Recipy;
import pl.preclaw.recipies.importData.RecipyList;
import pl.preclaw.recipies.utilities.RecipyAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipyAdapter.ListItemClickListener{
    public static final String STATIC_RECIPIES_URL =
            "http://d17h27t6h515a5.cloudfront.net/topher/2017/May/";

    @BindView(R.id.recipies_rv)
    RecyclerView recipiesRv;

    private RecipyList recipies;
    private RecipyAdapter recipyAdapter;
    private GridLayoutManager layoutManager;
    public static String RECIPE_DETAIL= "detail";
    public static String RECIPE_INDEX= "index";
    public static String RECIPE_BUNDLE= "bundle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recipies = new RecipyList();
        layoutManager = new GridLayoutManager(this,numberOfColumns());
        recipiesRv.setLayoutManager(layoutManager);
        getRecipiesData();


    }


    private void getRecipiesData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(STATIC_RECIPIES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeInteface myInterface = retrofit.create(RecipeInteface.class);
        Call<ArrayList<Recipy>> call = myInterface.getRecipiesData();

        call.enqueue(new Callback<ArrayList<Recipy>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipy>> call, Response<ArrayList<Recipy>> response) {
                if (response.isSuccessful()) {
                    recipies.setRecipies(response.body());

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        Log.d("TEST", jObjError.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("TEST", e.getMessage());
                    }
                }
                loadRecipeData();
            }

            @Override
            public void onFailure(Call<ArrayList<Recipy>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private void loadRecipeData() {
        recipyAdapter = new RecipyAdapter(recipies,this);
        recipiesRv.setAdapter(recipyAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this,RecipeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(RECIPE_DETAIL, recipies.getRecipies());
        bundle.putInt(RECIPE_INDEX,clickedItemIndex);


        intent.putExtra(RECIPE_BUNDLE,bundle);
        startActivity(intent);


    }
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 1) return 1; //to keep the grid aspect
        return nColumns;
    }
}
