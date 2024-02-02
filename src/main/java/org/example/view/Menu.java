package org.example.view;

import jakarta.persistence.EntityExistsException;
import org.example.modelos.*;
import org.example.repositorios.DatosProfesionalesRepository;
import org.example.repositorios.EmpleadoProyectoRepository;
import org.example.repositorios.EmpleadoRepository;
import org.example.repositorios.ProyectoRepository;
import org.example.utilities.Utilidades;
import org.example.utilities.Validaciones;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    Session session;

    public Menu(Session session) {
    this.session = session;
    }

    private EmpleadoRepository empleadoRepository;
    private DatosProfesionalesRepository datosProfesionalesRepository;
    private EmpleadoProyectoRepository empleadoProyectoRepository;
    private ProyectoRepository proyectoRepository;
    Validaciones validaciones;

    public void dialog(){

    Scanner scanner = new Scanner(System.in);
    empleadoRepository = new EmpleadoRepository(session);
    datosProfesionalesRepository = new DatosProfesionalesRepository(session);
    empleadoProyectoRepository = new EmpleadoProyectoRepository(session);
    proyectoRepository = new ProyectoRepository(session);


    int opt = 1;
        do {
        System.out.println("\n********************** Bienvenido a Empleados-Proyectos *****************************");
        System.out.println("\n\t1. Gestionar Empleados.\t\t\t\t\t2. Gestionar Proyectos.");
        System.out.println("\n\t0. Salir.");
        System.out.println("\n*************************************************************************************");
        scanner = new Scanner(System.in);
        System.out.println("Introduce un opcion");
        opt = scanner.nextInt();

        switch (opt){
            case 1:
                gestionarEmpleados();
                break;
            case 2:
                gestionarProyectos();
                break;
        }
    } while (opt != 0);

}

    private void gestionarProyectos() {
        Scanner scanner = new Scanner(System.in);
        int opt = 1;
        do {
            System.out.println("\n********************** Gestión de Proyectos *****************************");
            System.out.println("\n\t1. Crear Proyecto.\t\t\t\t\t\t4. Cambiar el jefe de un proyecto.");
            System.out.println("\n\t2. Modificar Proyecto.\t\t\t\t\t5. Mostrar los datos de un proyecto.");
            System.out.println("\n\t3. Eliminar Proyecto.\t\t\t\t\t0. Salir.");
            System.out.println("\n*************************************************************************************");
            scanner = new Scanner(System.in);
            System.out.println("Introduce un opcion");
            opt = scanner.nextInt();

            switch (opt){
                case 1:
                    crearProyecto();

            }

        } while (opt != 0);
    }

    private void crearProyecto() {
        String nombre = Utilidades.pedirString("Introduce el nombre del proyecto");
        LocalDate fechaInicio = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de inicio en el siguiente formato: DD-MM-YYYY"));
        LocalDate fechaFin = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de finalizacion en el siguiente formato: DD-MM-YYYY"));
        Empleado jefe = empleadoRepository.findEmpleadoByDni(Utilidades.pedirPalabra("Introduce el DNI del jefe del proyecto"));


        Proyecto proyecto = new Proyecto(nombre,fechaInicio,fechaFin,jefe);
        EmpleadoProyecto empleadoProyecto = new EmpleadoProyecto(jefe,proyecto,fechaInicio,fechaFin);
        proyecto.asignarEmpleadosProyecto(empleadoProyecto);
        jefe.addProyecto(proyecto);
        proyectoRepository.crear(proyecto);
        empleadoProyectoRepository.crear(empleadoProyecto);

    }


    public void gestionarEmpleados(){
        Scanner scanner = new Scanner(System.in);
        int opt = 1;
        do {
            System.out.println("\n************************************ Gestión de empleados ****************************************");
            System.out.println("\n\t1. Crear Empleado.\t\t\t\t\t\t5. Indicar fin de la participacion de un empleado en un proyecto.");
            System.out.println("\n\t2. Modificar Empleado.\t\t\t\t\t6. Mostrar datos empleados en plantilla.");
            System.out.println("\n\t3. Eliminar Empleado.\t\t\t\t\t7. Mostrar los Jefes de Proyecto.");
            System.out.println("\n\t4. Asignar Empleado a proyecto.\t\t\t0. SALIR.");
            System.out.println("\n***************************************************************************************************");
            scanner = new Scanner(System.in);
            System.out.println("Introduce un opcion");
            opt = scanner.nextInt();

            switch (opt){
                case 1:
                    crearEmpleadoOEmpleadoPlantilla();
                    break;
            }

        } while (opt != 0);

    }

    private void crearEmpleadoOEmpleadoPlantilla() {
        try {
            Scanner scanner = new Scanner(System.in);
            String categoria = "";
            String sueldoStr;
            BigDecimal sueldo = null;
            Empleado empleado= null;;
            String dni = Utilidades.pedirString("Introduce el dni del empleado");
            if (Validaciones.validarDNI(dni)){
                String nombre = Utilidades.pedirString("Introduce el nombre del empleado");
                if (Validaciones.validarNombre(nombre)){
                    empleado = new Empleado(dni,nombre);
                    //Guardamos el empleado en la base de datos
                    empleadoRepository.crear(empleado);
                }else {
                    System.out.println("-Máximo 35 caracteres");
                    System.out.println("-El nombre debe contener al menos 1 letra");
                    crearEmpleadoOEmpleadoPlantilla();
                }

            }else {
                System.out.println("El DNI no es válido debe tener el siguiente formato: [12345678T]");
                crearEmpleadoOEmpleadoPlantilla();
            }

            String ans = Utilidades.pedirPalabra("¿Es un empleado de plantilla? SI/NO");
            if (ans.equalsIgnoreCase("si")){
                do {
                    System.out.println("Categorias: A | B | C | D");
                    categoria= Utilidades.pedirPalabra("Introduce la categoria del empleado");
                    if (!Validaciones.comprobarCategoria(categoria)){
                        System.out.println("La categoria introducida no existe");
                    }
                }while (!Validaciones.comprobarCategoria(categoria));

                do {
                    sueldoStr = Utilidades.pedirString("Introduce el sueldo del empleado");
                    if (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr))){
                        System.out.println("Los decimales deben separarse por '.' [20000.4]");
                    }else {
                        sueldo = BigDecimal.valueOf(Double.parseDouble(sueldoStr));
                    }
                }while (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr)));


                DatosProfesionales datosProfesionales = new DatosProfesionales(empleado,sueldo, Categorias.valueOf(categoria));
                empleado.setDatosProfesionales(datosProfesionales);
                //Guardamos los datos profesionales
                datosProfesionalesRepository.crear(datosProfesionales);

                System.out.println("Se ha creado el empleado en plantilla con dni: " + empleado.getDni());
            }else System.out.println("Se ha creado, el empleado con dni: " + empleado.getDni() + ",no esta en la plantilla");

        }catch (ConstraintViolationException | EntityExistsException cve){
            System.out.println("Ese dni ya esta registrado");
            crearEmpleadoOEmpleadoPlantilla();
        }catch (InputMismatchException ime){
            System.out.println("El dato introducido no es correcto");
            System.out.println("Los valores decimales deben ir separados por '.' [20000.4]");
            crearEmpleadoOEmpleadoPlantilla();
        }
    }






}
