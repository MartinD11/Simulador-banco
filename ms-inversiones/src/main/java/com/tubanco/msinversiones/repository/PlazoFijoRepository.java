package com.tubanco.msinversiones.repository;

import com.tubanco.msinversiones.entity.PlazoFijo;
import com.tubanco.msinversiones.enums.EstadoPlazoFijo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlazoFijoRepository extends JpaRepository<PlazoFijo, Long> {

    List<PlazoFijo> findByEstadoAndFechaVencimientoLessThanEqual(
            EstadoPlazoFijo estado,
            LocalDate fecha
    );

    List<PlazoFijo> findByCuentaId(Long cuentaId);
}
