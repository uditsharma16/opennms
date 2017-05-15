/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2017-2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.utils;

import java.util.Objects;

/**
 * Exception to indicate that a service is (probably temporarily) unavailable.
 *
 * It was first introduced when REST services deployed on the jetty container side are consuming services from the osgi karaf side.
 * In some cases the rest service is up and running, but the osgi bundles exporting required services may not yet.
 * In order to allow the REST service to be consumed at some point later, this exception is used to signalise that the rest endpoint is not yet ready.
 * When the osgi service is fully resolved, started and exported the reuqired services, the REST endpoint can consume it and comes online.
 *
 */
public class ServiceTemporarilyUnavailableException extends Exception {

    private final Class<?> unavailableServiceClass;

    public <T> ServiceTemporarilyUnavailableException(Class<T> serviceClass) {
        this.unavailableServiceClass = Objects.requireNonNull(serviceClass);
    }

    public Class<?> getUnavailableServiceClass() {
        return unavailableServiceClass;
    }
}
