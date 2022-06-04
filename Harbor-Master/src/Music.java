import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Music implements Runnable {
	Thread t;
	File audioFile;
	AudioInputStream audioStream;
	Clip audioClip;
	String fn;

	public Music(String fileName, boolean loops) {
		fn = "src/audio/" + fileName;
		audioFile = new File(fn);
		try {
			audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			audioClip = (Clip) AudioSystem.getLine(info);

			if (loops) {
				audioClip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			audioClip.open(audioStream);
			// audioClip.start();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void quit() {
		audioClip.close();
		audioClip.stop();
	}

	public void play() {
		start3();
	}

	public void start3() {
		t = new Thread(this, fn);
		start2();
		t.start();
	}

	public void start() {
		t = new Thread(this, fn);
		t.start();
	}

	public void start2() {
		audioFile = new File(fn);
		try {
			audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.start();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void run() {
		audioClip.start();
	}

}