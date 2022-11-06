package explorewithme.ewm.events.service;

import explorewithme.ewm.events.dto.CategoryDto;
import explorewithme.ewm.events.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto createCategory(NewCategoryDto categoryDto);

    void deleteCategory(long catId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(int catId);

}
