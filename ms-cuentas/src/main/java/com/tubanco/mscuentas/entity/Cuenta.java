package com.tubanco.mscuentas.entity;

import com.tubanco.mscuentas.enums.Estado;
import com.tubanco.mscuentas.enums.TipoCuenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 22)
    private String cbu;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipoCuenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "estado")
    private Estado estado;

    @Column(nullable = false)
    private Long usuarioId;

    @OneToOne(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private Tarjeta tarjeta;

    @Column(nullable = false)
    private String titular;


}
