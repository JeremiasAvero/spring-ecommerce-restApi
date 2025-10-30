package com.jeremiasAvero.app.category.infraestructure.ports.out;

import com.jeremiasAvero.app.category.domain.CategoryEntity;
import com.jeremiasAvero.app.category.domain.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpringCategoryRepository implements CategoryRepository {
  private final JpaCategoryRepository repo;

  public SpringCategoryRepository(JpaCategoryRepository repo){
      this.repo = repo;
  }

    @Override
    public List<CategoryEntity> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<CategoryEntity> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public CategoryEntity save(CategoryEntity category) {
        return repo.save(category);
    }

    @Override
    public CategoryEntity update(Long id, CategoryEntity category) {
        category.setId(id);
        return repo.save(category);
    }
    @Override
    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }
    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
