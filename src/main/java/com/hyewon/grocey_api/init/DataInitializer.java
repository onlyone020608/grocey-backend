package com.hyewon.grocey_api.init;

import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.IngredientRepository;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final IngredientRepository ingredientRepository;
    private final ProductRepository productRepository;
    @Override
    public void run(String... args) throws Exception {
        loadIngredients();
        loadProducts();
    }

    private void loadIngredients() throws Exception{
        if (ingredientRepository.count() > 0) return;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/ingredient.csv").getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String name = tokens[0].trim();
                String imageUrl = tokens.length > 1 ? tokens[1].trim() : null;

                Ingredient ingredient = new Ingredient(name, imageUrl);
                ingredientRepository.save(ingredient);
            }
        }
    }

    private void loadProducts() throws Exception{
        if (productRepository.count() > 0) return;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/product.csv").getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                String brandName = tokens[0].trim();
                String productName = tokens[1].trim();
                double price = Double.parseDouble(tokens[2].trim());
                String imageUrl = tokens.length > 3 ? tokens[3].trim() : null;
                Long ingredientId = tokens.length > 4 ? Long.parseLong(tokens[4].trim()) : null;

                Product product = new Product(brandName, productName, price, imageUrl);
                if (ingredientId != null) {
                    Ingredient ingredient = ingredientRepository.findById(ingredientId)
                            .orElseThrow(() -> new RuntimeException("Ingredient not found with ID: " + ingredientId));
                    product.assignIngredient(ingredient);
                }
                productRepository.save(product);
            }
        }
    }
}
