package com.tubanco.mscuentas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "acreditaciones_plazo_fijo",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_acreditacion_plazo_fijo",
                columnNames = "plazo_fijo_id"
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcreditacionPlazoFijo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plazo_fijo_id", nullable = false, unique = true)
    private Long plazoFijoId;

    @Column(nullable = false)
    private Long cuentaId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDateTime fechaAcreditacion;
}
