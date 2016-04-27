package tech.derek.music25;

import javax.sound.midi.*;
import java.util.Scanner;
import java.util.Vector;

public class Core
{
    public static void main(String args[])
    {
        Scanner reader = new Scanner(System.in);
        Vector synthInfos;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        System.out.println("Please select device:");
        for(int x = 0; x < infos.length; x++)
        {
            MidiDevice.Info info = infos[x];
            System.out.println(x + 1 + ". " + info + " - " + info.getDescription());
        }

        int deviceNum = reader.nextInt();
        MidiDevice device = null;
        try
        {
            device = MidiSystem.getMidiDevice(infos[deviceNum - 1]);
        } catch (MidiUnavailableException e)
        {
            System.out.println("Error, the device you have selected is not available.");
            e.printStackTrace();
        }

        for(Transmitter transmitter : device.getTransmitters())
        {
            System.out.println(transmitter.toString());
            transmitter.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));
        }

        try
        {
            device.open();
        } catch (MidiUnavailableException e)
        {
            e.printStackTrace();
        }
    }

    public static class MidiInputReceiver implements Receiver
    {
        public String name;
        public MidiInputReceiver(String name)
        {
            this.name = name;
        }
        public void send(MidiMessage msg, long timeStamp)
        {
            System.out.println("midi received");
        }
        public void close() {}
    }
}
