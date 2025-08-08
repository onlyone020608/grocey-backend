package com.hyewon.grocey_api.domain.recipe.service;

import com.hyewon.grocey_api.domain.recipe.repository.SavedRecipeRepository;
import com.hyewon.grocey_api.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SavedRecipeCleaner {
    private final SavedRecipeRepository savedRecipeRepository;

    public void clean(User user){
        savedRecipeRepository.deleteByUser(user);
    }

}
