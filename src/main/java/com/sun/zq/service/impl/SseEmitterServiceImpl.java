package com.sun.zq.service.impl;

import com.sun.zq.service.SseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author: sunzheng
 * @date:
 * @description:
 */
@Service
@Slf4j
public class SseEmitterServiceImpl implements SseEmitterService {
    private ConcurrentHashMap<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();

    @Override
    public synchronized SseEmitter  createSession() {
        // 过期时间设置为0 ，表示永不过期
        SseEmitter sseEmitter = new SseEmitter(0L);
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        if (!SSE_CACHE.containsKey(id)) {
            SSE_CACHE.put(id, sseEmitter);
            log.info("客户度:{}建立连接成功，当前客户端总数为：{}", id, SSE_CACHE.size());
        }

        return sseEmitter;
    }

    @Override
    public void closeSession(String clientId) {
        if (SSE_CACHE.containsKey(clientId)) {
            SSE_CACHE.get(clientId).complete();
            SSE_CACHE.remove(clientId);
            log.info("客户度:{}关闭连接成功，当前剩余客户端总数为：{}", clientId, SSE_CACHE.size());
        }
     }

     @Scheduled(cron = "0/1 * * * * ?")
     public void job() {
        if (SSE_CACHE.size() > 0) {
            String msg = "消息:" + UUID.randomUUID().toString();
            log.info("当前客户端总数为：{}，推送消息：{}", SSE_CACHE.size(), msg);
            SSE_CACHE.forEach((k, v) -> {
                try {
                    v.send(SseEmitter.event().reconnectTime(1000L).id(k).data(msg));
                } catch (Exception e) {
                    log.error("客户端:{}推送消息失败，原因：{}", k, e.getMessage());
                }
            });

        }

     }
}
