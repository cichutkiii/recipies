package pl.preclaw.recipies.importData;


import java.util.ArrayList;

public class RecipyList {

    private ArrayList<Recipy> recipies = new ArrayList<>();

    /**
     * @return The recipies
     */
    public ArrayList<Recipy> getRecipies() {
        return recipies;
    }

    /**
     * @param recipies The recipies
     */
    public void setRecipies(ArrayList<Recipy> recipies) {
        this.recipies = recipies;
    }
}
