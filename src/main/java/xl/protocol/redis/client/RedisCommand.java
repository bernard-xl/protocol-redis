package xl.protocol.redis.client;

import xl.protocol.redis.message.Message;

import java.util.List;

import static xl.protocol.redis.message.Message.array;
import static xl.protocol.redis.message.Message.bulk;

public class RedisCommand {
    private RedisCommand() {
    }

    // String commands

    public static Message append(String key, String value) {
        return array(bulk("APPEND"), bulk(key), bulk(value));
    }

    public static Message append(String key, byte[] value) {
        return array(bulk("APPEND"), bulk(key), bulk(value));
    }

    public static Message get(String key) {
        return array(bulk("GET"), bulk(key));
    }

    public static Message set(String key, String value) {
        return array(bulk("SET"), bulk(key), bulk(value));
    }

    public static Message set(String key, byte[] value) {
        return array(bulk("SET"), bulk(key), bulk(value));
    }

    public static Message getSet(String key, String value) {
        return array(bulk("GETSET"), bulk(key), bulk(value));
    }

    public static Message mget(String key, String...otherKeys) {
        Message[] msgs = new Message[2 + otherKeys.length];
        msgs[0] = bulk("MGET");
        msgs[1] = bulk(key);

        for (int i = 2; i < msgs.length; i++) {
            msgs[i] = bulk(otherKeys[i - 3]);
        }

        return array(msgs);
    }

    public static Message mset(String key, String value, String...keyValues) {
        Message[] msgs = new Message[3 + keyValues.length];
        msgs[0] = bulk("MSET");
        msgs[1] = bulk(key);
        msgs[2] = bulk(value);

        for (int i = 3; i < msgs.length; i++) {
            msgs[i] = bulk(keyValues[i - 3]);
        }

        return array(msgs);
    }

    public static Message mset(String key, byte[] value, byte[]...keyValues) {
        Message[] msgs = new Message[3 + keyValues.length];
        msgs[0] = bulk("MSET");
        msgs[1] = bulk(key);
        msgs[2] = bulk(value);

        for (int i = 3; i < msgs.length; i++) {
            msgs[i] = bulk(keyValues[i - 3]);
        }

        return array(msgs);
    }

    // Bit commands

    public static Message getBit(String key, long offset) {
        return array(bulk("GETBIT"), bulk(key), bulk(offset));
    }

    public static Message setBit(String key, long offset, long value) {
        return array(bulk("SETBIT"), bulk(key), bulk(offset), bulk(value));
    }

    public static Message bitCount(String key) {
        return array(bulk("BITCOUNT"), bulk(key));
    }

    public static Message bitCount(String key, long start, long end) {
        return array(bulk("BITCOUNT"), bulk(key), bulk(start), bulk(end));
    }

    public static Message bitOp(String operation, String dest, String src) {
        return array(bulk("BITOP"), bulk(operation), bulk(src), bulk(dest));
    }

    public static Message bitOp(String operation, String dest, String src1, String...srcN) {
        Message[] msgs = new Message[4 + srcN.length];
        msgs[0] = bulk("BITOP");
        msgs[1] = bulk(operation);
        msgs[2] = bulk(dest);
        msgs[3] = bulk(src1);

        for (int i = 4; i < msgs.length; i++) {
            msgs[i] = bulk(srcN[i - 4]);
        }

        return array(msgs);
    }

    public static Message bitPos(String key, long bit) {
        return array(bulk(key), bulk(bit));
    }

    public static Message bitPos(String key, long bit, long start) {
        return array(bulk(key), bulk(bit), bulk(start));
    }

    public static Message bitPos(String key, long bit, long start, long end) {
        return array(bulk(key), bulk(bit), bulk(start), bulk(end));
    }

    // List commands

    public static Message lPop(String key) {
        return array(bulk("LPOP"), bulk(key));
    }

    public static Message lPush(String key, String value) {
        return array(bulk("LPUSH"), bulk(key), bulk(value));
    }

    public static Message lPush(String key, byte[] value) {
        return array(bulk("LPUSH"), bulk(key), bulk(value));
    }

    public static Message lPush(String key, String value, String...otherValues) {
        Message[] msgs = new Message[3 + otherValues.length];
        msgs[0] = bulk("LPUSH");
        msgs[1] = bulk(key);
        msgs[2] = bulk(value);

        for (int i = 3; i < msgs.length; i++) {
            msgs[i] = bulk(otherValues[i - 3]);
        }

        return array(msgs);
    }

    public static Message lPush(String key, byte[] value, byte[]...otherValues) {
        Message[] msgs = new Message[3 + otherValues.length];
        msgs[0] = bulk("LPUSH");
        msgs[1] = bulk(key);
        msgs[2] = bulk(value);

        for (int i = 3; i < msgs.length; i++) {
            msgs[i] = bulk(otherValues[i - 3]);
        }

        return array(msgs);
    }

    public static Message blPop(String key, long timeout) {
        return array(bulk("BLPOP"), bulk(key), bulk(timeout));
    }

    public static Message blPop(List<String> keys, long timeout) {
        Message[] msgs = new Message[2 + keys.size()];
        msgs[0] = bulk("BLPOP");

        int i = 1;
        for (String k : keys) {
            msgs[i++] = bulk(k);
        }
        msgs[i] = bulk(timeout);

        return array(msgs);
    }

    public static Message brPop(String key, long timeout) {
        return array(bulk("BRPOP"), bulk(key), bulk(timeout));
    }

    public static Message brPop(List<String> keys, long timeout) {
        Message[] msgs = new Message[2 + keys.size()];
        msgs[0] = bulk("BRPOP");

        int i = 1;
        for (String k : keys) {
            msgs[i++] = bulk(k);
        }
        msgs[i] = bulk(timeout);

        return array(msgs);
    }


    // Counter commands

    public static Message incr(String key) {
        return array(bulk("INCR"), bulk(key));
    }

    public static Message decr(String key) {
        return array(bulk("DECR"), bulk(key));
    }

    public static Message incrBy(String key, long value) {
        return array(bulk("INCRBY"), bulk(key), bulk(value));
    }

    public static Message decrBy(String key, long value) {
        return array(bulk("DECRBY"), bulk(key), bulk(value));
    }

    // General commands

    public static Message ping() {
        return array(bulk("PING"));
    }

    public static Message echo(String value) {
        return array(bulk("ECHO"), bulk(value));
    }

    public static Message del(String key) {
        return array(bulk("DEL"), bulk(key));
    }

    public static Message del(String key, String...otherKeys) {
        Message[] msgs = new Message[2 + otherKeys.length];
        msgs[0] = bulk("DEL");
        msgs[1] = bulk(key);

        for (int i = 2; i < msgs.length; i++) {
            msgs[i] = bulk(otherKeys[i - 2]);
        }

        return array(msgs);
    }
}
