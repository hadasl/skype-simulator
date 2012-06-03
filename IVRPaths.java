import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 */

/**
 * @author hadasbenami
 *
 */
public class IVRPaths {
	
	
	
	
	private List<int[]> mIVRPaths = new ArrayList<int[]>();
	
 
	
	public void Init(){
		
		int[] path0 = {1,1};
		
		
		int[] path1 = {1,2};
		int[] path2 = {1,3};
		
		
		int[] path3 = {2,2};
		int[] path4 = {2,3};
		int[] path5 = {2,4};
		
		
		int[] path6 = {3,9};
		int[] path7 = {3,1};
		int[] path8 = {3,2};
		int[] path9 = {3,3};
		
		
		int[] path10 = {4};
		
		mIVRPaths.add(path0);
		mIVRPaths.add(path1);
		mIVRPaths.add(path2);
		mIVRPaths.add(path3);
		mIVRPaths.add(path4);
		mIVRPaths.add(path5);
		mIVRPaths.add(path6);
		mIVRPaths.add(path7);
		mIVRPaths.add(path8);
		mIVRPaths.add(path9);
		mIVRPaths.add(path10);
		
		
	}

	public int[] GetRandomPath()
	{
		
		Random rn = new Random();
		
		int randomNum =  rn.nextInt(mIVRPaths.size());
		
		return (int[])mIVRPaths.get(randomNum);
		
	}
	
	
}
