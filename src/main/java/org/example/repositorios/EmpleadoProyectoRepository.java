package org.example.repositorios;

import org.example.modelos.EmpleadoProyecto;
import org.example.modelos.Proyecto;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmpleadoProyectoRepository implements CRUD<EmpleadoProyecto>{

    Session session;
    public EmpleadoProyectoRepository(Session session){
    this.session = session;
}

    @Override
    public void crear(EmpleadoProyecto empleadoProyecto) {
        Transaction trx = session.beginTransaction();
        session.persist(empleadoProyecto);
        trx.commit();
    }

    @Override
    public void remove(EmpleadoProyecto empleadoProyecto) {
        Transaction trx = session.beginTransaction();
        session.remove(empleadoProyecto);
        trx.commit();
    }

    @Override
    public void update(EmpleadoProyecto empleadoProyecto) {
        Transaction trx = session.beginTransaction();
        session.update(empleadoProyecto);
        trx.commit();
    }

    @Override
    public List<EmpleadoProyecto> findAll() {
        Transaction trx = session.beginTransaction();
        List<EmpleadoProyecto> empleadoProyectos = this.session.createQuery("SELECT ep FROM EmpleadoProyecto ep").getResultList();
        trx.commit();
        return empleadoProyectos;
    }
}
