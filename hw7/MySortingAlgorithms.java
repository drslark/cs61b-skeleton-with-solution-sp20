import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k; i += 1) {
                for (int j = 0; j < i; j += 1) {
                    if (array[j] > array[i]) {
                        int x = array[i];
                        int y = array[j];
                        for (int m = j; m < i; m += 1) {
                            int z = array[m + 1];
                            array[m + 1] = y;
                            y = z;
                        }
                        array[j] = x;
                        break;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k - 1; i += 1) {
                int min = i;
                for (int j = i + 1; j < k; j += 1 ) {
                    if (array[j] < array[min]) {
                        min = j;
                    }
                }
                swap(array, i, min);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            mergesort(array, 0, k-1);
        }

        private void mergesort(int[] array, int start, int end) {
            if (end - start == 0 || end - start == 1) {
                if (end - start == 1) {
                    if (array[start] > array[end]) {
                        swap(array, start, end);
                    }
                }
            } else {
                int len = (end - start) / 2;
                mergesort(array, start, start + len);
                mergesort(array, start + len + 1, end);
                merge(array, start, start + len, start + len + 1, end);
            }

        }

        private void merge(int[] array, int start1, int end1,
                            int start2, int end2) {

            int[] array1 = new int[end1 - start1 + 1];
            int[] array2 = new int[end2 - start2 + 1];
            System.arraycopy(array, start1, array1, 0, end1 - start1 + 1 );
            System.arraycopy(array, start2, array2, 0, end2 - start2 + 1 );
            int a1 = 0;
            int a2 = 0;

            for (int i = start1; i <= end2; i += 1) {
                if (a1 == array1.length) {
                    array[i] = array2[a2];
                    a2 += 1;
                } else if (a2 == array2.length) {
                    array[i] = array1[a1];
                    a1 += 1;
                } else {
                    if (array1[a1] <= array2[a2]) {
                        array[i] = array1[a1];
                        a1 += 1;
                    } else {
                        array[i] = array2[a2];
                        a2 += 1;
                    }
                }
            }


        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {

            int pow10 = 1;
            while(true) {
                int[] newA = new int[k];
                System.arraycopy(a, 0, newA, 0, k);
                int[] digits = new int[10];
                int[] sumDig = new int[10];
                for (int j = 0; j < k; j += 1) {
                    int curr = (newA[j] / pow10) % 10;
                    digits[curr] += 1;
                    for (int m = curr + 1; m < 10; m += 1) {
                        sumDig[m] += 1;
                    }

                }
                if (digits[0] == k) {
                    break;
                }
                for (int j = 0; j < k; j += 1) {
                    int curr = (newA[j] / pow10) % 10;
                    a[sumDig[curr]] = newA[j];
                    sumDig[curr] += 1;
                }
                pow10 *= 10;

            }

        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
