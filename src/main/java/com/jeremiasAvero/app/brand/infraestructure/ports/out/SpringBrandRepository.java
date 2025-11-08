package com.jeremiasAvero.app.brand.infraestructure.ports.out;

import com.jeremiasAvero.app.brand.domain.BrandEntity;
import com.jeremiasAvero.app.brand.domain.BrandRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpringBrandRepository implements BrandRepository {
  private final JpaBrandRepository repo;

  public SpringBrandRepository(JpaBrandRepository repo){
      this.repo = repo;
  }
    @Override
    public List<BrandEntity> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<BrandEntity> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public BrandEntity save(BrandEntity brand) {
        return repo.save(brand);
    }

    @Override
    public BrandEntity update(Long id, BrandEntity brand) {
        brand.setId(id);
        return repo.save(brand);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

	@Override
	public Optional<BrandEntity> findByName(String name) {
		return Optional.empty();
	}

    @Override
    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }
}
