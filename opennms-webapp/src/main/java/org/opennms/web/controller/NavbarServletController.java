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

package org.opennms.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.opennms.web.navigate.DisplayStatus;
import org.opennms.web.navigate.Menu;
import org.opennms.web.navigate.MenuItem;
import org.opennms.web.navigate.NavBarEntry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NavbarServletController {

    private List<NavBarEntry> navbarItems;

    @RequestMapping("/navigation")
    public @ResponseBody String getNavigation(HttpServletRequest request) throws IOException {
        Menu menu = getMenu(request);
        ObjectMapper objectMapper = new JacksonJaxbJsonProvider().locateMapper(Menu.class, MediaType.APPLICATION_JSON_TYPE);
        String json = objectMapper.writeValueAsString(menu);
        return json;
    }

    private Menu getMenu(HttpServletRequest request) {
        Menu menu = new Menu();
        buildMenu(menu, navbarItems, request);
        return menu;
    }

    private static void buildMenu(Menu rootMenu, List<NavBarEntry> menuItems, HttpServletRequest request) {
        for (final NavBarEntry navBarEntry : menuItems) {
            final DisplayStatus displayStatus = navBarEntry.evaluate(request);
            if (displayStatus != DisplayStatus.NO_DISPLAY) {
                final MenuItem menuItem = new MenuItem();
                menuItem.setName(navBarEntry.getName());
                menuItem.setUrl(navBarEntry.getUrl());
                rootMenu.addItem(menuItem);

                if (navBarEntry.getEntries() != null) {
                    buildMenu(menuItem, navBarEntry.getEntries(), request);
                }
            }
        }
    }

    public void setNavbarItems(List<NavBarEntry> navbarItems) {
        this.navbarItems = navbarItems;
    }
}
