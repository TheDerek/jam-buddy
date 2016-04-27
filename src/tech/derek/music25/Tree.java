package tech.derek.music25;

import java.util.*;
import java.util.stream.Collectors;

public class Tree
{
    public List<Node> nodes;

    public Tree(List<Note> notes)
    {
        // Create a set of all notes, these will be our roots for the trees
        nodes = notes.stream()
                // Create a set from the notes
                .distinct()
                // Turn the set of notes into root nodes
                .map(n -> new Node(n, relativeFrequency(notes, n)))
                // Return a list
                .collect(Collectors.toList());
    }

    /**
     * Populate the given nodes children based off of a list of notes.
     * @param notes - The score to find the children from.
     * @param node - The node to populate the children with.
     */
    private void populateChildren(List<Note> notes, Node node)
    {
       for(int i = 0; i < notes.size(); i++)
       {
           Note note = notes.get(i);

         
       }
    }

    private float relativeFrequency(List<Note> notes, Note note)
    {
        return (float) Collections.frequency(notes, note)
                / (float) notes.size();
    }

    public class Node
    {
        public final Note note;
        public final float relFreq;
        public final List<Node> children;

        Node(Note note, float relFreq)
        {
            this.note = note;
            this.relFreq = relFreq;
            children = new ArrayList<>();
        }
    }
}
