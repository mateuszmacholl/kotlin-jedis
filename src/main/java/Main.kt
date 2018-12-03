
import redis.clients.jedis.Jedis


fun main(args: Array<String>) {
    val jedis = Jedis("localhost")
    println("Server is running: " + jedis.ping())
    jedis.set("kotlin-redis", "My redis app");
    jedis.del("kotlin-redis")
    System.out.println("Stored string in redis: "+ jedis.get("kotlin-redis"))
}
