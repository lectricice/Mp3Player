import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;

import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.collections.FXCollections;

public class MP3Player {
		
		private SimpleMinim minim;
		private SimpleAudioPlayer audioPlayer;
		protected boolean isPause = false;
		private Playlist pList = new Playlist("pList1" , "../MP3/");
		private Track aktiveTrack = pList.get().get(0);
		private SimpleObjectProperty<Track> aktiveTrackProp = new SimpleObjectProperty<Track>(); 
		protected int millis = 0;
		protected boolean isPlaying = false;
		private SimpleIntegerProperty timeProgress = new SimpleIntegerProperty();
		private SimpleIntegerProperty time = new SimpleIntegerProperty();
	
	public MP3Player(){
		minim = new SimpleMinim(true);
		
	}
	public void play(String fileName){
		audioPlayer = minim.loadMP3File(fileName);
		audioPlayer.play();
		System.out.println(aktiveTrack.getName());
		isPause = false;
	}
	public void play(){
		minim.stop();
		audioPlayer = minim.loadMP3File(( aktiveTrack).getAbsolutePath());
		audioPlayer.play(millis);
		System.out.println(aktiveTrack.getName());
		isPause = false;
		isPlaying = true;
	}
	public SimpleIntegerProperty getTime(){
		return time;
	}
	
	public void pause(){
		if(isPause == false){
			audioPlayer.pause();
			millis = audioPlayer.position();
			isPause = true;
			isPlaying = false;
		} else {
			play();
		}
	}
	public void stop(){
		minim.stop();
		millis = 0;
		isPlaying = false;
	}
	public void gain(float value){
		audioPlayer.setGain(value);
		System.out.println(audioPlayer.getGain());
	}
	
	public void mute(){
		audioPlayer.mute();
	}
	
	public void unmute(){
		audioPlayer.unmute();
	}
	public void balance(float value){
		audioPlayer.setBalance(value);
	}
	public Playlist getPlaylist(){
		return pList;
	}
	public void skip(){
		
		Track tempTrack = pList.nextTrack(aktiveTrack);
			if(tempTrack != null){
				minim.stop();
				aktiveTrack = tempTrack;
				aktiveTrackProp.set(aktiveTrack);
			}
			if(isPlaying)
				play(aktiveTrack.getAbsolutePath());
				aktiveTrackProp.set(aktiveTrack);
	}
	public void skipBack(){
			Track tempTrack = pList.prevTrack(aktiveTrack);
			if(tempTrack != null){
				minim.stop();
				aktiveTrack = tempTrack;
				aktiveTrackProp.set(aktiveTrack);
			}
			if(isPlaying)
				play(aktiveTrack.getAbsolutePath());
				aktiveTrackProp.set(aktiveTrack);
	}
	public Track getAktiveTrack(){
		return aktiveTrack;
	} 	
	public SimpleObjectProperty<Track> getSOP(){
		return aktiveTrackProp;
	}
	public SimpleIntegerProperty getProgress(){
		return new SimpleIntegerProperty(audioPlayer.position());
	}
}