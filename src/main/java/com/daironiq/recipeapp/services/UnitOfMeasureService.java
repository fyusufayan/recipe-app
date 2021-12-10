package com.daironiq.recipeapp.services;

import com.daironiq.recipeapp.commands.UnitOfMeasureCommand;
import com.daironiq.recipeapp.domain.UnitOfMeasure;

import java.util.Set;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> listAllUoms();
}
