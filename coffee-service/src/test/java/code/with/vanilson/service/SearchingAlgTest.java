package code.with.vanilson.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Linear and binary search tests")
class SearchingAlgTest {

    private final int[] arr = {12, 34, 1, 56, 7, 2, 90, 123, 356, 1_000_000_000, 234, 2563, 5, 9, 11, 99, 50};
    private final int[] ordered = Arrays.stream(arr).sorted().toArray();

    @Test
    @DisplayName("Should find target using binary search on sorted array")
    void binarySearch() {
        var result = SearchingAlg.binarySearch(ordered, 2563);
        assertThat(result).isEqualTo(15);
    }

    @Test
    @DisplayName("Should throw exception when binary search input is null")
    void shouldThrowExceptionWhenBinarySearchArrayIsNull() {
        assertThatThrownBy(() -> SearchingAlg.binarySearch(null, 2563))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Array cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception when linear search input is empty")
    void shouldThrowExceptionWhenLinearSearchArrayIsEmpty() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                SearchingAlg.linearSearch(new int[0], 2563)
        );
        assertThat(exception).hasMessage("Array cannot be null or empty");
    }

    @Test
    @DisplayName("Should return -1 when binary search target is not found")
    void shouldReturnMinusOneWhenBinarySearchTargetNotFound() {
        var result = SearchingAlg.binarySearch(ordered, 234_009_009);
        assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should return -1 when linear search target is not found")
    void shouldReturnMinusOneWhenLinearSearchTargetNotFound() {
        var result = SearchingAlg.linearSearch(arr, 234_009_009);
        assertThat(result).isEqualTo(-1);
    }

}