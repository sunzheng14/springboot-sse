package com.sun.zq.controller;

import com.sun.zq.service.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: sunzheng
 * @date:
 * @description:
 */
@RestController
@RequestMapping("/sse/test")
public class SseController {
    @Autowired
    private SseEmitterService sseEmitterService;

    @GetMapping("/start")
    public Object createSession() {
        return sseEmitterService.createSession();
    }

}
