package org.example.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "proyecto")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int idProyecto;
    @NonNull
    @Column(name = "nombre")
    private String nombre;
    @NonNull
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @NonNull
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @OneToMany(mappedBy = "proyecto")
    private List<EmpleadoProyecto> listaEmpleadosProyectos;

    @ManyToOne
    @JoinColumn(name = "dni_jefe_proyecto", columnDefinition = "char(9)")
    private Empleado jefe;
}
