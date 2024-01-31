package org.example;

import org.example.modelos.Categorias;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.get().openSession();
        Scanner scanner = new Scanner(System.in);
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

            }

        } while (opt != 0);

    }
}