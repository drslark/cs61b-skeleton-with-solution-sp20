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


	public static void main(String[] args) {
		
		int[] x = {1,2,3,5,4,2};

		System.out.println(max_for(x) + " " + max_while(x));
	}
}