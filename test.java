import javax.sound.midi.*;
class DemoPlayer{
    /** We need four things: Sequencer, Sequence, Track, Midi Event
     * "Sequencer plays Sequence has a Track holds Midi event"
     */
    public static void main(String args[]){
        DemoPlayer mini = new DemoPlayer();
        if(args.length<2){
            System.out.println("Pass the instruments and note args.");
        }else{
            int InstruType = Integer.parseInt(args[0]);
            int note = Integer.parseInt(args[1]);
            mini.play(InstruType, note);
        }
    }
    
    public void play(int InstruType, int note){
        try{
            // Sequencer is the main part of a MIDI device.
            // It's like a CD player. It takes all Midi data and sends it to the right instruments
            // a static method of MidiSystem returns a sequencer instance
            Sequencer player = MidiSystem.getSequencer();
            player.open(); //opens the CD tray

            // You can imagine a Sequence as a music CD
            // Sequence is a data structure containing multiple tracks and timing info
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            // PPQ(Pulse per ticks) is used to specify timing 
            // type and 4 is the timing resolution.

            // Get a new Track from the Sequence
            // Track is a sequence of Midi events
            // Track is implemented with ArrayList
            Track t = seq.createTrack();
 
            // A MidiEvent comprises a Message object
            // Message says what to do and a MidiEvent says when to do
            // creating a Message
            ShortMessage InstruChange = new ShortMessage();
            // command/message type, channel, instrument type and velocity instructions are put in the message of type 192
            InstruChange.setMessage(192, 1, InstruType, 0); // always change instrument before note-playing
            // create a MidiEvent using the message
            MidiEvent changeInstru = new MidiEvent(InstruChange, 1);
            //MidiEvent constructor takes a message and beat info that says when to trigger the instruction
            // Add the MidiEvent to the track
            t.add(changeInstru);

            ShortMessage a = new ShortMessage();
            // In message of type 144, third argument tells which note to play
            a.setMessage(144, 1, note, 100); //velocity = 100 is a good default
            MidiEvent noteOn = new MidiEvent(a, 1);
            t.add(noteOn);

            ShortMessage b = new ShortMessage();
            b.setMessage(128, 1, note, 100);
            MidiEvent noteOff = new MidiEvent(b, 16);
            t.add(noteOff);

            player.setSequence(seq); //give the sequence to the sequencer like putting the CD in the CD player
            player.start(); //push the play button
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}

/**
 * References: https://www.geeksforgeeks.org/java-midi/
 *             Head First Java book
 */