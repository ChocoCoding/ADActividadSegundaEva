package org.example.repositorios;

import org.hibernate.Session;

public class ProyectoRepository {

    Session session;
    public ProyectoRepository(Session session){
        this.session = session;
    }




}
