package tech.derek.music25;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileUtil
{
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;

    private FileUtil()
    {
    }

    public static List<Note> readMIDI(String path) throws InvalidMidiDataException, IOException
    {
        Sequence sequence = MidiSystem.getSequence(new File(path));
        return toList(sequence);
    }

    public static List<Note> toList(Sequence sequence)
    {
        List<List<Note>> tracks = new ArrayList<>();

        for (Track track : sequence.getTracks())
        {
            List<TimestampNote> notes = new ArrayList<>();
            long[] pressedKeys = new long[128];

            for (int i = 0; i < track.size(); i++)
            {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                if (message instanceof ShortMessage)
                {
                    ShortMessage shortMessage = (ShortMessage) message;
                    int key = shortMessage.getData1();
                    int velocity = shortMessage.getData2();

                    // Indicate that the current key is pressed and store the
                    // tick at which is was pressed.
                    if (velocity > 0 && shortMessage.getCommand() == NOTE_ON)
                    {
                        pressedKeys[key] = event.getTick();
                    }
                    // A key has been released, create a note for the released
                    // key.
                    else if (velocity == 0 || shortMessage.getCommand() == NOTE_OFF)
                    {
                        // Add a timestamped note to the note list, we
                        // can sort the notes later by their timestamp
                        long noteStartingTime = pressedKeys[key];
                        long noteLength = event.getTick() - noteStartingTime;
                        Note note = Note.valueOf(key, noteLength);
                        notes.add(new TimestampNote(note, noteStartingTime));
                    }
                }
            }

            // Sort the notes according to when they were first played
            Collections.sort(notes);
            tracks.add(notes.stream().map(tn -> tn.note).collect(Collectors.toList()));
        }

        // Return the biggest track (hopefully the one containing notes)
        return tracks.stream().sorted((l1, l2) -> Integer.compare(l2.size(), l1.size())).findFirst().get();
    }

    public static Sequence toSequence(List<Note> notes) throws InvalidMidiDataException
    {
        // Create a MIDI sequence
        Sequence s = new Sequence(javax.sound.midi.Sequence.PPQ, 24);

        // Get a track from that sequence
        Track t = s.createTrack();

        //****  General MIDI sysex -- turn on General MIDI sound set  ****
        byte[] b = {(byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7};
        SysexMessage sm = new SysexMessage();
        sm.setMessage(b, 6);
        MidiEvent me = new MidiEvent(sm, (long) 0);
        t.add(me);

        //****  set tempo (meta event)  ****
        MetaMessage mt = new MetaMessage();
        byte[] bt = {0x01, (byte) 0x00, 0x00};
        mt.setMessage(0x51, bt, 3);
        me = new MidiEvent(mt, (long) 0);
        t.add(me);

        //****  set track name (meta event)  ****
        mt = new MetaMessage();
        String TrackName = "midifile track";
        mt.setMessage(0x03, TrackName.getBytes(), TrackName.length());
        me = new MidiEvent(mt, (long) 0);
        t.add(me);


        /* set omni on
        ShortMessage mm = new ShortMessage();
        mm.setMessage(0xB0, 0x7D, 0x00);
        me = new MidiEvent(mm, (long) 0);
        t.add(me);

        // set poly on
        mm = new ShortMessage();
        mm.setMessage(0xB0, 0x7F, 0x00);
        me = new MidiEvent(mm, (long) 0);
        t.add(me);

        // set instrument to Piano
        mm = new ShortMessage();
        mm.setMessage(0xC0, 0x00, 0x00);
        me = new MidiEvent(mm, (long) 0);
        t.add(me);*/

        long lastTick = 1L;
        for(Note note : notes)
        {

            long length = note.length / 2L;
            //****  note on - middle C  ****
            ShortMessage msg = new ShortMessage();
            msg.setMessage(0x90, note.key, note.velocity);
            me = new MidiEvent(msg, lastTick);
            t.add(me);

            //****  note off - middle C - 120 ticks later  ****
            msg = new ShortMessage();
            msg.setMessage(0x80, note.key, 0x40);
            me = new MidiEvent(msg, lastTick + length);
            t.add(me);

            lastTick += length;
        }

        //****  set end of track (meta event) 19 ticks later  ****
        mt = new MetaMessage();
        byte[] bet = {}; // empty array
        mt.setMessage(0x2F, bet, 0);
        me = new MidiEvent(mt, lastTick + 10L);
        t.add(me);

        s.getTickLength();
        return s;
    }

    public static void toFile(List<Note> notes, String path) throws InvalidMidiDataException, IOException
    {
        File f = new File(path);
        MidiSystem.write(toSequence(notes), 1, f);
    }

    private static class TimestampNote implements Comparable<TimestampNote>
    {
        final long start;
        final Note note;

        private TimestampNote(Note note, long start)
        {
            this.start = start;
            this.note = note;
        }

        @Override
        public int compareTo(TimestampNote o)
        {
            return Long.compare(this.start, o.start);
        }
    }
}
