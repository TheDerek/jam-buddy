package tech.derek.music25;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class Chain
{
    private final Map<Note, Float> map;
    private final int length;

    public Chain(int length)
    {
        this.length = length;
        map = new HashMap<>();
    }

    private Chain(Map<Note, Float> map, int length)
    {
        this.length = length;
        this.map = map;
    }

    /**
     * Add an element to the chain.
     * @param note - The note to add to this chain.
     */
    public void add(Note note)
    {
        if(map.containsKey(note))
            map.put(note, map.get(note) + 1f);
        else
            map.put(note, 1f);
    }

    /**
     * Normalise this chain. Essentially calculate the probability for each note
     * to occur in this chain.
     *
     * @param length - The length of the corpus that was passed into the chain.
     */
    public Chain normalise(int length)
    {
        return new Chain(map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() / (float) length
                )), length);
    }
}
