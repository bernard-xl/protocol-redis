package xl.protocol.redis.client;

import xl.protocol.redis.message.Message;

import java.util.concurrent.CompletableFuture;

class RedisRequestContext<Request extends Message, Reply extends Message> {

    private Request body;

    private CompletableFuture<Reply> replySite;

    public RedisRequestContext(Request body) {
        this.body = body;
        this.replySite = new CompletableFuture<>();
    }

    public Message getBody() {
        return body;
    }

    public CompletableFuture<Reply> getReplySite() {
        return replySite;
    }
}
