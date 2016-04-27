package tech.derek.music25;

import java.util.Arrays;
import java.util.Objects;

public final class Note
{
    public final int key;
    public final Note.State state;

    private Note(int key, State state)
    {
        this.key = key;
        this.state = state;
    }

    public static Note valueOf(int key, State state)
    {
        return new Note(key, state);
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Note))
            return false;

        return ((Note) object).key == this.key
                && ((Note) object).state == this.state;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(new Object[]{key, state});
    }

    public enum State {
        On, Off
    }
}
