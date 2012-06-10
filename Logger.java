import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {
	  
	private  BufferedWriter bufferedWriter = null;
	
	public void Init()
	{
		try {
	            
	            //Construct the BufferedWriter object
	            bufferedWriter = new BufferedWriter(new FileWriter("Logger.txt"));
	            
	             
	       
	        } 
	        catch (Exception ex) 
	        {
	            ex.printStackTrace();
	        }
	
	} 
	private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
	public void Write(String line)
	{
		try {
			bufferedWriter.write(String.format("%s, %s",getDateTime(), line));
			bufferedWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Close() 
	{
		if (bufferedWriter != null) {
			try {
				bufferedWriter.flush();
				 bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
		}
           
	}
	  
	  
}
