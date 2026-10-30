package com.tubanco.msinversiones.entity;

import com.tubanco.msinversiones.enums.EstadoPlazoFijo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "plazos_fijos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlazoFijo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cuentaId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal tna;

    @Column(nullable = false)
    private Integer plazoDias;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal interesGenerado;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal montoAlVencimiento;

    @Column(nullable = false)
    private LocalDate fechaConstitucion;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPlazoFijo estado;
}