package xl.protocol.redis.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import xl.protocol.redis.exception.RedisClientException;
import xl.protocol.redis.message.Message;

import java.util.ArrayDeque;
import java.util.concurrent.CompletableFuture;

public class RedisClient implements AutoCloseable {

    static final AttributeKey<ArrayDeque<CompletableFuture<Message>>> ATTR_REPLY_SITE_QUEUE = AttributeKey.valueOf("reply-sites-queue");

    private EventLoopGroup group;

    private Channel channel;

    public RedisClient(String hostName, int port) {
        Bootstrap bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();

        bootstrap
                .channel(NioSocketChannel.class)
                .group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .remoteAddress(hostName, port)
                .handler(new RedisClientInitializer());

        try {
            ChannelFuture future = bootstrap.connect().sync();
            if (future.isSuccess()) {
                channel = future.channel();
            } else {
                throw new RedisClientException(future.cause());
            }
        } catch (InterruptedException e) {
            throw new RedisClientException(e);
        }
    }

    public <Request extends Message, Reply extends Message> CompletableFuture<Reply> request(Request message) {
        RedisRequestContext<Request, Reply> request = new RedisRequestContext<>(message);
        CompletableFuture<Reply> replySite = request.getReplySite();
        channel.writeAndFlush(request).addListener(f -> {
            if (!f.isSuccess()) {
                replySite.completeExceptionally(f.cause());
            }
        });
        return replySite;
    }

    @Override
    public void close() throws InterruptedException {
        if (group != null) {
            group.shutdownGracefully().sync();
        }
    }
}
