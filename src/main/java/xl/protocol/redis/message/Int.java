package xl.protocol.redis.message;

import java.util.Objects;

public class Int implements Message {

    private long content;

    public Int(long content) {
        this.content = content;
    }

    public long content() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Int anInt = (Int) o;
        return content == anInt.content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return Long.toString(content);
    }
}
