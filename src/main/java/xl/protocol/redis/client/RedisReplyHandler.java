package xl.protocol.redis.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xl.protocol.redis.exception.RedisClientException;
import xl.protocol.redis.message.Err;
import xl.protocol.redis.message.Message;

import java.util.ArrayDeque;
import java.util.concurrent.CompletableFuture;

class RedisReplyHandler extends SimpleChannelInboundHandler<Message> {

    private ArrayDeque<CompletableFuture<Message>> replySites;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        replySites = ctx.channel().attr(RedisClient.ATTR_REPLY_SITE_QUEUE).get();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        CompletableFuture<Message> replySite = replySites.poll();
        if (msg instanceof Err) {
            Err err = (Err)msg;
            replySite.completeExceptionally(new RedisClientException(err.content()));
        } else {
            replySite.complete(msg);
        }
    }
}
