package xl.protocol.redis.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import xl.protocol.redis.message.Message;

import java.util.ArrayDeque;
import java.util.concurrent.CompletableFuture;

class RedisRequestHandler extends ChannelOutboundHandlerAdapter {

    private ArrayDeque<CompletableFuture<Message>> replySites;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        replySites = ctx.channel().attr(RedisClient.ATTR_REPLY_SITE_QUEUE).get();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        RedisRequestContext request = (RedisRequestContext)msg;
        replySites.offer(request.getReplySite());
        ctx.writeAndFlush(request.getBody(), promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
