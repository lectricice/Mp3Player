import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Playlist {
	
	private String name;
	private String path;
	private ArrayList<Track> trackList;
	
	public Playlist(String name, String path){
		this.name = name;
		this.path = path;
		trackList = new ArrayList<Track>();
		createPlaylist();
	}
	private String getName(){
		return name;
	}
	
	private void SetName(String name){
		this.name = name;
	}
	
	private ArrayList<Track> getTrackList(){
		return trackList;
	}
	protected String getPath(){
		return path;
	}
	
	public ArrayList<Track> get(){
		return trackList;
	}
	public Track prevTrack(Track track){
		for (int i = trackList.size()-1; i > 0 ; i--){
			if(trackList.get(i) == track){
				return trackList.get(i - 1);
			}
		} 
		if(trackList.get(0)== track){
			return trackList.get(trackList.size()-1);
		}
		return null;
	}
	
	public Track nextTrack(Track track){
		boolean isTrack = false;
		for (int i = 0; i < trackList.size(); i++){
			if(isTrack)
				return trackList.get(i);
				if(trackList.get(i) == track){
					isTrack = true;
			}
		}if(trackList.get(trackList.size()-1) == track){
			return trackList.get(0);
		}
		return null;
	}
	
	private void setTrackList(ArrayList<Track> trackList){
		this.trackList = trackList;
	}
	protected Track getTrack(String song){
		for(Track aktTrack : trackList){
			System.out.println(aktTrack.getName() + " ?= " + song);
			if(song.equals(aktTrack.getName())){
				return aktTrack;
			}
		} return null;
	}
	private boolean addSong(Track e){

		trackList.add(e);
		return true;
	}
	protected boolean clear(){
		trackList.clear();
		return true;
	}
	public void createPlaylist(){
		File file = new File(path);
		this.loadPlaylist(file);
	}

	private void loadPlaylist(File file){
		Mp3File mp3file;
		File [] f = file.listFiles();
		
		for(File aktFile : f){
			if(aktFile.exists())
				if(aktFile.isDirectory())loadPlaylist(aktFile);
					else if (aktFile.getName().endsWith(".mp3")){
						try {		
							mp3file = new Mp3File(aktFile);
							ID3v1 id3v1Tag = mp3file.getId3v1Tag();
							Track track = new Track(id3v1Tag.getTitle(), id3v1Tag.getArtist(), id3v1Tag.getAlbum(), mp3file.getLengthInSeconds());
							track.absolutePath(aktFile.getAbsolutePath());
							addSong(track);			
						} catch (UnsupportedTagException | InvalidDataException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		}
    }
}
