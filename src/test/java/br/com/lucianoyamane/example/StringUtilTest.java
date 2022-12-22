package br.com.lucianoyamane.example;

import org.junit.jupiter.api.Test;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StringUtilTest {

    @Test
    void testGetMerkleRootEmptyTransaction() {

        List<String> values = new ArrayList<>();

        String result = StringUtil.getMerkleRoot(values);
        assertNull(result);
    }

    @Test
    void testGetMerkleRootOneTransaction() {

        List<String> values = new ArrayList<>();
        values.add("value_one");

        String result = StringUtil.getMerkleRoot(values);
        assertEquals("value_one", result);
    }

    @Test
    void testGetMerkleRootTwoTransactions() {
        List<String> values = new ArrayList<>();
        values.add("value_one");
        values.add("value_two");

        String result = StringUtil.getMerkleRoot(values);
        assertEquals("307ecde187d12787b3ad0a8380e90512bd439ba06acf75557df3939de172aa83", result);
    }

    @Test
    void testGetMerkleRootFourTransactions() {
        List<String> values = new ArrayList<>();
        values.add("value_one");
        values.add("value_two");
        values.add("value_three");
        values.add("value_four");

        String result = StringUtil.getMerkleRoot(values);
        assertEquals("a29e2c2a96813c3326e76af74e0efb37b2055c3716e4c2adcb6a648b82980288", result);
    }

    @Test
    void testGetDificultyString() {
        String result = StringUtil.getCharsZeroByDifficuty(1);
        assertEquals("0", result);
    }

    @Test
    void testGetDificultyZeroString() {
        String result = StringUtil.getCharsZeroByDifficuty(2);
        assertEquals("00", result);
    }

    @Test
    void testGetStringFromKey() {
        Key key = mock(Key.class);
        when(key.getEncoded()).thenReturn("teste".getBytes());
        String result = StringUtil.getStringFromKey(key);
        assertEquals("dGVzdGU=", result);
    }

}