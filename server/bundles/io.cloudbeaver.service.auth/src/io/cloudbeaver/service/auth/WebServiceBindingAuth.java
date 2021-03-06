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
package io.cloudbeaver.service.auth;

import io.cloudbeaver.DBWebException;
import io.cloudbeaver.service.DBWBindingContext;
import io.cloudbeaver.service.WebServiceBindingBase;
import io.cloudbeaver.service.auth.impl.WebServiceAuthImpl;

/**
 * Web service implementation
 */
public class WebServiceBindingAuth extends WebServiceBindingBase<DBWServiceAuth> {

    private static final String SCHEMA_FILE_NAME = "schema/service.auth.graphqls";

    public WebServiceBindingAuth() {
        super(DBWServiceAuth.class, new WebServiceAuthImpl(), SCHEMA_FILE_NAME);
    }

    @Override
    public void bindWiring(DBWBindingContext model) throws DBWebException {
        model.getQueryType()
            .dataFetcher("authLogin", env -> getService(env).authLogin(
                getWebSession(env, false),
                env.getArgument("provider"),
                env.getArgument("credentials")))
            .dataFetcher("authLogout", env -> {
                getService(env).authLogout(getWebSession(env, false));
                return true;
            })
            .dataFetcher("sessionUser", env -> getService(env).sessionUser(getWebSession(env, false)))
            .dataFetcher("authProviders", env -> getService(env).getAuthProviders(getWebSession(env)))
        ;

    }
}
