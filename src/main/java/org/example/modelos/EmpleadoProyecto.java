package org.example.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "asig_proyecto")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class EmpleadoProyecto {

    @ManyToOne
    @Id
    @JoinColumn(name = "dni_emp")
    private Empleado empleado;

    @ManyToOne
    @Id
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @Id
    @Column(name = "fecha_inicio")
    @NonNull
    private LocalDate fechaInicio;

    @NonNull
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

}
