package com.daironiq.recipeapp.bootstrap;

import com.daironiq.recipeapp.domain.*;
import com.daironiq.recipeapp.repositories.CategoryRepository;
import com.daironiq.recipeapp.repositories.RecipeRepository;
import com.daironiq.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
        log.debug("loading bootstrap data");
    }

    private List<Recipe> getRecipes(){

        List<Recipe> recipes=new ArrayList<>(2);

        //get UOMs
        Optional<UnitOfMeasure> eachUomOptional=unitOfMeasureRepository.findByDescription("Each");

        if (!eachUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM Not Found");
        }

        Optional<UnitOfMeasure> tableSpoonUomOptional=unitOfMeasureRepository.findByDescription("Tablespoon");

        if (!tableSpoonUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM Not Found");
        }

        Optional<UnitOfMeasure> teaSpoonUomOptional=unitOfMeasureRepository.findByDescription("Teaspoon");

        if (!teaSpoonUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM Not Found");
        }

        Optional<UnitOfMeasure> dashUomOptional=unitOfMeasureRepository.findByDescription("Dash");

        if (!dashUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM Not Found");
        }

        Optional<UnitOfMeasure> pintUomOptional=unitOfMeasureRepository.findByDescription("Pint");

        if (!pintUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM Not Found");
        }

        Optional<UnitOfMeasure> cupUomOptional=unitOfMeasureRepository.findByDescription("Cup");

        if (!cupUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM Not Found");
        }

        //get optionals
        UnitOfMeasure eachUom=eachUomOptional.get();
        UnitOfMeasure tableSpoonUom=tableSpoonUomOptional.get();
        UnitOfMeasure teaSpoonUom=teaSpoonUomOptional.get();
        UnitOfMeasure dashUom=dashUomOptional.get();
        UnitOfMeasure pintUom=pintUomOptional.get();
        UnitOfMeasure cupUom=cupUomOptional.get();

        //get categories
        Optional<Category> americanCategoryOptional=categoryRepository.findByDescription("American");

        if (!americanCategoryOptional.isPresent()){
            throw new RuntimeException("Expected UOM Not Found");
        }

        Optional<Category> mexicanCategoryOptional=categoryRepository.findByDescription("Mexican");

        if (!mexicanCategoryOptional.isPresent()){
            throw new RuntimeException("Expected UOM Not Found");
        }

        Category americanCategory=americanCategoryOptional.get();
        Category mexicanCategory=mexicanCategoryOptional.get();

        //Yummy Guac
        Recipe guacRecipe=new Recipe();
        guacRecipe.setDescription("Perfect Guacomole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("Cut the avocado:\n" +
                "Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon.");

        Notes guacNotes=new Notes();
        guacNotes.setRecipeNotes("The best guacamole keeps it simple: just ripe avocados, salt, a squeeze of lime, onions, chilis, cilantro, and some chopped tomato. Serve it as a dip at your next party or spoon it on top of tacos for an easy dinner upgrade.");
        guacRecipe.setNotes(guacNotes);

        guacRecipe.addIngredient(new Ingredient("ripe avocados",new BigDecimal(2),eachUom));
        guacRecipe.addIngredient(new Ingredient("kosher salt",new BigDecimal(.5),teaSpoonUom));
        guacRecipe.addIngredient(new Ingredient("fresh lime juice or lemon juice",new BigDecimal(2),tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("minced red onion",new BigDecimal(2),tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("cilantro",new BigDecimal(2),tableSpoonUom));

        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);

        guacRecipe.setUrl("www.simplyrecipes.com/recipes/perfect-guatemole");
        guacRecipe.setServings(4);
        guacRecipe.setSource("Simply Recipes");

        //add to recipe list
        recipes.add(guacRecipe);

        Recipe tacosRecipe=new Recipe();
        tacosRecipe.setDescription("Spicy grilled chicken taco");
        tacosRecipe.setCookTime(9);
        tacosRecipe.setPrepTime(20);
        tacosRecipe.setDifficulty(Difficulty.MODERATE);
        tacosRecipe.setDirections("Make the marinade and coat the chicken:\n" +
                "In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest.");

        Notes tacosNotes=new Notes();
        tacosNotes.setRecipeNotes("Spicy grilled chicken tacos! Quick marinade, then grill. Ready in about 30 minutes. Great for a quick weeknight dinner, backyard cookouts, and tailgate parties.");

        tacosRecipe.setNotes(tacosNotes);

        tacosRecipe.addIngredient(new Ingredient("ancho chill powder",new BigDecimal(2),tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("dried oregona",new BigDecimal(1),tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("sugar",new BigDecimal(1),tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("salt",new BigDecimal(6),tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("olive oil",new BigDecimal(2),tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("avocados",new BigDecimal(4),tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("pinaple",new BigDecimal(3),tableSpoonUom));

        tacosRecipe.getCategories().add(mexicanCategory);
        tacosRecipe.getCategories().add(americanCategory);

        recipes.add(tacosRecipe);
        return recipes;

    }

}
