/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2020 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudbeaver.service.admin.impl;

import io.cloudbeaver.DBWebException;
import io.cloudbeaver.model.session.WebSession;
import io.cloudbeaver.model.user.WebPermission;
import io.cloudbeaver.model.user.WebRole;
import io.cloudbeaver.model.user.WebUser;
import io.cloudbeaver.server.CBPlatform;
import io.cloudbeaver.service.admin.AdminPermissionInfo;
import io.cloudbeaver.service.admin.AdminRoleInfo;
import io.cloudbeaver.service.admin.AdminUserInfo;
import io.cloudbeaver.service.admin.DBWServiceAdmin;
import org.jkiss.code.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Web service implementation
 */
public class WebServiceAdmin implements DBWServiceAdmin {


    @NotNull
    @Override
    public List<AdminUserInfo> listUsers(@NotNull WebSession webSession, String userName) throws DBWebException {
        try {
            List<AdminUserInfo> webUsers = new ArrayList<>();
            for (WebUser user : CBPlatform.getInstance().getApplication().getSecurityController().findUsers(userName)) {
                webUsers.add(new AdminUserInfo(user));
            }
            return webUsers;
        } catch (Exception e) {
            throw new DBWebException("Error reading users", e);
        }
    }

    @NotNull
    @Override
    public List<AdminRoleInfo> listRoles(@NotNull WebSession webSession, String roleName) throws DBWebException {
        try {
            List<AdminRoleInfo> webUsers = new ArrayList<>();
            for (WebRole role : CBPlatform.getInstance().getApplication().getSecurityController().findRoles(roleName)) {
                webUsers.add(new AdminRoleInfo(role));
            }
            return webUsers;
        } catch (Exception e) {
            throw new DBWebException("Error reading users", e);
        }
    }

    @NotNull
    @Override
    public List<AdminPermissionInfo> listPermissions(@NotNull WebSession webSession) throws DBWebException {
        try {
            List<AdminPermissionInfo> permissionInfos = new ArrayList<>();
            for (WebPermission permission : CBPlatform.getInstance().getApplication().getSecurityController().getAllPermissions()) {
                permissionInfos.add(new AdminPermissionInfo(permission));
            }
            return permissionInfos;
        } catch (Exception e) {
            throw new DBWebException("Error reading users", e);
        }
    }

    @NotNull
    @Override
    public AdminUserInfo createUser(@NotNull WebSession webSession, String userName) throws DBWebException {
        try {
            WebUser newUser = new WebUser(userName);
            CBPlatform.getInstance().getApplication().getSecurityController().createUser(newUser);
            return new AdminUserInfo(newUser);
        } catch (Exception e) {
            throw new DBWebException("Error reading users", e);
        }
    }

    @Override
    public boolean deleteUser(@NotNull WebSession webSession, String userName) throws DBWebException {
        try {
            CBPlatform.getInstance().getApplication().getSecurityController().deleteUser(userName);
            return true;
        } catch (Exception e) {
            throw new DBWebException("Error reading users", e);
        }
    }

    @NotNull
    @Override
    public AdminRoleInfo createRole(@NotNull WebSession webSession, String roleName) throws DBWebException {
        throw new DBWebException("Feature not supported");
    }

    @Override
    public boolean deleteRole(@NotNull WebSession webSession, String roleName) throws DBWebException {
        throw new DBWebException("Feature not supported");
    }

    @Override
    public boolean grantUserRole(@NotNull WebSession webSession, String user, String role) throws DBWebException {
        throw new DBWebException("Feature not supported");
    }

    @Override
    public boolean revokeUserRole(@NotNull WebSession webSession, String user, String role) throws DBWebException {
        throw new DBWebException("Feature not supported");
    }

    @Override
    public boolean setRolePermissions(@NotNull WebSession webSession, String roleID, String[] permissions) throws DBWebException {
        throw new DBWebException("Feature not supported");
    }
}
