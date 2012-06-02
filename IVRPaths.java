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
	//1,1,1
	//1,1,2
	//1,1,3
	//1,1,4
	//1,2
	//1,3
	//2,2
	//2,3
	//2,4
	//3,9
	//3,1
	//3,2
	//3,3
	//3,4
	
	
	
	private List<int[]> mIVRPaths = new ArrayList<int[]>();
	
 
	
	public void Init(){
		
		int[] path1 = {1,1,};
		//int[] path2 = {1,1,2};
		//int[] path3 = {1,1,3};
		//int[] path4 = {1,1,4};
		
		int[] path5 = {1,2};
		int[] path6 = {1,3};
		
		
		int[] path7 = {2,2};
		int[] path8 = {2,3};
		int[] path9 = {2,4};
		
		
		int[] path10 = {3,9};
		int[] path11 = {3,1};
		int[] path12 = {3,2};
		int[] path13 = {3,3};
		int[] path14 = {3,4};
		
		mIVRPaths.add(path1);
		//mIVRPaths.add(path2);
	//	mIVRPaths.add(path3);
	//	mIVRPaths.add(path4);
		mIVRPaths.add(path5);
		mIVRPaths.add(path6);
		mIVRPaths.add(path7);
		mIVRPaths.add(path8);
		mIVRPaths.add(path9);
		mIVRPaths.add(path10);
		mIVRPaths.add(path11);
		mIVRPaths.add(path12);
		mIVRPaths.add(path13);
		mIVRPaths.add(path14);

		
	}

	public int[] GetRandomPath()
	{
		
		Random rn = new Random();
		
		int randomNum =  rn.nextInt(mIVRPaths.size());
		
		return (int[])mIVRPaths.get(randomNum);
		
	}
	
	
}
