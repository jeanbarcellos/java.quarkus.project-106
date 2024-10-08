package br.com.cursoudemy.productapi.modules.category.services;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cursoudemy.productapi.config.SuccessResponse;
import br.com.cursoudemy.productapi.config.exception.ValidationException;
import br.com.cursoudemy.productapi.modules.category.dtos.CategoryRequest;
import br.com.cursoudemy.productapi.modules.category.dtos.CategoryResponse;
import br.com.cursoudemy.productapi.modules.category.models.Category;
import br.com.cursoudemy.productapi.modules.category.repositories.CategoryRepository;
import br.com.cursoudemy.productapi.modules.product.services.ProductService;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    public List<CategoryResponse> findAll() {
        return categoryRepository
            .findAll()
            .stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());
    }

    public List<CategoryResponse> findByDescription(String description) {
        if (isEmpty(description)) {
            throw new ValidationException("The category description must be informed.");
        }

        return categoryRepository
                .findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public CategoryResponse findByIdResponse(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The category ID was not informed.");
        }

        return CategoryResponse.of(findById(id));
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given ID."));
    }

    public CategoryResponse save(CategoryRequest request) {
        validateCategoryNameInformed(request);

        var category = categoryRepository.save(Category.of(request));

        return CategoryResponse.of(category);
    }

    public CategoryResponse update(CategoryRequest request, Integer id) {
        validateCategoryNameInformed(request);
        validateInformedId(id);

        var category = Category.of(request);
        category.setId(id);
        categoryRepository.save(category);

        return CategoryResponse.of(category);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);

        if (productService.existsByCategoryId(id)) {
            throw new ValidationException("You cannot delete this category because it's already defined by a product.");
        }
        categoryRepository.deleteById(id);
        return SuccessResponse.create("The category was deleted.");
    }

    private void validateCategoryNameInformed(CategoryRequest request) {
        if (isEmpty(request.getDescription())) {
            throw new ValidationException("The category description was not informed.");
        }
    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The category ID must be informed.");
        }
    }
}
