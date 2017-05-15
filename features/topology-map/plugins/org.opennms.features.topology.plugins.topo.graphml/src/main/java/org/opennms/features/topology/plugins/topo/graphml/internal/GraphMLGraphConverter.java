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

package org.opennms.features.topology.plugins.topo.graphml.internal;

import org.opennms.features.topology.api.v2.GenericEdge;
import org.opennms.features.topology.api.v2.GenericVertex;
import org.opennms.features.topology.api.v2.Properties;
import org.opennms.features.topology.api.v2.converter.EdgeConverter;
import org.opennms.features.topology.api.v2.converter.SimpleGraphConverter;
import org.opennms.features.topology.api.v2.converter.VertexConverter;
import org.opennms.features.topology.plugins.topo.graphml.GraphMLEdge;
import org.opennms.features.topology.plugins.topo.graphml.GraphMLTopologyProvider;
import org.opennms.features.topology.plugins.topo.graphml.GraphMLVertex;

public class GraphMLGraphConverter extends SimpleGraphConverter<GraphMLVertex, GraphMLEdge> {

    public GraphMLGraphConverter(GraphMLTopologyProvider provider) {
        super(provider);
    }

    @Override
    protected VertexConverter<GraphMLVertex> getVertexConverter() {
        return input -> {
            final GenericVertex output = new GenericVertex();
            output.setProperties(input.getProperties());
            output.setProperty(Properties.LEVEL, input.getLevel());
            return output;
        };
    }

    @Override
    protected EdgeConverter<GraphMLEdge> getEdgeConverter() {
        return (input, graph) -> {
            final GenericEdge output = new GenericEdge();
            output.setProperties(input.getProperties());

            GenericVertex sourceVertex = graph.getVertexById(input.getSource().getId());
            GenericVertex targetVertex = graph.getVertexById(input.getTarget().getId());
            if (sourceVertex != null) {
                output.setSource(sourceVertex);
            }
            if (targetVertex != null) {
                output.setTarget(targetVertex);
            }
            return output;
        };
    }
}
