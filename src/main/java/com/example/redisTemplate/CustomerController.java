package com.example.redisTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping(value = "/name", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Customer getCustomerName(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam("name") String name
    ) throws Exception {

        return customerService.getName(name);
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Customer saveCustomerName(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam("id") Long id,
                                        @RequestParam("name") String name
    ) throws Exception {

        Customer c = new Customer();
        c.setId(id);
        c.setUserName(name);

        return customerService.saveCache(c);
    }



    @RequestMapping(value = "/name2", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Customer getCustomerNameByMabatis(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @RequestParam("name") String name
    ) throws Exception {

        return customerService.getNameByMabatis(name);
    }



    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Customer> getCustomerListByMabatis(HttpServletRequest request,
                                                   HttpServletResponse response
    ) throws Exception {
        return customerService.getListByMabatis();
    }



    @RequestMapping(value = "/save3", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Customer saveCustomerNameByRedis(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @RequestParam("name") String name
    ) throws Exception {

        return customerService.saveNameByRedis(name);
    }

    @RequestMapping(value = "/name3", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Customer getCustomerNameByRedis(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @RequestParam("name") String name
    ) throws Exception {

        return customerService.getNameByRedis(name);
    }



    @RequestMapping(value = "/deleteRedisKey", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String deleteKeyByRedis(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                 @RequestParam("name") String name
    ) throws Exception {
        return customerService.deketeKeyByRedis(name);
    }

    @RequestMapping(value = "/list3", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Customer> getCustomerListByRedis(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                 @RequestParam("name") String name
    ) throws Exception {
        return customerService.getListByRedis(name);
    }


}
