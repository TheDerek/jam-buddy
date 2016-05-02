package tech.derek.music25;

import java.util.Arrays;
import java.util.Objects;

public final class Note
{
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public final int key;
    public final long length;
    public final int velocity = 70;
    private final int hash;


    private Note(int key, long length)
    {
        this.key = key;
        this.length = length;
        this.hash = Arrays.hashCode(new Object[]{key, length});
    }

    public static Note valueOf(int key, long length)
    {
        return new Note(key, length);
    }

    @Override
    public String toString()
    {
        int octave = (key / 12)-1;
        int note = key % 12;
        String noteName = NOTE_NAMES[note];
        return "Length: " + length + ", Note: " + noteName + octave + ", Key: " + key +  ", Velocity: " + velocity;
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Note))
            return false;

        return ((Note) object).key == this.key
                && ((Note) object).length == this.length;
    }

    @Override
    public int hashCode() {return hash; }
}
