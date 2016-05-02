package tech.derek.music25;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TreeTest
{
    private Note a = Note.valueOf(57, 60);
    private Note b = Note.valueOf(59, 60);
    private Note c = Note.valueOf(60, 60);
    private Note d = Note.valueOf(62, 60);
    private Note e = Note.valueOf(64, 60);

    @Before
    public void setUp() throws Exception
    {

    }

    @Test
    public void testWalk() throws InvalidMidiDataException, IOException
    {
        Note[] notes = new Note[] {a, b, e, c, d, e, a, a, d, a, d};

        Tree tree = new Tree(Arrays.asList(notes));
        List<Note> generated = tree.walk(40);
        FileUtil.toFile(generated, "assets/gen.midi");
    }

    @Test
    public void testCreateTree()
    {
        Note[] notes = new Note[] {a, b, e, c, d, e, a, a, d, a, d};
        Tree tree = new Tree(Arrays.asList(notes));

        // Check it has created a set
        Assert.assertEquals(5, tree.nodes.size());

        // Check the relative frequencies are correct
        for(Tree.Node node : tree.nodes)
        {
            if(node.note.equals(a))
                Assert.assertEquals(4f/11f, node.relFreq, 0.001f);

            if(node.note.equals(b))
                Assert.assertEquals(1f/11f, node.relFreq, 0.001f);

            if(node.note.equals(c))
                Assert.assertEquals(1f/11f, node.relFreq, 0.001f);

            if(node.note.equals(d))
                Assert.assertEquals(3f/11f, node.relFreq, 0.001f);
        }
    }

}