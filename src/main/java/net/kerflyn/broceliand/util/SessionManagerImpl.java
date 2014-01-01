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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class SessionManagerImpl implements SessionManager {

    public static final String BSESSIONID_COOKIE_NAME = "BSESSIONID";

    private final Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    public Cookie getSessionCookie(Request request) {
        return request.getCookie(BSESSIONID_COOKIE_NAME);
    }

    public Cookie getOrCreateSessionCookie(Request request) {
        Cookie cookie = getSessionCookie(request);
        if (cookie != null) {
            return cookie;
        }

        return createCookie();
    }

    private Cookie createCookie() {
        UUID uuid = UUID.randomUUID();
        String value = uuid.toString();

        return new Cookie(BSESSIONID_COOKIE_NAME, value, true);
    }

    public Session getOrCreateSession(Request request) {
        Cookie sessionCookie = getOrCreateSessionCookie(request);

        String uuid = sessionCookie.getValue();

        if (!sessions.containsKey(uuid)) {
            Session session = new Session(request, sessionCookie, this);
            sessions.put(uuid, session);

            return session;
        }

        return new Session(request, sessionCookie, this);
    }

    public Session getSession(Request request) {
        Cookie sessionCookie = getSessionCookie(request);
        if (sessionCookie == null) {
            return null;
        }

        String uuid = sessionCookie.getValue();

        return sessions.get(uuid);
    }

    public void close(Session session) {
        sessions.remove(session.getIdentifier());
    }
}
