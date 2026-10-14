package com.tubanco.mscuentas.repository;

import com.tubanco.mscuentas.entity.Cuenta;
import com.tubanco.mscuentas.enums.TipoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta,Long> {

    Optional<Cuenta> findByCbu(String cbu);

    List<Cuenta> findAllByUsuarioId(Long usuarioId);

    boolean existsByUsuarioIdAndTipoCuenta(Long usuarioId, TipoCuenta tipoCuenta);

    @Query("SELECT c.saldo FROM Cuenta c WHERE c.cbu = :cbu")
    Optional<BigDecimal> obtenerSoloSaldoPorCbu(@Param("cbu") String cbu);

    List<Cuenta> findByUsuarioId(Long usuarioId);
}
