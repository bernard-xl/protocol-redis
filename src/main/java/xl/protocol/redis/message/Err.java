package xl.protocol.redis.message;

import java.util.Objects;

public class Err implements Message {

    private String content;

    public Err(String content) {
        this.content = content;
    }

    public String content() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Err err = (Err) o;
        return Objects.equals(content, err.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return content;
    }
}
