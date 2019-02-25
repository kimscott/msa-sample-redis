package com.example.redisTemplate.pubsub;

import com.example.redisTemplate.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/pubsub")
public class PubSubTestController {

    @Autowired
    RedisMessagePublisher redisMessagePublisher;

    @RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void sendMessage(@RequestParam("message") String message ) throws Exception {
        redisMessagePublisher.publish(message);
    }
}
