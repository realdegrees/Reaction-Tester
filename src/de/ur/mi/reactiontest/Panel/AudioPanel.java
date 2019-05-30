package de.ur.mi.reactiontest.Panel;

import de.ur.mi.reactiontest.Enums.PanelType;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

public class AudioPanel extends PanelProperties {

    private MidiChannel noteHandler;

    public AudioPanel(JPanel jPanel) {
        super(PanelType.AUDIO, jPanel);
        initSynthesizer();
    }

    @Override
    public void start() {
        isRunning = true;
        initScheduler();
        runAudioTest();
    }

    @Override
    protected boolean checkInputValidity(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_ENTER || keyEvent.getKeyCode() == KeyEvent.VK_SPACE;
    }

    @Override
    public void stop() {
        reset();
    }

    private void runAudioTest() {
        if (!isRunning) {
            return;
        }
        scheduler.schedule(new TimerTask() {
            @Override
            public void run() {
                playSound();
                timer.startTimer();
            }
        }, getRandomDelay());
    }

    private void initSynthesizer() {
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

            Instrument instrument = synthesizer.getAvailableInstruments()[5];
            noteHandler = synthesizer.getChannels()[0];

            synthesizer.loadInstrument(instrument);
        }catch (MidiUnavailableException e){
            e.printStackTrace();
        }
    }

    private void playSound(){
        noteHandler.noteOn(80, 400);
    }
}
