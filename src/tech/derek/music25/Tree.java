package tech.derek.music25;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tree
{
    private static final Random random = new Random();
    public final List<Node> nodes;

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

        // Populate the set of nodes with children
        nodes.stream().forEach(n -> populateChildren(notes, n));
        System.out.println("Total Notes: " + notes.size() + ", Unique Notes: " + nodes.size());
    }

    // Walk along the tree and generate a series of notes
    public List<Note> walk(int length)
    {
        List<Note> song = new ArrayList<>(length);
        Note note = nodes.get(random.nextInt(nodes.size())).note;

        do {
            song.add(note);
            length--;
            note = nextNote(note);
        } while(length > 0);

        return song;
    }

    /**
     * Combine this tree with another tree
     * @param other the tree to combine with.
     */
    public void merge(Tree other)
    {

    }

    private Note nextNote(Note note)
    {
        Optional<Node> node = nodes.stream()
                .filter(n -> n.note.equals(note))
                .findFirst();

        if(!node.isPresent())
            throw new NoSuchElementException("The note you have given is not" +
                    " present in this tree");

        if(node.get().children.isEmpty())
            return nodes.get(random.nextInt(nodes.size())).note;

        float probability = random.nextFloat();
        float cumulative = 0;

        for(Node child : node.get().children)
        {
            cumulative += child.relFreq;
            if(cumulative >= probability)
                return child.note;
        }

        throw new NoSuchElementException("This nodes probabilities do not add " +
                "up to one.");
    }

    /**
     * Populate the given nodes children based off of a list of notes.
     *
     * @param notes - The score to find the children from.
     * @param node  - The node to populate the children with.
     */
    private void populateChildren(List<Note> notes, Node node)
    {
        float occurrences = 0;

        for (int i = 0; i < notes.size() - 1; i++)
        {
            Note note = notes.get(i);

            // Add the next note to this note to the nodes children
            if (note.equals(node.note))
            {
                occurrences += 1;
                Note nextNote = notes.get(i + 1);

                // Check if  nextNote already exists in tree or add a new note
                // if it does not
                Optional<Node> possibleNode = node.children.stream()
                        .filter(n -> n.note.equals(nextNote))
                        .findFirst();

                if (possibleNode.isPresent())
                    possibleNode.get().relFreq += 1;
                else
                    node.children.add(new Node(nextNote, 1));
            }
        }

        // Done populating children, can change their frequencies into
        // relative frequencies
        for(Node child : node.children)
            child.relFreq /= occurrences;

    }

    private float relativeFrequency(List<Note> notes, Note note)
    {
        return (float) Collections.frequency(notes, note)
                / (float) notes.size();
    }

    public class Node
    {
        final Note note;
        float relFreq;
        final List<Node> children;

        Node(Note note, float relFreq)
        {
            this.note = note;
            this.relFreq = relFreq;
            children = new ArrayList<>();
        }

        @Override
        public String toString()
        {
            return note.toString() + ", Probability: " +
                    String.format("%.02f", relFreq);
        }
    }
}
