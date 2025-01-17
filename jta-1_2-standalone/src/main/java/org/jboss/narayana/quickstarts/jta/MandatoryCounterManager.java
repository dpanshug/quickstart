/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.narayana.quickstarts.jta;

import jakarta.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.transaction.Transactional;
import jakarta.transaction.TransactionalException;
import jakarta.transaction.UserTransaction;

/**
 * <p>
 * A class with definition of the
 * {@link Transactional.TxType#MANDATORY} transactional boundary.
 * <p>
 * <p>
 * When any of the methods are called they require transaction
 * context being available on the invocation.
 * If there is no transaction context available on the invocation
 * {@link TransactionalException} is thrown.
 * </p>
 * 
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Transactional(Transactional.TxType.MANDATORY)
public class MandatoryCounterManager {

    @Inject
    private Counter counter;

    public boolean isTransactionAvailable() {
        UserTransaction userTransaction = null;
        try {
            userTransaction = (UserTransaction) new InitialContext().lookup("java:/UserTransaction");
        } catch (final NamingException e) {
        }

        return userTransaction != null;
    }

    public int getCounter() {
        return counter.get();
    }

    public void incrementCounter() {
        counter.increment();
    }

}
