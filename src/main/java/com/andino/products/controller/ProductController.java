package com.andino.products.controller;

import com.andino.products.model.Product;
import com.andino.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String showProducts(Model model) {
        List<Product> products = productRepository.findAll();
        
        // Calculate the total sum of prices
        double totalPrice = products.stream()
                                    .mapToDouble(p -> p.getPrice() != null ? p.getPrice() : 0.0)
                                    .sum();
                                    
        // List of categories for the form select
        List<String> categories = Arrays.asList("Beverages", "Snacks", "Dairy", "Candies", "Bakery");
        
        // Pass data to the view
        model.addAttribute("products", products);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("categories", categories);
        
        // Empty object for the form to populate
        model.addAttribute("product", new Product());
        
        return "products"; // Return the template name (products.html)
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Product> products = productRepository.findAll();
            double totalPrice = products.stream()
                                        .mapToDouble(p -> p.getPrice() != null ? p.getPrice() : 0.0)
                                        .sum();
            List<String> categories = Arrays.asList("Beverages", "Snacks", "Dairy", "Candies", "Bakery");
            
            model.addAttribute("products", products);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("categories", categories);
            
            return "products";
        }
        // Save the product in MongoDB
        productRepository.save(product);
        // Redirect to the main controller (GET /)
        return "redirect:/";
    }
}
