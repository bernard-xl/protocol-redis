package xl.protocol.redis.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import xl.protocol.redis.codec.RedisDecoder;
import xl.protocol.redis.codec.RedisEncoder;

import java.util.ArrayDeque;

class RedisClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.attr(RedisClient.ATTR_REPLY_SITE_QUEUE).set(new ArrayDeque<>());
        ch.pipeline()
                .addLast(new RedisDecoder())
                .addLast(new RedisReplyHandler())
                .addLast(new RedisEncoder())
                .addLast(new RedisRequestHandler());
    }
}
