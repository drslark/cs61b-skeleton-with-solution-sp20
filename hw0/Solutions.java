/** Solutions for HW 0.
 * @author Amit Bhat
 */

public class Solutions {

	/** Determine the maximum element of an integer array.
	 * @param a The inputted array.
	 * @return The maximum value of the array.
	 */ 
	public static int max(int[] a) {
		int max = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		return max;
	}

	/** Determine whether there exists a combination of three integers
		in an array such that they sum to 0. Elements can be repeated.
	 * @param a The inputted int array.
	 * @return A boolean stating whether any 3 elements sum to 0.
	 */ 
	public static boolean threeSum(int[] a){
		for (int i = 0; i < a.length; i++){
			for (int j = 0; j < a.length; j++){
				for (int k = 0; k < a.length; k++){
					if (a[i] + a[j] + a[k] == 0)
						return true;
				}
			}
		}
		return false;
	}

	/** Determine whether there exists a combination of three integers
		in an array such that they sum to 0. Elements can be repeated.
	 * @param a The inputted int array.
	 * @return A boolean stating whether any 3 elements sum to 0.
	 */  
	public static boolean threeSum_Distinct(int[] a){
		for (int i = 0; i < a.length; i++){
			for (int j = 0; j < a.length; j++){
				if (i == j)
					continue;
				for (int k = 0; k < a.length; k++){
					if (i == k || j == k) {
						continue;
					}
					else if (a[i] + a[j] + a[k] == 0)
						return true;
				}
			}
		}
		return false;
	}
	
}