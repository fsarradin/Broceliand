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

import org.junit.Test;
import org.simpleframework.http.Cookie;
import org.simpleframework.http.Request;

import static net.kerflyn.broceliand.util.SessionManagerImpl.BSESSIONID_COOKIE_NAME;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SessionManagerTest {

    private final Request request = mock(Request.class);
    private final SessionManager sessionManager = new SessionManagerImpl();

    @Test
    public void should_create_new_session() throws Exception {
        Session session = sessionManager.getOrCreateSession(request);

        assertThat(session).isNotNull();
    }

    @Test
    public void should_not_get_session_when_none() throws Exception {
        Session session = sessionManager.getSession(request);

        assertThat(session).isNull();
    }

    @Test
    public void should_get_session_when_present() throws Exception {
        Cookie sessionCookie = new Cookie(BSESSIONID_COOKIE_NAME, "hello");
        when(request.getCookie(BSESSIONID_COOKIE_NAME)).thenReturn(sessionCookie);

        sessionManager.getOrCreateSession(request);

        Session session = sessionManager.getSession(request);

        assertThat(session).isNotNull();
    }

    @Test
    public void should_close_session() throws Exception {
        Cookie sessionCookie = new Cookie(BSESSIONID_COOKIE_NAME, "hello");
        when(request.getCookie(BSESSIONID_COOKIE_NAME)).thenReturn(sessionCookie);

        Session session = sessionManager.getOrCreateSession(request);

        session.close();
    }

    @Test
    public void should_create_session_cookie_when_absent() {
        Cookie cookie = sessionManager.getOrCreateSessionCookie(request);

        assertThat(cookie.getName()).isEqualTo(BSESSIONID_COOKIE_NAME);
        assertThat(cookie.getValue()).isNotNull();
    }

    @Test
    public void should_get_session_cookie_when_present() {
        Cookie sessionCookie = new Cookie(BSESSIONID_COOKIE_NAME, "hello");
        when(request.getCookie(BSESSIONID_COOKIE_NAME)).thenReturn(sessionCookie);

        Cookie cookie = sessionManager.getOrCreateSessionCookie(request);

        assertThat(cookie.getName()).isEqualTo(BSESSIONID_COOKIE_NAME);
        assertThat(cookie.getValue()).isEqualTo("hello");
    }

    @Test
    public void should_get_only_session_cookie_when_present() {
        Cookie sessionCookie = new Cookie(BSESSIONID_COOKIE_NAME, "hello");
        when(request.getCookie(BSESSIONID_COOKIE_NAME)).thenReturn(sessionCookie);

        Cookie cookie = sessionManager.getSessionCookie(request);

        assertThat(cookie.getName()).isEqualTo(BSESSIONID_COOKIE_NAME);
        assertThat(cookie.getValue()).isEqualTo("hello");
    }

    @Test
    public void should_get_null_when_session_cookie_absent() {
        Cookie cookie = sessionManager.getSessionCookie(request);

        assertThat(cookie).isNull();
    }

}
