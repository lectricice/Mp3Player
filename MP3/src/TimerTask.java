

import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class TimerTask extends ScheduledService<Void> {
	private int i;
	private int sec;
	private SimpleIntegerProperty timeProgress = new SimpleIntegerProperty();
	
	@Override
	protected Task<Void> createTask(){
		return new Task<Void>(){
			
			@Override
			protected Void call() throws Exception{
				for(; i < sec; i++){
					updateProgress(i + 1, sec);
					int mins = (i % 3600 / 60);
					int secs = i % 60;
					String result = String.format("%02d", mins) + ";" + String.format("%02d", secs) + "/" + sec;
					int resultP = secs;
					updateMessage(result);
					timeProgress.set(resultP);
					Thread.sleep(1000);
				}
				this.succeeded();
				return null;
			}
		};
	}
	public void setSeconds(int sec){
		this.sec = sec;
	}
	
	public void setI(int i){
		this.i = i;
	}

}
