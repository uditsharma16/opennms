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

package org.opennms.web.rest.v2.topology;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.opennms.features.topology.rest.api.GraphDTO;
import org.opennms.features.topology.rest.api.MetaTopologyDTO;
import org.opennms.features.topology.rest.api.TopologyRestService;
import org.opennms.web.utils.ServiceLocator;
import org.opennms.web.utils.ServiceTemporarilyUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("topologyRestService")
@Path("topologies")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class TopologyRestServiceProxy {

    @Autowired
    private ServiceLocator serviceLocator;

    @GET
    public List<MetaTopologyDTO> getMetaTopologies() throws ServiceTemporarilyUnavailableException {
        final List<MetaTopologyDTO> metaTopologies = getTopologyRestService().getMetaTopologies();
        return metaTopologies;
    }

    @GET
    @Path("{metaTopologyId}")
    public MetaTopologyDTO getMetaTopology(@PathParam("metaTopologyId") String metaTopologyId) throws ServiceTemporarilyUnavailableException {
        final MetaTopologyDTO dto = getTopologyRestService().getMetaTopology(metaTopologyId);
        return dto;
    }

    @GET
    @Path("{metaTopologyId}/{namespace}")
    public GraphDTO getGraph(@PathParam("metaTopologyId") String metaTopologyId,
                             @PathParam("namespace") String namespace) throws ServiceTemporarilyUnavailableException {
        final GraphDTO graphDTO = getTopologyRestService().getGraph(metaTopologyId, namespace);
        return graphDTO;
    }

    private TopologyRestService getTopologyRestService() throws ServiceTemporarilyUnavailableException {
        return serviceLocator.findService(TopologyRestService.class);
    }
}
