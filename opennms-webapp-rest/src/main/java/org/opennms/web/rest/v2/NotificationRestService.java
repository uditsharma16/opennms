/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
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

package org.opennms.web.rest.v2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.ext.search.SearchBean;
import org.opennms.core.config.api.JaxbListWrapper;
import org.opennms.core.criteria.Alias.JoinType;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.core.criteria.restrictions.Restrictions;
import org.opennms.netmgt.dao.api.NotificationDao;
import org.opennms.netmgt.model.OnmsNotification;
import org.opennms.netmgt.model.OnmsNotificationCollection;
import org.opennms.web.rest.support.Aliases;
import org.opennms.web.rest.support.CriteriaBehavior;
import org.opennms.web.rest.support.CriteriaBehaviors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic Web Service using REST for {@link OnmsNotification} entity.
 *
 * @author <a href="seth@opennms.org">Seth Leger</a>
 */
@Component
@Path("notifications")
@Transactional
public class NotificationRestService extends AbstractDaoRestService<OnmsNotification,SearchBean,Integer,Integer> {

    @Autowired
    private NotificationDao m_dao;

    @Override
    protected NotificationDao getDao() {
        return m_dao;
    }

    @Override
    protected Class<OnmsNotification> getDaoClass() {
        return OnmsNotification.class;
    }

    @Override
    protected Class<SearchBean> getQueryBeanClass() {
        return SearchBean.class;
    }

    @Override
    protected CriteriaBuilder getCriteriaBuilder(UriInfo uriInfo) {
        final CriteriaBuilder builder = new CriteriaBuilder(OnmsNotification.class, Aliases.notification.toString());

        // 1st level JOINs
        builder.alias("event", Aliases.event.toString(), JoinType.LEFT_JOIN);
        builder.alias("node", Aliases.node.toString(), JoinType.LEFT_JOIN);
        builder.alias("serviceType", Aliases.serviceType.toString(), JoinType.LEFT_JOIN);

        // 2nd level JOINs
        builder.alias("node.assetRecord", Aliases.assetRecord.toString(), JoinType.LEFT_JOIN);
        // Left joins on a toMany relationship need a join condition so that only one row is returned
        builder.alias("node.ipInterfaces", Aliases.ipInterface.toString(), JoinType.LEFT_JOIN, Restrictions.or(Restrictions.eq(Aliases.ipInterface.prop("ipAddress"), Aliases.notification.prop("ipAddress")), Restrictions.isNull(Aliases.ipInterface.prop("ipAddress"))));
        builder.alias("node.location", Aliases.location.toString(), JoinType.LEFT_JOIN);

        // Order by ID by default
        builder.orderBy("notifyId").desc();

        return builder;
    }

    @Override
    protected Map<String,CriteriaBehavior<?>> getCriteriaBehaviors() {
        Map<String,CriteriaBehavior<?>> criteriaPropertiesMapping = new HashMap<>();

        // Root alias
        criteriaPropertiesMapping.putAll(CriteriaBehaviors.NOTIFICATION_BEHAVIORS);

        // 1st level JOINs
        criteriaPropertiesMapping.putAll(CriteriaBehaviors.EVENT_BEHAVIORS);
        criteriaPropertiesMapping.putAll(CriteriaBehaviors.NODE_BEHAVIORS);
        criteriaPropertiesMapping.putAll(CriteriaBehaviors.SERVICE_TYPE_BEHAVIORS);

        // 2nd level JOINs
        criteriaPropertiesMapping.putAll(CriteriaBehaviors.ASSET_RECORD_BEHAVIORS);
        criteriaPropertiesMapping.putAll(CriteriaBehaviors.IP_INTERFACE_BEHAVIORS);
        criteriaPropertiesMapping.putAll(CriteriaBehaviors.MONITORING_LOCATION_BEHAVIORS);

        return criteriaPropertiesMapping;
    }

    @Override
    protected JaxbListWrapper<OnmsNotification> createListWrapper(Collection<OnmsNotification> list) {
        return new OnmsNotificationCollection(list);
    }

    @Override
    protected OnmsNotification doGet(UriInfo uriInfo, Integer id) {
        return getDao().get(id);
    }

}
