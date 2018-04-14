package cn.itcast.bos.redis.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class JedisTest {
    @Test
    public void testRedis(){
        // 连接localhost  默认端口6379
        Jedis jedis = new Jedis("localhost");
        jedis.setex("test",30,"heimacehngxuyuan");
        System.out.println(jedis.get("test"));

    }
}
