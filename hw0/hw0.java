import java.io.*;
import java.lang.*;
import java.util.*;

public class hw0 {

	public static int max_for(int[] a) {
		int max = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		return max;
	}

	public static int max_while(int[] a) {
		int max = a[0];
		int i = 1;
		while (i < a.length){
			if (a[i] > max) {
				max = a[i];
			}
			i++;
		}
		return max;
	}

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



	public static void main(String[] args) {
		
		int[] x = {1,2,3,5,4,2};

		//System.out.println(max_for(x) + " " + max_while(x));

		System.out.println(threeSum(x) + " " + threeSum(new int[]{8, 2, -1, 15}));
		System.out.println(threeSum_Distinct(x) + " " + threeSum_Distinct(new int[]{8, 2, -1, 15}));


	}
}