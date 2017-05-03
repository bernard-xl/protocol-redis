package xl.protocol.redis.message;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Array implements Message {

    public static final Array NULL = new Array(null);

    private Message[] content;

    public Array(Message[] content) {
        this.content = content;
    }

    public int length() {
        return (content == null)? -1 : content.length;
    }

    public Message[] content() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Array array = (Array) o;
        return Arrays.equals(content, array.content);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(content);
    }

    @Override
    public String toString() {
        return Stream.of(content)
                .map(Message::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
