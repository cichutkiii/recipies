package pl.preclaw.recipies.importData;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class Recipy implements Parcelable
{


    private Integer id;

    private String name;

    private List<Ingredient> ingredients = null;

    private List<Step> steps = null;

    private Integer servings;

    private String image;
    public final static Parcelable.Creator<Recipy> CREATOR = new Creator<Recipy>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Recipy createFromParcel(Parcel in) {
            return new Recipy(in);
        }

        public Recipy[] newArray(int size) {
            return (new Recipy[size]);
        }

    };

    protected Recipy(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.ingredients, (pl.preclaw.recipies.importData.Ingredient.class.getClassLoader()));
        in.readList(this.steps, (pl.preclaw.recipies.importData.Step.class.getClassLoader()));
        this.servings = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Recipy() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeValue(servings);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

}