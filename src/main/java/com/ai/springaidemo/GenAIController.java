package com.ai.springaidemo;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class GenAIController {
    private final ChatService chatService;
    private final ImageService imageService;
    private final RecipeService recipeService;

    public GenAIController(ChatService chatService, ImageService imageService, RecipeService recipeService) {
        this.chatService = chatService;
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("ask-ai")
    public String getResponse(@RequestParam String prompt){
        return chatService.getResponse(prompt);
    }

    @GetMapping("ask-ai-options")
    public String getResponseOptions(@RequestParam String prompt){
        return chatService.getResponseOptions(prompt);
    }

    // Disclaimer: If you want more than one image check under ImageService.java -n.
    // Below method is used if you are generating just one image as default.
    /*
    @GetMapping("generate-image")
    public void generateImage(HttpServletResponse response, @RequestParam String prompt) throws IOException {
        ImageResponse imageResponse = imageService.generateImage(prompt);
        String imageUrl = imageResponse.getResult().getOutput().getUrl();
        response.sendRedirect(imageUrl);
    }*/

    // Generates Images if you have more than one image to be generated.
    // We are storing the image URLs in the form of a list with the help of stream and map.

    // Parameterized Values
    @GetMapping("generate-image")
    public List<String> generateImage(HttpServletResponse response,
                                      @RequestParam String prompt,
                                      @RequestParam (defaultValue = "hd") String quality,
                                      @RequestParam (defaultValue = "1") int n,
                                      @RequestParam (defaultValue = "1024") int width,
                                      @RequestParam (defaultValue = "1024") int height) throws IOException {
        ImageResponse imageResponse = imageService.generateImage(prompt, quality, n, width, height);
        List<String> imageUrls = imageResponse.getResults().stream()
                .map(result -> result.getOutput().getUrl())
                .toList();
        return imageUrls;
    }

    // Recipe Controller - Recipe Service
    @GetMapping("recipe-creator")
    public String recipeCreator(@RequestParam String ingredients,
                                      @RequestParam (defaultValue = "any") String cuisine,
                                      @RequestParam (defaultValue = "") String dietaryRestriction) throws IOException {
        return recipeService.createRecipe(ingredients, cuisine, dietaryRestriction);
    }
}
