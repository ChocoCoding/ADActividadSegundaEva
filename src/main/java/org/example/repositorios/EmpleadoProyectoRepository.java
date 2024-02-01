package org.example.repositorios;

import org.hibernate.Session;

public class EmpleadoProyectoRepository {

    Session session;
public EmpleadoProyectoRepository(Session session){
    this.session = session;
}
}
