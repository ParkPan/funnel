package com.cloutropy.platform.funnel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class FodQueue {
    private static FodQueue ourInstance = new FodQueue();

    public static FodQueue getInstance() {
        return ourInstance;
    }

    private JedisPool pool;
    private final static Logger logger = LoggerFactory.getLogger(FodQueue.class);

    private FodQueue() {
    }

    public boolean initialize(){
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(100);
            config.setMaxWaitMillis(1000);
            pool = new JedisPool(config, "redis-mq-file.ys-internal.com", 6379);
        } catch (Exception e){
            logger.error("op=[FodQueue::intialize] cause=[{}]",e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addMessage(String msg){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.lpush("MQ_FILE_ON_DEMAND_0",msg);
        }catch (Exception e){
            logger.error("op=[Fod::addMessage] cause=[{}] info=[{}]",e.getMessage(),msg);
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return true;
    }
}
