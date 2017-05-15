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

package org.opennms.features.topology.rest.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.features.topology.api.TopologyService;
import org.opennms.features.topology.api.topo.MetaTopologyProvider;
import org.opennms.features.topology.api.v2.GenericEdge;
import org.opennms.features.topology.api.v2.GenericGraph;
import org.opennms.features.topology.api.v2.GenericVertex;
import org.opennms.features.topology.rest.api.EdgeDTO;
import org.opennms.features.topology.rest.api.GraphDTO;
import org.opennms.features.topology.rest.api.MetaTopologyDTO;
import org.opennms.features.topology.rest.api.TopologyRestService;
import org.opennms.features.topology.rest.api.VertexDTO;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.google.common.base.Strings;

public class TopologyRestServiceImpl implements TopologyRestService {

    private final TopologyService topologyService;

    private final TopologyConverterDispatcher converterDispatcher;

    private final BundleContext bundleContext;

    public TopologyRestServiceImpl(TopologyService topologyService, TopologyConverterDispatcher converterDispatcher, BundleContext bundleContext) {
        this.topologyService = Objects.requireNonNull(topologyService);
        this.converterDispatcher = Objects.requireNonNull(converterDispatcher);
        this.bundleContext = Objects.requireNonNull(bundleContext);
    }

    @Override
    public List<MetaTopologyDTO> getMetaTopologies() {
        final List<MetaTopologyDTO> metaTopologyList = topologyService.getMetaTopologyProviders().stream().map(metaTopologyProvider -> {
            MetaTopologyDTO dto = convert(metaTopologyProvider);
            return dto;
        }).collect(Collectors.toList());
        return metaTopologyList;
    }

    @Override
    public MetaTopologyDTO getMetaTopology(String metaTopologyId) {
        MetaTopologyProvider metaTopologyProvider = topologyService.getMetaTopologyProvider(metaTopologyId);
        MetaTopologyDTO metaTopologyDTO = convert(metaTopologyProvider);
        return metaTopologyDTO;
    }

    @Override
    public GraphDTO getGraph(String metaTopologyId, String namespace) {
//        final GraphProvider graphProvider = topologyService.getGraphProvider(metaTopologyId, namespace);
        final GenericGraph genericGraph = converterDispatcher.convert(namespace); // TODO MVR metaTopologyId/namespace ?!

        final GraphDTO graphDTO = new GraphDTO();
//        graphDTO.setNamespace(graphProvider.getNamespace());
//        graphDTO.setPreferredLayout(graphProvider.getDefaults().getPreferredLayout());
//        graphDTO.setDefaultSemanticZoomLevel(graphProvider.getDefaults().getSemanticZoomLevel());


        for(GenericVertex eachVertex : genericGraph.getVertices()) {
            VertexDTO convertedVertex = new VertexDTO();
            convertedVertex.setProperties(eachVertex.getProperties());
            graphDTO.addVertex(convertedVertex);
        }

        for (GenericEdge eachEdge : genericGraph.getEdges()) {
            EdgeDTO convertedEdge = new EdgeDTO();
            convertedEdge.setProperties(eachEdge.getProperties());
            graphDTO.addEdge(convertedEdge);
        }

        return graphDTO;
    }

    private MetaTopologyDTO convert(MetaTopologyProvider metaTopologyProvider) {
        final MetaTopologyDTO dto = new MetaTopologyDTO();
        dto.setId(metaTopologyProvider.getId());
        dto.setBreadcrumbStrategy(metaTopologyProvider.getBreadcrumbStrategy().name());
        dto.setDefaultNamespace(metaTopologyProvider.getDefaultGraphProvider().getNamespace());
        dto.setNamespaces(metaTopologyProvider.getGraphProviders().stream().map(p -> p.getNamespace()).collect(Collectors.toList()));

        final String label = getLabel(metaTopologyProvider);
        if (label != null) {
            dto.setLabel(label);
        } else {
            dto.setLabel(dto.getId());
        }
        return dto;
    }

    private String getLabel(MetaTopologyProvider metaTopologyProvider) {
        try {
            final Collection<ServiceReference<MetaTopologyProvider>> serviceReferences = bundleContext.getServiceReferences(MetaTopologyProvider.class, null);
            for (ServiceReference<MetaTopologyProvider> eachServiceReference : serviceReferences) {
                if (eachServiceReference != null && Arrays.asList(eachServiceReference.getPropertyKeys()).contains("label")) {
                    final MetaTopologyProvider otherProvider = bundleContext.getService(eachServiceReference);
                    if (otherProvider != null) {
                        if (metaTopologyProvider.getId().equals(otherProvider.getId())) {
                            String label = (String) eachServiceReference.getProperty("label");
                            if (!Strings.isNullOrEmpty(label)) {
                                return label;
                            }
                        }
                    }
                }

            }
        } catch (InvalidSyntaxException e) {
            // TODO MVR log
//            LOG.warn("Invalid syntax", e);
        }
        return null;
    }

}
