package bichromate.audio;

import java.io.File;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.zip.ZipException;

import javax.media.MediaException;
import bichromate.core.sTestOSInformationFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;

@SuppressWarnings("restriction")
public class sTestPlayAudioFactory {
	
	private static ResourceBundle resources;
	private sTestOSInformationFactory path = null;
	@SuppressWarnings("unused")
	private String wavSample = null;
	private String mp3Sample = null;
	private MediaPlayer mediaPlayer = null;
	
	static
	 {
			try
			{
				resources = ResourceBundle.getBundle("audio.sTestPlayAudioFactory",Locale.getDefault());
			} catch (MissingResourceException mre) {
				System.out.println("sTestPlayAudioFactory.properties not found: "+mre);
				System.exit(0);
			}
	 }

	
	 @SuppressWarnings("unused")
	public sTestPlayAudioFactory(){
		 
		 path = new sTestOSInformationFactory();
		 wavSample = new String(resources.getString("sTestPlayAudioFactory.testWav"));
		 mp3Sample = new String(resources.getString("sTestPlayAudioFactory.testMP3"));
		 
		 final JFXPanel fxPanel = new JFXPanel();
		  
	 }
	public void playWavFile(String file){
		
	}
	public void playMP3File(String file){
		
	}
	public void playWavFileFromPropertiesFile(){
		
	}
	public void playMP3FileFromPropertiesFile(){
		
	}
	private void playFile(String bip) throws MediaException {
		
		
		Media hit = new Media(new File(bip).toURI().toString());
		
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		if(null != mediaPlayer)
			mediaPlayer.play();
	}
	public void stopMediaPlayer(){
		if(null != mediaPlayer)
			mediaPlayer.stop();
	}
	private void test(){
		String fileToPlay = new String(path.getAudioDirectory()+path.fileSeperator()+mp3Sample);
		try{
			playFile(fileToPlay);
		}catch (MediaException e){
			System.out.println("Error reading file: " +fileToPlay);
		}
		
	}
	 //
	 // Inner class for testing on the command line
	 //
	 public static class Test
	 {
		 public static void main(String[] args) throws ZipException {
			 
			 sTestPlayAudioFactory pAudio = new sTestPlayAudioFactory();
			 if(pAudio != null){
				 pAudio.test();
			 }
			 
		 }//main
	 }// Test
}// sTestPlayAudioFactory
