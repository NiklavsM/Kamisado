package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MusicPlayer {
	private Clip clip;
	GeneralSettingsManager manager;

	public void musicOn() throws Exception {
		AudioInputStream inputStream = AudioSystem
				.getAudioInputStream(getClass().getResource("/sound/backgroundmusic.wav"));
		if (clip != null) {
			clip.stop();
		}
		clip = AudioSystem.getClip();
		clip.open(inputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);

	}

	public void musicOff() {
		if (clip != null) {
			clip.stop();
		}
	}

	public void setVolume(int volume) {
		FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		floatControl.setValue(volume);
	}

}
