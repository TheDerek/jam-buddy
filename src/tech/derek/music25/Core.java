package tech.derek.music25;

import javax.sound.midi.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Core
{
    public static void main(String args[]) throws InvalidMidiDataException, IOException
    {
        List<Note> notes = FileUtil.readMIDI("assets/bach_bourree.mid");
        Tree tree = new Tree(notes);
        List<Note> gen = tree.walk(40);
        FileUtil.toFile(gen, "assets/gen.midi");
    }

}
