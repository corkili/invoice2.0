package org.hld.invoice.common.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 自定义Session监听器 SessionListener
 * @author 李浩然 2017年7月20日
 * @version 1.0
 */
public class SessionListener implements HttpSessionListener{
    private SessionContext sessionContext = SessionContext.getInstance();

    /**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        sessionContext.addSession(httpSessionEvent.getSession());
    }

    /**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        sessionContext.delSession(httpSessionEvent.getSession());
    }
}
