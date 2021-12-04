package com.daironiq.recipeapp.controllers;

import com.daironiq.recipeapp.domain.Category;
import com.daironiq.recipeapp.domain.UnitOfMeasure;
import com.daironiq.recipeapp.repositories.CategoryRepository;
import com.daironiq.recipeapp.repositories.UnitOfMeasureRepository;
import com.daironiq.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
public class IndexController {

    //delete later
    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"","/","/index"})
    public String getIndexPage(Model model){
        log.debug("getting index page");

        model.addAttribute("recipes",recipeService.getRecipes());

        return "index";
    }

}
