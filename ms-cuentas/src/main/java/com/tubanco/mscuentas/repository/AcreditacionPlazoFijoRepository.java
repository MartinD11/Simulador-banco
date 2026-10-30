package com.tubanco.mscuentas.repository;

import com.tubanco.mscuentas.entity.AcreditacionPlazoFijo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcreditacionPlazoFijoRepository
        extends JpaRepository<AcreditacionPlazoFijo, Long> {

    boolean existsByPlazoFijoId(Long plazoFijoId);
}
