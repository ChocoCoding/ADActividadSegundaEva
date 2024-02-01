package org.example;

import com.google.protobuf.StringValue;
import org.example.modelos.Categorias;
import org.example.modelos.DatosProfesionales;
import org.example.modelos.Empleado;
import org.example.repositorios.DatosProfesionalesRepository;
import org.example.repositorios.EmpleadoProyectoRepository;
import org.example.repositorios.EmpleadoRepository;
import org.example.repositorios.ProyectoRepository;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    static EmpleadoRepository empleadoRepository;
    static DatosProfesionalesRepository datosProfesionalesRepository;
    static EmpleadoProyectoRepository empleadoProyectoRepository;
    static ProyectoRepository proyectoRepository;
    public static void main(String[] args) {
        Session session = HibernateUtil.get().openSession();
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
        session.close();
    }

    private static void gestionarProyectos() {
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

            }

        } while (opt != 0);
    }


    public static void gestionarEmpleados(){
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


            }

        } while (opt != 0);

    }

    private static void crearEmpleadoOEmpleadoPlantilla() {
        try {
            String categoria = "";
            String dni = pedirString("Introduce el dni del empleado");
            String nombre = pedirString("Introduce el nombre del empleado");
            Empleado empleado = null;

                empleado = new Empleado(dni,nombre);
                //Guardamos el empleado en la base de datos
                empleadoRepository.crear(empleado);

            String ans = pedirString("¿Es un empleado de plantilla? SI/NO");
            if (ans.equalsIgnoreCase("si")){
                System.out.println("Categorias: A | B | C | D");
                do {
                    categoria= pedirString("Introduce la categoria del empleado");
                }while (!comprobarCategoria(categoria));

                BigDecimal sueldo = pedirBigDecimal("Introduce el sueldo del empleado");
                DatosProfesionales datosProfesionales = new DatosProfesionales(empleado,sueldo,Categorias.valueOf(categoria));
                empleado.setDatosProfesionales(datosProfesionales);
                //Guardamos los datos profesionales
                datosProfesionalesRepository.crear(datosProfesionales);

                System.out.println("Se ha creado el empleado en plantilla con dni: " + empleado.getDni());
            }else System.out.println("Se ha creado, el empleado con dni: " + empleado.getDni() + ",no esta en la plantilla");

        }catch (ConstraintViolationException cve){
            System.out.println("El empleado con dni ya existe");
        }
    }

    private static String pedirString(String mensaje) {
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return sc.nextLine();
    }

    private static BigDecimal pedirBigDecimal(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return sc.nextBigDecimal();
    }

    private static char pedirChar(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return (char) sc.nextInt();
    }
    
    private static boolean comprobarCategoria(String c){
        for (Categorias cat: Categorias.values()) {
            if (c.equals(String.valueOf(cat))){
                return true;
            }
        }
        return false;
    }
}