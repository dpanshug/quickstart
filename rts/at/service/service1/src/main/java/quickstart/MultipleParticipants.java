/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
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
 */
package quickstart;

import org.jboss.jbossts.star.util.TxMediaType;
import org.jboss.jbossts.star.util.TxSupport;

import java.net.HttpURLConnection;

public class MultipleParticipants {
    static JAXRSServer txnServer;

    public static void main(String[] args) {
        String coordinatorUrl = null; // the endpoint of the resource for creating transaction coordinators
        String serviceUrl = null; // the endpoint for the example web service that will take part in a transaction

        for (String arg : args)
            if (arg.startsWith("coordinator="))
                coordinatorUrl = arg.substring("coordinator=".length());
            else if (arg.startsWith("service="))
                serviceUrl = arg.substring("service=".length());

        if (coordinatorUrl == null || serviceUrl == null)
            throw new RuntimeException("Missing coordinator or service URLs");

        startServer(serviceUrl);
        txnServer.addDeployment(new TransactionAwareResource.ServiceApp(), "/");

        // get a helper for using REST Atomic Transactions, passing in the well know resource endpoint for the transaction coordinator
        TxSupport txn = new TxSupport(coordinatorUrl);

        int oldCommitCnt = Integer.parseInt(
                txn.httpRequest(new int[] {HttpURLConnection.HTTP_OK}, serviceUrl + "/query", "GET",
                TxMediaType.PLAIN_MEDIA_TYPE, null, null));

        // start a REST Atomic transaction
        txn.startTx();

        /*
         * Send two web service requests. Include the resource url for registering durable participation
         * in the transaction with the request (namely txn.enlistUrl())
         *
         * Each request should cause the service to enlist a unit of work within the transaction.
         */
        String serviceRequest = serviceUrl + "?enlistURL=" + txn.getDurableParticipantEnlistmentURI();

        txn.httpRequest(new int[] {HttpURLConnection.HTTP_OK}, serviceRequest, "GET",
                TxMediaType.PLAIN_MEDIA_TYPE, null, null);

        txn.httpRequest(new int[] {HttpURLConnection.HTTP_OK}, serviceRequest, "GET",
                TxMediaType.PLAIN_MEDIA_TYPE, null, null);

        /*
         * Commit the transaction (we expect the service to receive a prepare followed by a commit request for
         * each work unit it enlists)
         * Note that if there was only one work unit then the implementation would skip the prepare step.
         */
        System.out.println("Client: Committing transaction");
        txn.commitTx();

        // the web service should have received prepare and commit requests from the transaction coordinator
        // (TXN_MGR_URL) for each work unit
        int newCommitCnt = Integer.parseInt(
                txn.httpRequest(new int[] {HttpURLConnection.HTTP_OK}, serviceUrl + "/query", "GET",
                TxMediaType.PLAIN_MEDIA_TYPE, null, null));

        stopServer();

        // check that the service has been asked to commit twice
        if (oldCommitCnt + 2 == newCommitCnt)
            System.out.println("SUCCESS: Both service work loads received commit requests");
        else
            throw new RuntimeException("FAILURE: At least one server work load did not receive a commit request: " +
                    (newCommitCnt - oldCommitCnt));
    }

    public static void startServer(String serviceUrl) {
        int servicePort = Integer.parseInt(serviceUrl.replaceFirst(".*:(.*)/.*", "$1"));
        // the example uses an embedded JAX-RS server for running the service that will take part in a transaction
        txnServer = new JAXRSServer("recovery1", servicePort);
    }

    public static void stopServer() {
        // shutdown the embedded JAX-RS server
        txnServer.stop();
    }
}
