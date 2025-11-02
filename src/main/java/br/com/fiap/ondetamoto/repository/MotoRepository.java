package br.com.fiap.ondetamoto.repository;

import br.com.fiap.ondetamoto.model.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {


    Page<Moto> findByTagIgnoreCase(String tag, Pageable pageable);

    Page<Moto> findBySetoresId(Long setorId, Pageable pageable);
}
