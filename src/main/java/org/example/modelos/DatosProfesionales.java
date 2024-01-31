package org.example.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Types;

@Entity
@Table(name = "datos_profesionales")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class DatosProfesionales {
    @Id
    @OneToOne
    @JoinColumn(name = "dni")
    private Empleado empleadoPlantilla;

    @NonNull
    @Column(name = "sueldo_bruto", columnDefinition = "decimal(8,2)")
    private BigDecimal sueldoBruto;

    @NonNull
    @Column(name = "categoria")
    @Enumerated(EnumType.STRING)
    private Categorias categorias;

}
