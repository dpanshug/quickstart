/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates,
 * and individual contributors as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
 * (C) 2011,
 * @author JBoss, by Red Hat.
 */
import org.jboss.jbossts.star.util.TxSupport;
import org.jboss.jbossts.star.util.TxStatusMediaType;

public class MultipleTransactions {
    public static void main(String[] args) throws Exception {
        String coordinatorUrl = "http://localhost:8080/rest-at-coordinator/tx/transaction-manager";

        if (args.length > 0 && args[0].startsWith("coordinator="))
            coordinatorUrl = args[0].substring("coordinator=".length());

        // create a helper with the desired transaction manager resource endpoint
        TxSupport[] txns = { new TxSupport(coordinatorUrl), new TxSupport(coordinatorUrl)};

        // start transactions
        for (TxSupport txn: txns)
            txn.startTx();

        // verify that all the transactions are active
        for (TxSupport txn: txns)
            if (!txn.txStatus().equals(TxStatusMediaType.TX_ACTIVE))
                throw new RuntimeException("A transaction should be active: " + txn.txStatus());

        // see how many RESTful transactions are running (there should be at least one)
        int txnCount = txns[0].txCount();

        if (txns[0].txCount() != 2)
            throw new RuntimeException("At least one transaction did not start");

        // end the transaction
        for (TxSupport txn: txns)
            txn.commitTx();

        // there should now be one fewer transactions
        if (txns[0].txCount() >= txnCount)
            throw new RuntimeException("At least one transaction did not complete");

        System.out.println("Success");
    }
}
