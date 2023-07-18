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
    private ConcurrentHashMap<SseEmitter, String> SSE_CACHE = new ConcurrentHashMap<>();

    @Override
    public synchronized SseEmitter  createSession() {
        // 过期时间设置为0 ，表示永不过期
        SseEmitter sseEmitter = new SseEmitter(0L);

        // 浏览器断开连接后，清除sse连接
        sseEmitter.onCompletion(() -> {
            SSE_CACHE.remove(sseEmitter);
            log.info("客户端:{}，连接关闭，当前客户端总数为：{}", sseEmitter, SSE_CACHE.size());
        });

        String id = UUID.randomUUID().toString().replaceAll("-", "");
        if (!SSE_CACHE.containsKey(id)) {
            SSE_CACHE.put(sseEmitter, id);
            log.info("客户端:{}，建立连接成功，当前客户端总数为：{}", id, SSE_CACHE.size());
        }

        return sseEmitter;
    }

     @Scheduled(cron = "0/2 * * * * ?")
     public void job() {
        if (SSE_CACHE.size() > 0) {
            String msg = UUID.randomUUID().toString();
            log.info("当前客户端总数为：{}，推送消息：{}", SSE_CACHE.size(), msg);
            SSE_CACHE.forEach((k, v) -> {
                try {
                    k.send(SseEmitter.event().reconnectTime(1000L).id(v).data(msg));
                } catch (Exception e) {
                    log.error("客户端:{}推送消息失败，原因：{}", k, e.getMessage());
                }
            });

        }

     }
}
