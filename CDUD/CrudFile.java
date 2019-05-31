
package il.co.ilrd.crud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.NoSuchFileException;
import java.util.Objects;

public class CrudFile implements CRUD<String, Integer>{
    private PrintWriter printWriter = null;
	private Integer lineNumber = 0;
	/************************non API*****************************/
	private void addToFile(File newFile, File syslogFile) throws IOException {
        Objects.requireNonNull(newFile);

        FileInputStream instream;
		FileOutputStream outstream;
		
		File infile = syslogFile;
		File outfile = newFile;
		
		instream = new FileInputStream(infile);
		outstream = new FileOutputStream(outfile);
		byte[] buffer = new byte[1024];
		int length;
		
		while ((length = instream.read(buffer)) > 0){
			outstream.write(buffer, 0, length);
			++lineNumber;
		}
		
		instream.close();
		outstream.close();
	}
	/************************Ctor*****************************/

	CrudFile (File newFile, File syslogFile) throws IOException{
		if (!newFile.exists()) { throw new NoSuchFileException(null); }
		
		printWriter = new PrintWriter(new FileOutputStream(newFile, true));
		addToFile(newFile, syslogFile);
	}
	/************************ API*****************************/

	@Override
    public Integer create(String entity) {
		printWriter.append(entity + "\n");

		++lineNumber;
        printWriter.flush();
    	printWriter.close();
		return lineNumber;
	}

	@Override
	public String read(Integer id) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void update(Integer id, String entity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void delete(Integer id) {
	  throw new UnsupportedOperationException();
	}

}
