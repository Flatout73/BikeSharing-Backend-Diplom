package ru.hse.BikeSharing.Services;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Properties;

public class CustomGenerator extends IdentityGenerator implements Configurable {

    private IdentifierGenerator defaultGenerator;

    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        Long idValue = (Long)defaultGenerator.generate(s, obj);
        //idValue will be assigned your entity id
        return idValue;
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        DefaultIdentifierGeneratorFactory dd = new DefaultIdentifierGeneratorFactory();
        defaultGenerator = dd.createIdentifierGenerator("sequence", type, params);
    }
}
