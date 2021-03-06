package com.daironiq.recipeapp.controllers;

import com.daironiq.recipeapp.commands.RecipeCommand;
import com.daironiq.recipeapp.domain.Recipe;
import com.daironiq.recipeapp.exceptions.NotFoundException;
import com.daironiq.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

//import static org.springframework.test.web.servlet.MockMvc.perform;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    MockMvc mockMvc;

    RecipeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        controller=new RecipeController(recipeService);
        mockMvc=MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void testGetRecipeNotFound()throws Exception{

        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void testGetRecipeNumberFormatException()throws Exception{

       // when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/asd/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void testGetRecipe() throws Exception{

        Recipe recipe=new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testGetNewRecipeForm() throws Exception{
        RecipeCommand command=new RecipeCommand();

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception{
        RecipeCommand command=new RecipeCommand();
        command.setId(2L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","")
                .param("description","some string")
                .param("directions","some direction")
            ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));

    }

    @Test
    public void testPostNewRecipeFormValidationFail()throws Exception{
        RecipeCommand command=new RecipeCommand();
        command.setId(2L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","")
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));

    }

    @Test
    public void testGetUpdateView() throws Exception{
        RecipeCommand command=new RecipeCommand();
        command.setId(2L);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testDeleteAction() throws Exception{
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService,times(1)).deleteById(anyLong());
    }

}
