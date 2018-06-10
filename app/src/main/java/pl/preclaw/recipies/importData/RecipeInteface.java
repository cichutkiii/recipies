package pl.preclaw.recipies.importData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeInteface {


    @GET("59121517_baking/baking.json")
    Call<ArrayList<Recipy>> getRecipiesData();

}
