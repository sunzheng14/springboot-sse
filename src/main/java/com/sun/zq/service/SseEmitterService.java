package com.sun.zq.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author: sunzheng
 * @date:
 * @description:
 */
public interface SseEmitterService {
    /**
     * 创建SseEmitter
     * @return
     */
    SseEmitter createSession();

    /**
     * 关闭连接
     * @param clientId
     */
    void closeSession(String clientId);
}
