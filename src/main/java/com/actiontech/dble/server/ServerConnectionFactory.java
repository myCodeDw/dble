/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese
 * opensource volunteers. you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Any questions about this component can be directed to it's project Web address
 * https://code.google.com/p/opencloudb/.
 *
 */
package com.actiontech.dble.server;

import com.actiontech.dble.DbleServer;
import com.actiontech.dble.config.ServerPrivileges;
import com.actiontech.dble.config.model.SystemConfig;
import com.actiontech.dble.net.FrontendConnection;
import com.actiontech.dble.net.factory.FrontendConnectionFactory;
import com.actiontech.dble.server.handler.ServerLoadDataInfileHandler;
import com.actiontech.dble.server.handler.ServerPrepareHandler;

import java.io.IOException;
import java.nio.channels.NetworkChannel;

/**
 * @author mycat
 */
public class ServerConnectionFactory extends FrontendConnectionFactory {

    @Override
    protected FrontendConnection getConnection(NetworkChannel channel) throws IOException {
        ServerConnection c = new ServerConnection(channel);
        DbleServer.getInstance().getConfig().setSocketParams(c, true);
        c.setPrivileges(ServerPrivileges.instance());
        c.setQueryHandler(new ServerQueryHandler(c));
        c.setLoadDataInfileHandler(new ServerLoadDataInfileHandler(c));
        c.setPrepareHandler(new ServerPrepareHandler(c));
        SystemConfig sys = DbleServer.getInstance().getConfig().getSystem();
        c.setTxIsolation(sys.getTxIsolation());
        c.setSession2(new NonBlockingSession(c));
        return c;
    }

}