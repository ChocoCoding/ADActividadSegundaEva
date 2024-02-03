package org.example.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "proyecto")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"listaEmpleadosProyectos","jefe"})
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
    private List<EmpleadoProyecto> listaEmpleadosProyectos= new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "dni_jefe_proyecto", columnDefinition = "char(9)")
    private Empleado jefe;

    public Proyecto(String nombre, LocalDate fechaInicio, LocalDate fechaFin, Empleado jefe) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.jefe = jefe;
    }

    public void asignarEmpleadosProyecto(EmpleadoProyecto empleadoProyecto){
        this.listaEmpleadosProyectos.add(empleadoProyecto);
    }

    public void eliminarEmpleadosProyectos(EmpleadoProyecto empleadoProyecto){
        this.listaEmpleadosProyectos.remove(empleadoProyecto);
    }

    public void eliminarJefe(){
        this.setJefe(null);
    }
}
