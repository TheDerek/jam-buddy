package tech.derek.music25;

import org.junit.Assert;
import org.junit.Test;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class FileUtilTest
{
    @Test
    public void readMIDI() throws InvalidMidiDataException, IOException
    {
        List<Note> notes = FileUtil.readMIDI("assets/scale.midi");

        Assert.assertEquals(48, notes.get(0).key);
        Assert.assertEquals(50, notes.get(1).key);
        Assert.assertEquals(65, notes.get(10).key);
    }

    @Test
    public void toSequence() throws InvalidMidiDataException, IOException
    {
        List<Note> notes1 = FileUtil.readMIDI("assets/scale.midi");
        List<Note> notes2 = FileUtil.toList(FileUtil.toSequence(notes1));

        // Verify that the two list of notes match
        Assert.assertEquals(notes1.size(), notes2.size());
        for(int x = 0; x < notes1.size(); x++)
        {
            Assert.assertEquals(notes1.get(x), notes2.get(x));
        }
    }

    @Test
    public void toFile() throws InvalidMidiDataException, IOException
    {
        List<Note> notes1 = FileUtil.readMIDI("assets/scale.midi");

        // Save it to another file
        FileUtil.toFile(notes1, "test.midi");

        // Load it again
        List<Note> notes2 = FileUtil.readMIDI("test.midi");

        // Cleanup
        new File("test.midi").delete();

        // Verify that the two list of notes match
        Assert.assertEquals(notes1.size(), notes2.size());
        for(int x = 0; x < notes1.size(); x++)
        {
            Assert.assertEquals(notes1.get(x), notes2.get(x));
        }
    }
}