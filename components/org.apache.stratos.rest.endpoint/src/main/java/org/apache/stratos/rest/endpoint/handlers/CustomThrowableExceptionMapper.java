package org.apache.stratos.rest.endpoint.handlers;/*
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.rest.endpoint.Utils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class CustomThrowableExceptionMapper implements ExceptionMapper<Throwable> {
    private static Log log = LogFactory.getLog(CustomThrowableExceptionMapper.class);

    public Response toResponse(Throwable throwable) {
        if(log.isErrorEnabled()){
            log.error("Internal server error", throwable);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).
                entity(Utils.buildMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Internal server error")).build();
    }
}
