import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.MySession;



import com.skype.api.Participant;

/**
 * 
 */

/**
 * @author hadasbenami
 *
 */
public class IVRPaths {
	
	/*
	 * 
[1, 1, 1]
[1, 1, 2]
[1, 1, 3]
[1, 1, 4]
[1, 2] 
[1, 3]


[2, [8], 8, 1, 1]
[2, [8], 8, 1, 2]
[2, [8], 8, 2]
[2, [8], 8, 3, 1]
[2, [8], 8, 3, 2]
[2, [8], 8, 3, 3]
[2, [8], 8, 3, 4]
[2, [8], 8, 3, 5]
[2, [8], 8, 3, 6]
[2, [8], 2]
[2, [8], 3]
[2, [8], 4]

[3, [8],  9]
[3, [8], 1, 8, 1, 1]
[3, [8], 1, 8, 1, 2]
[3, [8], 1, 8, 2]
[3, [8], 1, 8, 3, 1]
[3, [8], 1, 8, 3, 2]
[3, [8], 1, 8, 3, 3]
[3, [8], 1, 8, 3, 4]
[3, [8], 1, 8, 3, 5]
[3, [8], 1, 8, 3, 6]
[3, [8], 1, 1]
[3, [8], 1, 2]
[3, [8], 1, 3]
[3, [8], 2, 1]
[3, [8], 2, 2]

[3, [8], 3, 8, 1, 1]
[3, [8], 3, 8, 1, 2]
[3, [8], 3, 8, 2]
[3, [8], 3, 8, 3, 1]
[3, [8], 3, 8, 3, 2]
[3, [8], 3, 8, 3, 3]
[3, [8], 3, 8, 3, 4]
[3, [8], 3, 8, 3, 5]
[3, [8], 3, 8, 3, 6]
[3, [8], 3, 1]
[3, [8], 3, 2]
[3, [8], 3, 3]
[3, [8], 3, 4]
[3, [8], 3, 5]

[4,[8], 1]
[4,[8], 2]



	 */
	
	
	private List<Path> mIVRPaths = new ArrayList<Path>();
	private Logger mLog = null;
 
	
	public void Init(){
		
		int rand  = (int)(Math.random()*100);
		MySession.myConsole.printf("Randon number for customer ID %d%n", rand);
		
		
		int i = rand / 10;
		int j = rand % 10;
		
		int[] customerID = {2,2,2,2,i,j};
		int[] personID = {3,3,3,3,3,3,i,j};
		
		
		
		Path p1 = new Path();
		p1.Add(customerID, 0);
		int[] p11 = {1,1,1};
		
		p1.Add(p11, 8000);
		mIVRPaths.add(p1);
		
		Path p2 = new Path();
		p2.Add(customerID, 0);
		int[] p22 = {1,1,2};
		
		p2.Add(p22, 8000);
		mIVRPaths.add(p2);
		
		Path p3 = new Path();
		p3.Add(customerID, 0);
		int[] p33 = {2};
		p3.Add(p33,  8000);
		p3.Add(personID, 0);
		int[] p333 = {8,1,1};
		p3.Add(p333, 8000);
		mIVRPaths.add(p3);
		
		
		
	}

	public Path GetRandomPath()
	{
		
		Random rn = new Random();
		
		int randomNum =  rn.nextInt(mIVRPaths.size());
		MySession.myConsole.printf("Path number %d%n", randomNum);
		//return (Path)mIVRPaths.get(randomNum);
		mLog.Write(String.format("Path = %d",randomNum));
		
		String strPath = "Path: ";
		Path path = (Path)mIVRPaths.get(randomNum);
		PartialPath pp = null;
		int[] digits = null;
		for (int i =0; i < path.size(); i++)
		{
			strPath = strPath.concat(" [");
			pp = path.GetPart(i);
			digits = pp.getDigits();
			for (int j=0; j < digits.length; j++)
			{
				if (j == 0)
					strPath = strPath.concat(String.format("%d", digits[j]));
				else
					strPath = strPath.concat(String.format(",%d", digits[j]));
					
			}
			strPath = strPath.concat("]");
		}
		mLog.Write(strPath);
		return path;
		
	}
	public void InitFromFile(Logger log, String fileName)
	{
		mLog = log;
		int rand  = (int)(Math.random()*100);
		MySession.myConsole.printf("Randon number for customer ID %d%n", rand);
		
		int x = rand / 10;
		int y = rand % 10;
		
		mIVRPaths.clear();
		try
		{
			  
			  FileInputStream fstream = new FileInputStream(fileName);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  String[] parts; 
			  int index = 0;
			  String[] digits = null;
			  String temp = null;
			  Path path = null;
			  int[] numbers = null;
			  int delay = 0;
			  while ((strLine = br.readLine()) != null)   
			  {
				 
				  MySession.myConsole.printf("line %s%n", strLine);
				  if (strLine.isEmpty() == false)
				  {
					  
					  path = new Path();
					  parts = strLine.split("]");
					  for (int i=0; i<parts.length; i++)
					  {
						  if (parts[i].isEmpty() == false)
						  {
							  MySession.myConsole.printf("part %d %s%n", i, parts[i]);
							  index = parts[i].indexOf("[");
							  if (index > -1)
							  {
								  temp = parts[i].substring(index + 1);
								  digits = temp.split(",");
								  numbers = new int[digits.length];
								 
								  for (int j=0; j< digits.length; j++)
								  {
									  temp = digits[j].trim();
									  if (temp.compareTo("x") == 0)
										  numbers[j] = x;
									  else if (temp.compareTo("y")==0)
										  numbers[j] = y;
									  else  numbers[j] = Integer.parseInt(temp);
									  
								  }
								  if (numbers.length > 1)
									  delay = 0;
								  else delay = 3000;
								  path.Add(numbers, delay);
							  }
						  }
							  
					  }
					  mIVRPaths.add(path);
					
				  }
				  	  
			  }
			  //Close the input stream
			  in.close();
		 }
		 catch (Exception e)
		 {//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		 }
		
	}

		


}
