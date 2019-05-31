package il.co.ilrd.crud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Observable;

public class Monitor extends Observable{
	
    private final Path syslogDir;
    private WatchService watchService;
	private WatchKey key;
	private boolean isWorking;
	private File syslog;
	/************************Ctor*****************************/

	public Monitor(Path path, File file) throws IOException {
		if (!Files.isDirectory(path)) {throw new FileNotFoundException();}
		syslog = file;
		syslogDir = path;
		watchService = FileSystems.getDefault().newWatchService();
		
		syslogDir.register(watchService, 
		            StandardWatchEventKinds.ENTRY_CREATE, 
		              StandardWatchEventKinds.ENTRY_DELETE, 
		                StandardWatchEventKinds.ENTRY_MODIFY);
	}
	
	/************************non API*****************************/
	private String getLastLine() throws IOException {
		
		@SuppressWarnings("resource")
		BufferedReader input = new BufferedReader(new FileReader(syslog));
		String last = null, line;
		
		while ((line = input.readLine()) != null) { 
			last = line;
		}
	
		return last;
	}
	
	/************************ API*****************************/

	public void StopWatch() {
		isWorking = false;
	}
	
	public void startWatch() throws IOException, InterruptedException {
		isWorking = true;

		while (isWorking && (key = watchService.take()) != null) {
				List<WatchEvent<?>> pollEvents = key.pollEvents();
				for (int i = 0; i < pollEvents.size(); ++i) {
					setChanged();
					notifyObservers(getLastLine());
			
				//	print last line - syso
				//	System.out.println(getLastLine());
				}
				key.reset();
			}
	}
	
	public static void main (String[] args) throws IOException, InterruptedException {
		
		Monitor monFile = new Monitor (Paths.get("/var/log/"), new File("/var/log/syslog"));
		CrudFile obs = new CrudFile(new File ("/home/student/git/g.txt"), new File("/var/log/syslog"));
		CrudFile obs2 = new CrudFile(new File ("/home/student/git/bbb.txt"), new File("/var/log/syslog"));


		monFile.addObserver((obj, a)->{ obs.create(a.toString());});
		monFile.addObserver((obj2, a)->{ obs2.create(a.toString());});
		
		monFile.startWatch();
	}
}