package com.jeremiasAvero.app.category.application;

import com.jeremiasAvero.app.category.application.exception.CategoryNotFoundException;
import com.jeremiasAvero.app.category.domain.CategoryEntity;
import com.jeremiasAvero.app.category.domain.CategoryRepository;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo){
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<CategoryEntity> findAll(){
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public CategoryEntity findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }
    public CategoryEntity save(CategoryEntity category){
        return repo.save(category);
    }

    @Transactional
    public CategoryEntity update(Long id, CategoryEntity category) {
        CategoryEntity existing = repo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        existing.setName(category.getName());
        return repo.update(id, existing);
    }

    @Transactional
    public void deleteById(Long id) {
        if (repo.findById(id).isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        repo.deleteById(id);
    }
}
