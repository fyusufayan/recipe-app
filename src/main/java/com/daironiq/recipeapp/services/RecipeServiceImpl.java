package com.daironiq.recipeapp.services;

import com.daironiq.recipeapp.commands.RecipeCommand;
import com.daironiq.recipeapp.converters.RecipeCommandToRecipe;
import com.daironiq.recipeapp.converters.RecipeToRecipeCommand;
import com.daironiq.recipeapp.domain.Recipe;
import com.daironiq.recipeapp.exceptions.NotFoundException;
import com.daironiq.recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("I'm in the service");

        Set<Recipe> recipeSet=new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long l) {

        Optional<Recipe> recipeOptional=recipeRepository.findById(l);
            if (!recipeOptional.isPresent()){
                //throw new RuntimeException("Recipe Not Found!");
                throw new NotFoundException("Recipe Not Found. For Id value: "+l.toString());
            }
        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Long l) {
        return recipeToRecipeCommand.convert(findById(l));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe=recipeCommandToRecipe.convert(command);

        Recipe savedRecipe=recipeRepository.save(detachedRecipe);
        log.debug("Saved recipeId: "+savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public void deleteById(Long idToDelete) {
        recipeRepository.deleteById(idToDelete);
    }
}
