package tech.derek.music25;

import java.util.Arrays;
import java.util.Objects;

public final class Note
{
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

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
    public String toString()
    {
        int octave = (key / 12)-1;
        int note = key % 12;
        String noteName = NOTE_NAMES[note];
        return "State: " + state.toString() + ", Note: " + noteName + octave + ", Key: " + key;
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
