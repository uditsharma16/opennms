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

package org.opennms.features.topology.api.v2.converter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.GraphProvider;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.v2.GenericEdge;
import org.opennms.features.topology.api.v2.GenericGraph;
import org.opennms.features.topology.api.v2.GenericVertex;

public class SimpleGraphConverter<V extends Vertex, E extends Edge> implements GraphConverter {

    private final GraphProvider graphProvider;

    public SimpleGraphConverter(GraphProvider graphProvider) {
        this.graphProvider = Objects.requireNonNull(graphProvider);
    }

    @Override
    public GenericGraph convert() {
        final GenericGraph genericGraph = new GenericGraph();
//        genericGraph.setNamespace(provider.getNamespace());
//        genericGraph.setPreferredSemanticZoomLevel(provider.getDefaults().getSemanticZoomLevel());
//        genericGraph.setPreferredLayout(provider.getDefaults().getPreferredLayout());

        final List<GenericVertex> convertedVertices = graphProvider.getVertices().stream()
                .map(v -> (V) v)
                .map(v -> convert(v))
                .filter(v -> v != null)
                .collect(Collectors.toList());

        final List<GenericEdge> convertedEdges = graphProvider.getEdges().stream()
                .map(e -> (E) e)
                .map(e -> convert(e, genericGraph))
                .filter(e -> e != null)
                .collect(Collectors.toList());

        genericGraph.setVertices(convertedVertices);
        genericGraph.setEdges(convertedEdges);

        return genericGraph;
    }

    @Override
    public String getNamespace() {
        return graphProvider.getNamespace();
    }

    @Override
    public boolean contributesTo(String namespace) {
        return getNamespace().equals(namespace);
    }

    protected GenericVertex convert(V vertex) {
        return getVertexConverter().convert(vertex);
    }

    protected GenericEdge convert(E edge, GenericGraph graph) {
        return getEdgeConverter().convert(edge, graph);
    }

    protected VertexConverter<V> getVertexConverter() {
        return new SimpleVertexConverter<>();
    }

    protected EdgeConverter<E> getEdgeConverter() {
        return new SimpleEdgeConverter<>();
    }

}
