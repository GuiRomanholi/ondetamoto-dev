package br.com.fiap.ondetamoto.repository;

import br.com.fiap.ondetamoto.model.Setores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetoresRepository extends JpaRepository<Setores, Long> {
}