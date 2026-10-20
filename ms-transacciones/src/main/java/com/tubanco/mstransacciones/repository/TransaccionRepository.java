package com.tubanco.mstransacciones.repository;

import com.tubanco.mstransacciones.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion,Long> {
    List<Transaccion> findByCuentaOrigenIdOrCuentaDestinoIdOrderByFechaTransaccionDesc(Long cuentaOrigenId, Long cuentaDestinoId);
}
