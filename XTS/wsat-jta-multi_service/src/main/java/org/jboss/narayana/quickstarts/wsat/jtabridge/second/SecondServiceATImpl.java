/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. 
 * See the copyright.txt in the distribution for a full listing 
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 * 
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package org.jboss.narayana.quickstarts.wsat.jtabridge.second;

import org.jboss.narayana.quickstarts.wsat.jtabridge.second.jaxws.SecondServiceAT;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * @author paul.robinson@redhat.com, 2012-10-29
 */
@Stateless
@Remote(SecondServiceAT.class)
@WebService(serviceName = "SecondServiceATService", portName = "SecondServiceAT", name = "SecondServiceAT", targetNamespace = "http://www.jboss.org/narayana/quickstarts/wsat/simple/second")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@TransactionAttribute(TransactionAttributeType.MANDATORY) // default is REQUIRED
public class SecondServiceATImpl implements SecondServiceAT {

    private static final int ENTITY_ID = 1;

    @PersistenceContext
    protected EntityManager em;

    /**
     * Incriment the second counter. This is done by updating the counter within a JTA transaction. The JTA transaction
     * was automatically bridged from the WS-AT transaction.
     */
    @WebMethod
    public void incrementCounter(int num) {

        System.out.println("[SERVICE] Second service invoked to increment the counter by '" + num + "'");

        // invoke the backend business logic:
        System.out.println("[SERVICE] Using the JPA Entity Manager to update the counter within a JTA transaction");

        SecondCounterEntity entitySecond = lookupCounterEntity();
        entitySecond.incrementCounter(num);
        em.merge(entitySecond);
    }

    @WebMethod
    public int getCounter() {
        System.out.println("[SERVICE] getCounter() invoked");
        SecondCounterEntity secondCounterEntity = lookupCounterEntity();
        if (secondCounterEntity == null) {
            return -1;
        }
        return secondCounterEntity.getCounter();
    }

    @WebMethod
    public void resetCounter() {
        SecondCounterEntity entitySecond = lookupCounterEntity();
        entitySecond.setCounter(0);
        em.merge(entitySecond);
    }

    private SecondCounterEntity lookupCounterEntity() {
        SecondCounterEntity entitySecond = em.find(SecondCounterEntity.class, ENTITY_ID);
        if (entitySecond == null) {
            entitySecond = new SecondCounterEntity(ENTITY_ID, 0);
            em.persist(entitySecond);
        }
        return entitySecond;
    }

}
