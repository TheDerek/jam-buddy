package tech.derek.music25;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Core
{
    public static void main(String args[]) throws InvalidMidiDataException, IOException, InterruptedException
    {
        // Setup our stuff for creating midi files and reading user input
        Scanner reader = new Scanner(System.in);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        dateFormat.setTimeZone(timeZone);

        // Remove old files from the user directory
        for(File file: new File("assets/user").listFiles()) file.delete();

        // Remove old files from the generation directory
        for(File file: new File("assets/gen").listFiles()) file.delete();

        int port = Integer.parseInt(args[0]);

        while(true)
        {
            // Save user input
            String filename = "assets/user/" + dateFormat.format(new Date()) + ".midi";

            System.out.println("Accepting user input, click enter when user is finished.");
            Process p = Runtime.getRuntime().exec("arecordmidi -p " + port + " " + filename);

            // Wait for the user to click enter before ending the recording
            // session
            System.in.read();
            p.destroy();

            // Load our base music
            String[] files = Files.walk(Paths.get("assets/user"))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .toArray(String[]::new);

            // Get our list of notes from the base music
            List<Note> notes = getNotesFromFiles(files);

            // Generate the tree
            Tree tree = new Tree(notes);

            // Generate the music to play back to the user
            List<Note> gen = tree.walk(40);

            // Save the music
            filename = "assets/gen/" + dateFormat.format(new Date()) + ".midi";
            FileUtil.toFile(gen, filename);

            //p = Runtime.getRuntime().exec("fluidsynth -a alsa -m alsa_seq -l -i /usr/share/soundfonts/FluidR3_GM2-2.sf2 " + filename);
            p = Runtime.getRuntime().exec("timidity " + filename);
            p.waitFor();
        }



    }

    private static List<Note> getNotesFromFiles(String... files) throws IOException
    {
        List<Note> notes = new ArrayList<>();

        for(int x = 0; x < files.length; x++)
        {
            String file = files[x];
            System.out.println("Parsing file " + x + " / " + files.length + ", File: " + file);
            List<Note> fileNotes = null;
            try
            {
                fileNotes = FileUtil.readMIDI(file, 40);
            } catch (InvalidMidiDataException e)
            {
                System.out.println("Invalid midi file: " + file);
            }
            notes.addAll(fileNotes);
        }

        return notes;
    }

}
