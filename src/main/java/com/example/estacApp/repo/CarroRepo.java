package com.example.estacApp.repo;

import com.example.estacApp.model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarroRepo extends JpaRepository<Carro, Long> {
}
