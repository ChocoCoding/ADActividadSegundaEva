package org.example.repositorios;

import org.example.modelos.Empleado;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmpleadoRepository implements CRUD<Empleado>{
    Session session;

    public EmpleadoRepository(Session session){
        this.session = session;
    }


    @Override
    public void crear(Empleado empleado) {
        Transaction trx = session.beginTransaction();
        session.save(empleado);
        trx.commit();
    }

    @Override
    public void remove(Empleado empleado) {

    }

    @Override
    public void update(Empleado empleado) {

    }

    @Override
    public List<Empleado> findAll() {
        return null;
    }
}
