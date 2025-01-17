/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005-2010, Red Hat, and individual contributors
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
 * @author JBoss Inc.
 */
/*
 * FirstManager.java
 *
 * Copyright (c) 2003 Arjuna Technologies Ltd.
 *
 * $Id: FirstManager.java,v 1.3 2004/04/21 13:09:18 jhalliday Exp $
 *
 */
package org.jboss.narayana.quickstarts.wsat.jtabridge.first.jaxws;

import jakarta.ejb.Remote;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

/**
 * Interface to a simple First. Provides simple methods to manipulate bookings.
 * 
 * @author paul.robinson@redhat.com, 2012-01-04
 */
@WebService(name = "FirstServiceAT", targetNamespace = "http://www.jboss.org/narayana/quickstarts/wsat/simple/first")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Remote
public interface FirstServiceAT {

    /**
     * Create a new booking
     */
    @WebMethod
    public void incrementCounter(int numSeats);

    /**
     * obtain the number of existing bookings
     * 
     * @return the number of current bookings
     */
    @WebMethod
    public int getFirstCounter();

    /**
     * obtain the number of existing bookings
     *
     * @return the number of current bookings
     */
    @WebMethod
    public int getSecondCounter();

    /**
     * Reset the booking count to zero
     */
    @WebMethod
    public void resetCounter();

}
