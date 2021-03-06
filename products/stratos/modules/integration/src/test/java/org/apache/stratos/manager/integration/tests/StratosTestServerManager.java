/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.stratos.manager.integration.tests;

import org.apache.activemq.broker.BrokerService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.wso2.carbon.integration.framework.TestServerManager;
import org.wso2.carbon.integration.framework.utils.FrameworkSettings;
import org.wso2.carbon.integration.framework.utils.ServerUtils;
import org.wso2.carbon.integration.framework.utils.TestUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.testng.Assert.assertNotNull;

/**
 * Prepare activemq, stratos server for tests, enables mock iaas, starts servers and stop them after the tests.
 */
public class StratosTestServerManager extends TestServerManager {

    private static final Log log = LogFactory.getLog(StratosTestServerManager.class);

    private final static String CARBON_ZIP = SampleApplicationTests.class.getResource("/").getPath() +
            "/../../../distribution/target/apache-stratos-4.1.0-SNAPSHOT.zip";
    private final static int PORT_OFFSET = 0;
    private static final String ACTIVEMQ_BIND_ADDRESS1 = "tcp://localhost:61616";
    private static final String ACTIVEMQ_BIND_ADDRESS2 = "tcp://localhost:5672";
    private static final String MOCK_IAAS_XML = "mock-iaas.xml";
    public static final String MQTT_URI = "mqtt://0.0.0.0:1883?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600";

    private ServerUtils serverUtils;
    private String carbonHome;

    public StratosTestServerManager() {
        super(CARBON_ZIP, PORT_OFFSET);
        serverUtils = new ServerUtils();
    }

    @Override
    @BeforeSuite(timeOut = 300000)
    public String startServer() throws IOException {
        try {
            // Start ActiveMQ
            long time1 = System.currentTimeMillis();
            log.info("Starting ActiveMQ...");
            BrokerService broker = new BrokerService();
            broker.setBrokerName("testBroker");
            broker.addConnector(ACTIVEMQ_BIND_ADDRESS1);
            broker.start();
            long time2 = System.currentTimeMillis();
            log.info(String.format("ActiveMQ started in %d sec", (time2 - time1)/1000));
        } catch (Exception e) {
            throw new RuntimeException("Could not start ActiveMQ", e);
        }

        try {
            log.info("Setting up stratos server...");
            long time3 = System.currentTimeMillis();
            String carbonZip = getCarbonZip();
            if (carbonZip == null) {
                carbonZip = System.getProperty("carbon.zip");
            }

            if (carbonZip == null) {
                throw new IllegalArgumentException("carbon zip file is null");
            } else {
                carbonHome = this.serverUtils.setUpCarbonHome(carbonZip);
                TestUtil.copySecurityVerificationService(carbonHome);
                this.copyArtifacts(carbonHome);
                log.info("Stratos server setup completed");

                log.info("Starting stratos server...");
                this.serverUtils.startServerUsingCarbonHome(carbonHome, carbonHome, "stratos", PORT_OFFSET, null);
                FrameworkSettings.init();
                long time4 = System.currentTimeMillis();
                log.info(String.format("Stratos server started in %d sec", (time4 - time3)/1000));
                return carbonHome;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not stratos server", e);
        }
    }

    @Override
    @AfterSuite(timeOut = 60000)
    public void stopServer() throws Exception {
        super.stopServer();
    }

    protected void copyArtifacts(String carbonHome) throws IOException {
        log.info("Copying " + MOCK_IAAS_XML + " configuration file...");
        URL mockIaasUrl = getClass().getResource("/" + MOCK_IAAS_XML);
        assertNotNull(mockIaasUrl);
        File srcFile = new File(mockIaasUrl.getFile());
        File destFile = new File(carbonHome + "/repository/conf/" + MOCK_IAAS_XML);
        FileUtils.copyFile(srcFile, destFile);
        log.info(MOCK_IAAS_XML + " configuration file copied");
    }
}
