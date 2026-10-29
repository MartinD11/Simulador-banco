package com.tubanco.mscuentas.repository;

import com.tubanco.mscuentas.entity.Cuenta;
import com.tubanco.mscuentas.enums.TipoCuenta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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

    /*
    *
    *
    * Este método se utiliza para obtener una cuenta en modo de actualización,
    *  lo que implica que se bloqueará la fila correspondiente en la base de datos hasta
    *  que se complete la transacción.
    * utilizamos el bloqueo pesimista
    */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cuenta c WHERE c.id = :id")
    Optional<Cuenta> findByIdForUpdate(@Param("id") Long id);
}
