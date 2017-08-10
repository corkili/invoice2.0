package org.hld.invoice.common.session;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Session的上下文数据结构
 * @author 李浩然 2017年7月20日
 * @version 1.0
 */
public class SessionContext {
	/**
	 * 用户ID属性名
	 */
    public static final String ATTR_USER_ID = "userId";

    public static final String ATTR_USER_NAME = "name";

    /**
     * 静态成员，单例
     */
    private static SessionContext instance;
    
    /**
     * 服务端维护的Session集合，若未登录，键值对为（sessionId,session），登录后，键值对为（userId,session）
     */
    private Map<String, HttpSession> sessionMap;

    /**
     * 私有构造方法
     */
    private SessionContext() {
        sessionMap = new HashMap<>();
    }

    /**
     * 获取单例对象的工厂方法
     * @author 李浩然
     * @return
     */
    public static SessionContext getInstance() {
        if(instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    /**
     * 添加一个session
     * @author 李浩然
     * @param session
     */
    public synchronized void addSession(HttpSession session) {
        if (session != null) {
            sessionMap.put(session.getId(), session);
        }
    }

    /**
     * 删除一个session
     * @author 李浩然
     * @param session
     */
    public synchronized void delSession(HttpSession session) {
        if(session != null) {
            sessionMap.remove(session.getId());
            if(session.getAttribute(ATTR_USER_ID) != null) {
                sessionMap.remove(session.getAttribute(ATTR_USER_ID).toString());
            }
        }
    }

    /**
     * 根据sessionId，获取一个session
     * @author 李浩然
     * @param sessionId
     * @return
     */
    public synchronized HttpSession getSession(String sessionId) {
        if(sessionId == null)
            return null;
        return sessionMap.get(sessionId);
    }

    /**
     * 获取sessionMap
     * @author 李浩然
     * @return sessionMap
     */
    public Map<String, HttpSession> getSessionMap() {
        return sessionMap;
    }

    /**
     * 设置一个sessionMap
     * @author 李浩然
     * @param sessionMap 待设置的sessionMap
     */
    public void setSessionMap(Map<String, HttpSession> sessionMap) {
        this.sessionMap = sessionMap;
    }
    
    public void sessionHandlerByCacheMap(HttpSession session) {
        synchronized (this) {
            String userId = session.getAttribute(SessionContext.ATTR_USER_ID).toString();
            HttpSession userSession = (HttpSession) sessionMap.get(userId);
            if (userSession != null) {
                // 注销在线用户
                userSession.invalidate();
                sessionMap.remove(userId);
                // 清楚在线用户后，更新map，替换map
                sessionMap.remove(session.getId());
                sessionMap.put(userId, session);
            } else {
                // 根据当前sessionId取session对象，更新map
            	sessionMap.put(userId, sessionMap.get(session.getId()));
            	sessionMap.remove(session.getId());
            }
        }
    }
}
