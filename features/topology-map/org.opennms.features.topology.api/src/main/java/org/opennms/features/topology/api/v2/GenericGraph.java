/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2016 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2016 The OpenNMS Group, Inc.
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

package org.opennms.features.topology.api.v2;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class GenericGraph {

    private final LinkedHashMap<String, GenericEdge> edgesById = new LinkedHashMap<>();

    private final LinkedHashMap<String, GenericVertex> verticesById = new LinkedHashMap<>();

    public void addEdge(GenericEdge edge) {
        edgesById.put(edge.getId(), edge);
    }

    public void addVertex(GenericVertex node) {
        verticesById.put(node.getId(), node);
    }

    public Collection<GenericEdge> getEdges() {
        return edgesById.values();
    }

    public Collection<GenericVertex> getVertices() {
        return verticesById.values();
    }

    public GenericVertex getVertexById(String id) {
        return verticesById.get(id);
    }

    public GenericEdge getEdgeById(String id) {
        return edgesById.get(id);
    }

//    @Override
//    public <T> T accept(GraphMLElementVisitor<T> visitor) {
//        return visitor.visit(this);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), edgesById, verticesById);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = super.equals(obj);
        if (equals) {
            if (obj instanceof GenericGraph) {
                GenericGraph other = (GenericGraph) obj;
                equals = Objects.equals(edgesById, other.edgesById) && Objects.equals(verticesById, other.verticesById);
                return equals;
            }
        }
        return false;
    }

    public void setVertices(List<GenericVertex> vertices) {
        verticesById.clear();
        vertices.stream().forEach(v -> addVertex(v));
    }

    public void setEdges(List<GenericEdge> edges) {
        edgesById.clear();
        edges.stream().forEach(e -> addEdge(e));
    }
}
