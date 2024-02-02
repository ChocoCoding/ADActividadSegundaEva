package org.example.utilities;

import java.math.BigDecimal;
import java.util.Scanner;

public class Utilidades {

    public static String pedirPalabra(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return sc.next();
    }


    public static String pedirString(String mensaje) {
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return sc.nextLine();
    }

    public static BigDecimal pedirBigDecimal(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return sc.nextBigDecimal();
    }

    public static char pedirChar(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return (char) sc.nextInt();
    }

}
