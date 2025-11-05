package com.jeremiasAvero.app.brand.application;

import com.jeremiasAvero.app.brand.application.exception.BrandAlreadyExistsException;
import com.jeremiasAvero.app.brand.application.exception.BrandNotFoundException;
import com.jeremiasAvero.app.brand.domain.BrandEntity;
import com.jeremiasAvero.app.brand.domain.BrandRepository;
import com.jeremiasAvero.app.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandService {
    private final BrandRepository repo;

    public BrandService(BrandRepository repo){
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<BrandEntity> findAll(){
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public BrandEntity findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id, ""));
    }
    @Transactional(readOnly = true)
    public BrandEntity findByName(String name) {
        return repo.findByName(name)
                .orElseThrow(() -> new BrandNotFoundException(null, name));
    }
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }

    public BrandEntity save(BrandEntity brand){
        if(existsByName(brand.getName())){
            throw new BrandAlreadyExistsException(brand.getName());
        }
        return repo.save(brand);
    }

    @Transactional
    public BrandEntity update(Long id, BrandEntity brand) {
        BrandEntity existing = repo.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id, ""));

        existing.setName(brand.getName());
        return repo.update(id, existing);
    }

    @Transactional
    public void deleteById(Long id) {
        if (repo.findById(id).isEmpty()) {
            throw new BrandNotFoundException(id, "");
        }
        repo.deleteById(id);
    }
}
