/*
 * Copyright 2012 François Sarradin <fsarradin AT gmail DOT com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.kerflyn.broceliand.util;

import org.simpleframework.http.Cookie;
import org.simpleframework.http.Request;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Session {

    private final Request request;
    private final Cookie sessionCookie;
    private final SessionManager sessionManager;
    private final Map<String, String> data = new ConcurrentHashMap<String, String>();

    public Session(Request request, Cookie sessionCookie, SessionManager sessionManager) {
        this.request = request;
        this.sessionCookie = sessionCookie;
        this.sessionManager = sessionManager;
    }

    public Request getRequest() {
        return request;
    }

    public Cookie getSessionCookie() {
        return sessionCookie;
    }

    public String getIdentifier() {
        return sessionCookie.getValue();
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public String get(String key) {
        return data.get(key);
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public void close() {
        sessionManager.close(this);
    }

}
