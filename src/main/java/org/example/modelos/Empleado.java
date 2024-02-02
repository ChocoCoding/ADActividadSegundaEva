package org.example.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleado")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Empleado {
    @Id
    @Column(name = "dni",columnDefinition = "char(9)")
    @NonNull
    private String dni;
    @NonNull
    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "empleado")
    private List<EmpleadoProyecto> listaEmpleadosProyectos = new ArrayList<>();

    @OneToOne(mappedBy = "empleadoPlantilla")
    private DatosProfesionales datosProfesionales;

    @OneToMany(mappedBy = "jefe")
    private List<Proyecto> proyectos= new ArrayList<>();

    public void addProyecto(Proyecto proyecto){
        proyectos.add(proyecto);
    }



}
