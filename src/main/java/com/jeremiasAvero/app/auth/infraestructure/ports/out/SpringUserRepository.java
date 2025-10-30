package com.jeremiasAvero.app.auth.infraestructure.ports.out;

import com.jeremiasAvero.app.auth.domain.UserEntity;
import com.jeremiasAvero.app.auth.domain.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpringUserRepository implements UserRepository{
	private final JpaUserRepository repo;

	public SpringUserRepository( JpaUserRepository repo){
		this.repo = repo;
	}

	@Override
	public UserEntity save(UserEntity user) {
		return repo.save(user);
	}

	@Override
	public Optional<UserEntity> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Optional<UserEntity> findByEmail(String email) {
		return repo.findByEmail(email);
	}

	@Override
	public boolean existsByEmail(String email) {
		return repo.existsByEmail(email);
	}

}
