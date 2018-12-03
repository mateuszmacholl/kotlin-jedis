
import redis.clients.jedis.Jedis
import java.util.*





fun main(args: Array<String>) {
    val jedis = Jedis("localhost")
    println("Server is running: " + jedis.ping())

    jedis.set("kotlin-redis", "My redis app");
    System.out.println("Stored string in redis: "+ jedis.get("kotlin-redis"))

    jedis.lpush("my-list", "A")
    jedis.lpush("my-list", "B")
    jedis.lpush("my-list", "C")
    val list = jedis.lrange("my-list", 0, 2)
    list.forEach { println("Stored string in redis: $it")}

    val keys = jedis.keys("*")
    keys.forEach { println("Key: $it") }

    jedis.sadd("nicknames", "nickname#1")
    jedis.sadd("nicknames", "nickname#2")
    jedis.sadd("nicknames", "nickname#1")
    val nicknames = jedis.smembers("nicknames")
    val exists = jedis.sismember("nicknames", "nickname#1")
    println("Nicknames: $nicknames")
    println("Exists: $exists")

    jedis.hset("user#1", "name", "Mateusz")
    jedis.hset("user#1", "job", "programmer")
    val name = jedis.hget("user#1", "name")
    val fields = jedis.hgetAll("user#1")
    val job = fields["job"]
    println("Fields: $fields")
    println("Name: $name")
    println("Job: $job")

    val scores = HashMap<String, Double>()
    scores["One"] = 3000.0
    scores["Two"] = 1500.0
    scores["Three"] = 8200.0
    scores.entries.forEach { score -> jedis.zadd("ranking", score.value, score.key) }
    val player = jedis.zrevrange("ranking", 0, 1)
    val rank = jedis.zrevrank("ranking", "One")
    println("Player $player")
    println("Rank $rank")

    val friendsPrefix = "friends#"
    val userOneId = "4352523"
    val userTwoId = "5552321"
    val t = jedis.multi()
    t.sadd(friendsPrefix + userOneId, userTwoId)
    t.sadd(friendsPrefix + userTwoId, userOneId)
    t.exec()

    val idOne = "4352523"
    val idTwo = "4849888"
    val p = jedis.pipelined()
    p.sadd("searched#$idOne", "paris")
    p.zadd("ranking", 126.0, idOne)
    p.zadd("ranking", 325.0, idTwo)
    val pipeExists = p.sismember("searched#$idOne", "paris")
    val pipeRanking = p.zrange("ranking", 0, -1)
    p.sync()
    val pipe = pipeExists.get()
    val ranking = pipeRanking.get()
    println("Pipe: $pipe")
    println("Ranking: $ranking")

    jedis.del("my-list")
    jedis.del("kotlin-redis")
}
