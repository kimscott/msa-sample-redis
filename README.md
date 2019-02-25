
-- 데이터 넣기  
http http://localhost:8280/customers userName=kimkook

http http://localhost:8280/customers/test/name?name=kimkook

-- jpa 및 redis 테스트  
http http://localhost:8280/test/name\?name\=kimkook


-- redis 에 데이터 확인  
kubectl exec -it redis -- redis-cli
> keys *


-- mybatis 테스트  

http http://localhost:8280/test/name2\?name\=kimkook

http http://localhost:8280/test/list2



--- redis key 직접 컨트롤  

http http://localhost:8280/test/save3\?name\=kimkook  
http http://localhost:8280/test/name3\?name\=kimkook  
http http://localhost:8280/test/deleteRedisKey\?name\=kimkook  
http http://localhost:8280/test/list3\?name\=kim  


-- redis pubsub test  

http http://localhost:8280/pubsub/send\?message\=hello  

RedisMessageSubscriber 의 onMessage 에서 메세지가 오는지 확인  

-- redis-cli에서 messageQueue 채널을 구독하여, 메시지가 오는지 확인   
> pubsub channels            :  채널 목록을 확인  
> subscribe messageQueue  : messageQueue 채널을 구독  
