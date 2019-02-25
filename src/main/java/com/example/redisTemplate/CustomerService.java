package com.example.redisTemplate;

import com.example.redisTemplate.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    private RedisTemplate redisTemplate;



    @Cacheable(value = "app", key = "#userName")
    public Customer getName(String userName){
        LOGGER.info("find name from jdbc {}", userName);
        return customerRepository.findByUserName(userName);
    }

    @CachePut(value = "app", key = "#customer.userName")
    @Transactional
    public Customer saveCache(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * use mybatis
     * @param userName
     * @return
     */
    public Customer getNameByMabatis(String userName){
        LOGGER.info("find name from mybatis jdbc {}", userName);
        return customerMapper.findByUserNameMybatis(userName);
    }

    /**
     * use mybatis
     * @return
     */
    public List<Customer> getListByMabatis(){
        LOGGER.info("find list from mybatis jdbc");
        return customerMapper.findAllByMybatis();
    }

    /**
     * 레디스를 사용하는 방법중에 데이터 저장 공간으로 활용을 할 수가 있다.
     * 이렇게 생성된 데이터는 삭제를 하기 전까지는 데이터가 남아있는다.
     */
    public Customer saveNameByRedis(String userName){
        Customer c = new Customer();
        c.setUserName(userName);
        c.setFirstName(userName);

        String key = "app2::"+userName;
        redisTemplate.opsForValue().set(key, c);


        return c;
    }
    /**
     * @param userName
     * @return
     */
    public Customer getNameByRedis(String userName){

        String key = "app2::"+userName;
        Customer value = (Customer)redisTemplate.opsForValue().get(key);

        return value;
    }

    /**
     * redis data delete
     * @return
     */
    public String deketeKeyByRedis(String userName){
        String key = "app2::"+userName;
        redisTemplate.delete(key);

        // delete multi keys
//        String key = "app2::*"+userName+"*";
//        Set<String> keys = redisTemplate.keys(key);
//        redisTemplate.delete(keys);

        return "ok";
    }


    public List<Customer> getListByRedis(String userName){

        // like 조회
        String key = "app2::*"+userName+"*";
        Set<String> keys = redisTemplate.keys(key);
        List<Customer> value = (List<Customer>)redisTemplate.opsForValue().multiGet(keys);

        return value;
    }
}

