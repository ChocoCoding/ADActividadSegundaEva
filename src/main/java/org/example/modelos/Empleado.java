package org.example.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "empleado")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
    @Id
    @Column(name = "dni",columnDefinition = "char(9)")
    private String dni;
    @NonNull
    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "empleado")
    private List<EmpleadoProyecto> listaEmpleadosProyectos;

    @OneToOne(mappedBy = "empleadoPlantilla")
    private DatosProfesionales datosProfesionales;

    @OneToMany(mappedBy = "jefe")
    private List<Proyecto> proyectos;

    private void addProyecto(Proyecto proyecto){
        proyectos.add(proyecto);
    }

}
