/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@ApplicationScoped
public class QuickstartEntityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<QuickstartEntity> findAll() {
        final Query query = entityManager.createQuery("select qe from QuickstartEntity qe");

        return (List<QuickstartEntity>) query.getResultList();
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public Long save(QuickstartEntity quickstartEntity) {
        if (quickstartEntity.isTransient()) {
            entityManager.persist(quickstartEntity);
        } else {
            entityManager.merge(quickstartEntity);
        }

        return quickstartEntity.getId();
    }
}
