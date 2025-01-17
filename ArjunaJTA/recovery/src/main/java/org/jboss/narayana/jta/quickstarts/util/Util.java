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
package org.jboss.narayana.jta.quickstarts.util;

import java.io.File;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;

public class Util {
    public static final String dataDir = "target/data";
    public static final String recoveryStoreDir = dataDir + "/recoveryTestStore";
    public static final String activeMQStoreDir = dataDir + "/activeMQ";

    public static void emptyObjectStore() {
        String objectStoreDirName = BeanPopulator.getDefaultInstance(ObjectStoreEnvironmentBean.class).getObjectStoreDir();

        if (objectStoreDirName != null)
            Util.removeContents(new File(objectStoreDirName));
    }

    public static int countLogRecords() {
        String objectStoreDirName = BeanPopulator.getDefaultInstance(ObjectStoreEnvironmentBean.class).getObjectStoreDir();

        if (objectStoreDirName != null) {
            File osDir = new File(objectStoreDirName);

            if (osDir.exists())
                return Util.countLogRecords(osDir, 0);
        }

        return 0;
    }

    public static int countLogRecords(File directory, int count)
    {
        if ((directory != null) &&
                directory.isDirectory() &&
                (!directory.getName().equals("")) &&
                (!directory.getName().equals("/")) &&
                (!directory.getName().equals("\\")) &&
                (!directory.getName().equals(".")) &&
                (!directory.getName().equals("..")))
        {
            File[] contents = directory.listFiles();

            for (File f : contents) {
                if (f.isDirectory()) {
                    count += countLogRecords(f, count);
                } else {
                    count += 1;
                }
            }
        }

        return count;
    }


    public static void removeContents(File directory)
    {
        if ((directory != null) &&
                directory.isDirectory() &&
                (!directory.getName().equals("")) &&
                (!directory.getName().equals("/")) &&
                (!directory.getName().equals("\\")) &&
                (!directory.getName().equals(".")) &&
                (!directory.getName().equals("..")))
        {
            File[] contents = directory.listFiles();

            for (File f : contents) {
                if (f.isDirectory()) {
                    removeContents(f);

                    f.delete();
                } else {
                    f.delete();
                }
            }
        }

        if (directory != null)
            directory.delete();
    }
}
