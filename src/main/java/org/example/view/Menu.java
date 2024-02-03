package org.example.view;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;
import org.example.modelos.*;
import org.example.repositorios.DatosProfesionalesRepository;
import org.example.repositorios.EmpleadoProyectoRepository;
import org.example.repositorios.EmpleadoRepository;
import org.example.repositorios.ProyectoRepository;
import org.example.utilities.Utilidades;
import org.example.utilities.Validaciones;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
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
                    break;
                case 4:
                    cambiarJefeProyecto();
                    break;
            }

        } while (opt != 0);
    }

    private void crearProyecto() {
        LocalDate fechaFin;
        try {
            String nombre = Utilidades.pedirString("Introduce el nombre del proyecto");
            LocalDate fechaInicio = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de inicio en el siguiente formato: DD-MM-YYYY"));
            do{
                fechaFin = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de finalizacion en el siguiente formato: DD-MM-YYYY"));
                if (!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin)){
                    System.out.println("La fecha fin debe ser mayor que la fecha de inicio");
                }
            }while(!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin));

            Empleado jefe;
            do {
                jefe = empleadoRepository.findEmpleadoByDni(Utilidades.pedirPalabra("Introduce el DNI del jefe del proyecto"));

                if (jefe.getDatosProfesionales() == null){
                    System.out.println("Solo los empleados de plantilla pueden ser jefes");
                }
            }while(jefe.getDatosProfesionales() == null);

            Proyecto proyecto = new Proyecto(nombre, fechaInicio, fechaFin, jefe);
            EmpleadoProyecto empleadoProyecto = new EmpleadoProyecto(jefe, proyecto, fechaInicio, fechaFin);
            proyecto.asignarEmpleadosProyecto(empleadoProyecto);
            jefe.addlistEmpleadosProyectos(empleadoProyecto);
            jefe.addProyecto(proyecto);

            proyectoRepository.crear(proyecto);
            empleadoProyectoRepository.crear(empleadoProyecto);
        }catch (DateTimeParseException dtpe){
            System.out.println("La fecha es incorrecta, debe estar en formato [DD-MM-YYYY]");
        }catch (DataException de){
            System.out.println("El nombre del proyecto no debe exceder los 35 caracteres");
        }catch (NoResultException nre){
            System.out.println("No existe el empleado con ese DNI");
        }
    }

    private void eliminarProyecto(){
        Proyecto proyecto = proyectoRepository.findProyectoByid(Utilidades.pedirInt("Introduce el id del proyecto a eliminar"));

    }


    public void cambiarJefeProyecto(){
        Proyecto proyecto = proyectoRepository.findProyectoByid(Utilidades.pedirInt("Introduce la id del proyecto"));
        Empleado empleado = empleadoRepository.findEmpleadoByDni(Utilidades.pedirPalabra("Introduce el DNI del nuevo Jefe"));
        EmpleadoProyecto empleadoProyecto = empleadoProyectoRepository.findAsignacionEmpleadoProyecto(proyecto.getJefe().getDni(),proyecto.getIdProyecto());


        empleadoProyecto.setEmpleado(empleado);
        empleado.addlistEmpleadosProyectos(empleadoProyecto);
        proyecto.asignarEmpleadosProyecto(empleadoProyecto);

        System.out.println("Estoy aqui");
        proyecto.setJefe(empleado);
        proyectoRepository.update(proyecto);

    }

    public void gestionarEmpleados(){
        Scanner scanner = new Scanner(System.in);
        int opt = 1;
        do {
            System.out.println("\n************************************ Gestión de empleados ****************************************");
            System.out.println("\n\t1. Crear Empleado.\t\t\t\t\t\t5. Quitar un empleado de un proyecto.");
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
                case 2:
                    modificarEmpleado();
                    break;
                case 3:
                    borrarEmpleado();
                    break;
                case 4:
                    asignarEmpleadoAProyecto();
                    break;
                case 5:
                    quitarEmpleadoDeProyecto();
                    break;
                case 6:
                    mostrarDatosEmpleadosPlantilla();
                    break;
                case 7:
                    mostrarEmpleadosJefes();
                    break;
            }

        } while (opt != 0);

    }


    private void mostrarEmpleadosJefes() {
        List<Empleado> empleados = empleadoRepository.findAll();
        for(Empleado e: empleados){
                if (!e.getProyectos().isEmpty()){
                    System.out.println(e);
                }
        }
    }


    private void mostrarDatosEmpleadosPlantilla() {
        List<Empleado> empleados = empleadoRepository.findAll();
        for(Empleado e: empleados){
            if (e.getDatosProfesionales() != null){
                System.out.println(e);
            }
        }

    }

    private void quitarEmpleadoDeProyecto() {
        Empleado empleado = empleadoRepository.findEmpleadoByDni(Utilidades.pedirPalabra("Introduce el dni del empleado que quieres quitar proyecto"));
        Proyecto proyecto = proyectoRepository.findProyectoByid(Utilidades.pedirInt("Introduce la ID del proyecto"));
        EmpleadoProyecto empleadoProyecto = empleadoProyectoRepository.findAsignacionEmpleadoProyecto(empleado.getDni(),proyecto.getIdProyecto());

        empleado.eliminarEmpleadosProyectos(empleadoProyecto);
        proyecto.eliminarEmpleadosProyectos(empleadoProyecto);

        empleadoProyectoRepository.remove(empleadoProyecto);
        empleadoRepository.update(empleado);
        proyectoRepository.update(proyecto);
    }


    private void asignarEmpleadoAProyecto(){
        Empleado empleado = empleadoRepository.findEmpleadoByDni(Utilidades.pedirPalabra("Introduce el dni del empleado que quieres agregar al proyecto"));
        Proyecto proyecto = proyectoRepository.findProyectoByid(Utilidades.pedirInt("Introduce la ID del proyecto"));
        LocalDate fechaFin;
        LocalDate fechaInicio = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de inicio en el siguiente formato: DD-MM-YYYY"));
        do{
            fechaFin = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de finalizacion en el siguiente formato: DD-MM-YYYY"));
            if (!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin)){
                System.out.println("La fecha fin debe ser mayor que la fecha de inicio");
            }
        }while(!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin));

        EmpleadoProyecto empleadoProyecto = new EmpleadoProyecto(empleado,proyecto,fechaInicio,fechaFin);

        empleado.addlistEmpleadosProyectos(empleadoProyecto);
        proyecto.asignarEmpleadosProyecto(empleadoProyecto);

        empleadoProyectoRepository.crear(empleadoProyecto);
        empleadoRepository.update(empleado);
        proyectoRepository.update(proyecto);
    }

    private void modificarEmpleado() {
        try {
            String dni = Utilidades.pedirPalabra("Introduce el DNI del empleado que quieres modificar: ");
            Empleado empleado = empleadoRepository.findEmpleadoByDni(dni);
            String categoria = "";
            String sueldoStr;
            BigDecimal sueldo = null;
            String nombre = Utilidades.pedirPalabra("Introduce el nuevo nombre del empleado");
            if (empleado.getDatosProfesionales() == null){
                String ans = Utilidades.pedirPalabra("¿Quieres añadir al empleado a la plantilla? SI/NO");
                if (ans.equalsIgnoreCase("SI")){
                    do {
                        System.out.println("Categorias: A | B | C | D");
                        categoria= Utilidades.pedirPalabra("Introduce la categoria del empleado");
                        if (!Validaciones.comprobarCategoria(categoria)){
                            System.out.println("La categoria introducida no existe");
                        }
                    }while (!Validaciones.comprobarCategoria(categoria));

                    do {
                        sueldoStr = Utilidades.pedirPalabra("Introduce el sueldo del empleado");
                        if (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr))){
                            System.out.println("Los decimales deben separarse por '.' [20000.4]");
                        }else {
                            sueldo = BigDecimal.valueOf(Double.parseDouble(sueldoStr));
                        }
                    }while (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr)));

                    DatosProfesionales datosProfesionales = new DatosProfesionales(empleado,sueldo,Categorias.valueOf(categoria));

                    empleado.setNombre(nombre);
                    empleado.setDatosProfesionales(datosProfesionales);
                    //Guardamos los datos profesionales
                    datosProfesionalesRepository.crear(datosProfesionales);

                    System.out.println("Se ha creado el empleado en plantilla con dni: " + empleado.getDni());

                } else if (ans.equalsIgnoreCase("NO")) {
                    empleado.setNombre(nombre);
                    empleadoRepository.update(empleado);
                    System.out.println("Se ha modificado al empleado");
                }
            }else {
                DatosProfesionales dP = datosProfesionalesRepository.findByDni(dni);
                do {
                    System.out.println("Categorias: A | B | C | D");
                    categoria= Utilidades.pedirPalabra("Introduce la categoria del empleado");
                    if (!Validaciones.comprobarCategoria(categoria)){
                        System.out.println("La categoria introducida no existe");
                    }
                }while (!Validaciones.comprobarCategoria(categoria));

                do {
                    sueldoStr = Utilidades.pedirPalabra("Introduce el sueldo del empleado");
                    if (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr))){
                        System.out.println("Los decimales deben separarse por '.' [20000.4]");
                    }else {
                        sueldo = BigDecimal.valueOf(Double.parseDouble(sueldoStr));
                    }
                }while (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr)));


                dP.setCategorias(Categorias.valueOf(categoria));
                dP.setEmpleadoPlantilla(empleado);
                dP.setSueldoBruto(sueldo);
                empleado.setNombre(nombre);
                empleado.setDatosProfesionales(new DatosProfesionales(empleado,sueldo,Categorias.valueOf(categoria)));
                //Guardamos los datos profesionales
                empleadoRepository.update(empleado);
                datosProfesionalesRepository.update(dP);

                System.out.println("Se ha actualizado el empleado con dni: " + empleado.getDni());
            }
        }catch (ConstraintViolationException | EntityExistsException cve){
            System.out.println("Ese dni ya esta registrado");
            crearEmpleadoOEmpleadoPlantilla();
        }catch (InputMismatchException ime){
            System.out.println("El dato introducido no es correcto");
            System.out.println("Los valores decimales deben ir separados por '.' [20000.4]");
            crearEmpleadoOEmpleadoPlantilla();
        }
    }

    private void borrarEmpleado() {
        String dni = Utilidades.pedirPalabra("Introduce el DNI del empleado que quieres borrar: ");
        //Seleccionamos el empleado
        Empleado empleado = empleadoRepository.findEmpleadoByDni(dni);
        //Cogemos sus datos profesionales
        DatosProfesionales datosProfesionales = datosProfesionalesRepository.findByDni(dni);
        //Buscamos las asignaciones de ese empleado
        List<EmpleadoProyecto> asignacionesEmpleado = empleadoProyectoRepository.findAsignacionesByDni(dni);
        System.out.println("Asignaciones empleados " + asignacionesEmpleado.size());
        //Buscamos los proyectos en los que es jefe
        List<Proyecto> proyectos = empleado.getProyectos();
        System.out.println("Proyectos en los que es jefe: " + proyectos.size());

        //Eliminamos la relacion     Empleado - jefe - Proyecto
        for(int x = proyectos.size() -1 ; x >= 0 ; x--){
            proyectos.get(x).setJefe(null);
            proyectos.remove(proyectos.get(x));
        }
        //Eliminamos la relacion     Empleado - realiza - Proyecto
        for(int x = asignacionesEmpleado.size(); x >= 0; x--){
            empleado.setListaEmpleadosProyectos(null);
        }

    }

    private void crearEmpleadoOEmpleadoPlantilla() {
        try {
            Scanner scanner = new Scanner(System.in);
            String categoria = "";
            String sueldoStr;
            BigDecimal sueldo = null;
            Empleado empleado= null;;
            String dni = Utilidades.pedirPalabra("Introduce el dni del empleado");
            if (Validaciones.validarDNI(dni)){
                String nombre = Utilidades.pedirPalabra("Introduce el nombre del empleado");
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
                    sueldoStr = Utilidades.pedirPalabra("Introduce el sueldo del empleado");
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
        }catch (NoResultException nre){
            System.out.println("El DNI no existe");
        }
    }



}
