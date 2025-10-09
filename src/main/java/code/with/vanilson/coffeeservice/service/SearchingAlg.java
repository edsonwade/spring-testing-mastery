/**
 * Author: vanilson muhongo
 * Date:2025-10-07
 * Time:15:12
 * Version:1
 */

package code.with.vanilson.coffeeservice.service;

import org.springframework.stereotype.Service;

class SearchingAlg {

    public static int linearSearch(int[] array, int target) {
        validateArray(array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {return i;}
        }
        return -1;
    }

    public static int binarySearch(int[] array, int target) {
        validateArray(array);
        int left = 0;
        int rigth = array.length - 1;

        while (left <= rigth) {
            int middle = (left + rigth) / 2;

            if (array[middle] == target) {
                return middle;
            } else if (array[middle] < target) {
                left = middle + 1;

            } else if (array[middle] > target) {
                rigth = middle - 1;

            }

        }

        return -1;
    }

    private static void validateArray(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
    }

}
