/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ricardo
 */

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.net.URL;

public class SoundClip {
    private AudioInputStream sample;
    private Clip clip; 
    private boolean looping = false;
    private int repeat = 0;
    private String filename = "";
    
    public SoundClip() {
        try {
                //crea el Buffer de sonido
                clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) { 

        }
    }
    public SoundClip(String filename) {
        //Llama al constructor default.
        this();
        //Carga el archivo de sonido.
        load(filename);
    }
    
    public Clip getClip() { 
	return clip; 
    }
    
    public void setLooping(boolean looping) {
	this.looping = looping; 
    }
    
    public boolean getLooping() {
        return looping;
    }
    
    public void setRepeat(int repeat) {
	this.repeat = repeat;
    }
    
    public int getRepeat() { 
	return repeat; 
    }
    
    public void setFilename(String filename) { 
	this.filename = filename; 
    }
    
    public String getFilename() { 
        return filename;
    }
    
    public boolean isLoaded() {
	return (boolean)(sample != null);
    }
    
    private URL getURL(String filename) {
        URL url = null;
        try {
                url = this.getClass().getResource(filename);
        }
        catch (Exception e) { 

        }
        return url;
    }
    
    public boolean load(String audiofile) {
        try {
                setFilename(audiofile);
                sample = AudioSystem.getAudioInputStream(getURL(filename));
                clip.open(sample);
                return true;

        } catch (IOException e) {
                return false;
        } catch (UnsupportedAudioFileException e) {
                return false;
        } catch (LineUnavailableException e) {
                return false;
        }
    }
    
    public void play() {
        //se sale si el sonido no a sido cargado
        if (!isLoaded()) 
                return;
        //vuelve a empezar el sound clip
        clip.setFramePosition(0);

        //Reproduce el sonido con repeticion opcional.
        if (looping)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        else
            clip.loop(repeat);
    }
    
    public void stop() {
	clip.stop();
    }
    
}

