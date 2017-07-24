package mycode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Sorter {

	private static int[] intArray;

	public Sorter() {
		Sorter.intArray = new int[] { 5, 2, 7, 8, 3, 1, 10, 52, 0, 6 };
	}

	public int[] descendingSorter(int[] unsorted) {
		int unsortedIndex = 0;
		int endIndex = unsorted.length;
		int minIndex = 0;

		int[] sorted = new int[endIndex];

		while (unsortedIndex < endIndex - 1) {
			// find the minimum on the unsorted array
			int minIndexValue = (int) 7.77e8;
			for (int i = unsortedIndex; i < endIndex - 1; i++) {

				if (unsorted[i] < unsorted[i + 1]
						&& unsorted[i] < minIndexValue) {
					minIndex = i;
					minIndexValue = unsorted[i];
				} else {
					if (unsorted[i] > unsorted[i + 1]
							&& unsorted[i + 1] < minIndexValue) {
						minIndex = i + 1;
						minIndexValue = unsorted[i + 1];
					}
				}
			}
			// At this point I have the index of the minimum value of the
			// unsorted array
			// in the variable minIndex

			unsorted[minIndex] = unsorted[unsortedIndex];
			unsorted[unsortedIndex] = minIndexValue;

			unsortedIndex++;
		}
		sorted = unsorted;
		return sorted;
	}

	public int[] mysterySorter(int[] unsorted) {

		int endIndex = unsorted.length;
		int pos = 1;

		for (int i = pos; i < endIndex; i++) {
			int currentIndex = i;
			while (currentIndex >= 1) {
				if (unsorted[currentIndex] > unsorted[currentIndex - 1]) {
					break;
				} else {
					// Swap the elements if its lower and go searching for lower
					// values until the start of the array

					// Swapping
					int temp = unsorted[currentIndex];
					unsorted[currentIndex] = unsorted[currentIndex - 1];
					unsorted[currentIndex - 1] = temp;
					// Decrease the current index for check again
					currentIndex--;
				}
			}
		}
		int[] sorted = new int[endIndex];
		sorted = unsorted;
		return sorted;
	}

	public static void main(String[] args) {
		Sorter sorter = new Sorter();
		// Measuring the execution time of the method descendingSorter
		long startTime = System.nanoTime();
		int[] sorted = sorter.descendingSorter(intArray);
		long endTime = System.nanoTime();
		System.out
				.println("The array was sorted by descendingSorter in a time of: "
						+ (endTime - startTime) + " nanoseconds");
		System.out.println(Arrays.toString(sorted));

		// Measuring the execution time of the method mysterySorter
		Sorter sorter2 = new Sorter();
		long startTime2 = System.nanoTime();
		int[] sorted2 = sorter2.mysterySorter(intArray);
		long endTime2 = System.nanoTime();
		System.out
				.println("The array was sorted by mysterySorter in a time of: "
						+ (endTime2 - startTime2) + " nanoseconds");
		System.out.println(Arrays.toString(sorted2));

		// Measuring the execution time of the built-in method sorter
		// Building of a list with the array values
		Sorter sorter3 = new Sorter();
		ArrayList<Integer> intList = new ArrayList<Integer>();
		System.out.println(intArray.length);
		for (int i = 0; i < getLength(); i++) {
			intList.add(intArray[i]);
		}
		long startTime3 = System.nanoTime();
		Collections.sort(intList);
		long endTime3 = System.nanoTime();
		System.out
				.println("The array was sorted by built-in Java sort in a time of: "
						+ (endTime3 - startTime3) + " nanoseconds");
		System.out.println(intList.toString());

	}

	public static int getLength() {
		return intArray.length;
	}
}
