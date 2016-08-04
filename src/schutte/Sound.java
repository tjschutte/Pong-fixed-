package schutte;

import java.applet.Applet;
import java.applet.AudioClip;

/**
 *
 * @author Tom
 */
public class Sound {
public static final Sound paddleHit = new Sound("/PaddleHit.wav");
    private AudioClip clip;
    
    public Sound(String path) {
        clip = Applet.newAudioClip(Sound.class.getResource(path));
    }
    
    public void play() {
        clip.play();
    }
    
}
