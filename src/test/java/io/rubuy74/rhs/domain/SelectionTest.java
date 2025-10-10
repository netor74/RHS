package io.rubuy74.rhs.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SelectionTest {
    private static final String SELECTION_ID = "s1";
    private static final String SELECTION_NAME = "Home";
    private static final double SELECTION_ODD = 1.5;

    @Test
    void constructor_ShouldSetFields_WhenValidArgsProvided() {
        Selection selection = new Selection(SELECTION_ID, SELECTION_NAME, SELECTION_ODD);
        assertThat(selection).isNotNull();
        assertThat(selection.toString()).contains(SELECTION_NAME);
    }

    @Test
    void constructor_ShouldThrow_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Selection(null, SELECTION_NAME, SELECTION_ODD));
    }

    @Test
    void constructor_ShouldThrow_WhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Selection(SELECTION_ID, null, SELECTION_ODD));
    }

    @Test
    void constructor_ShouldThrow_WhenOddIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Selection(SELECTION_ID, SELECTION_NAME, null));
    }
}


