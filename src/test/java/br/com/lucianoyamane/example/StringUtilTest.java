package br.com.lucianoyamane.example;

import org.junit.jupiter.api.Test;


import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    public void testGetMerkleRootEmptyTransaction() {

        List<String> values = new ArrayList<>();

        String result = StringUtil.getMerkleRoot(values);
        assertNull(result);
    }

    @Test
    public void testGetMerkleRootOneTransaction() {

        List<String> values = new ArrayList<>();
        values.add("value_one");

        String result = StringUtil.getMerkleRoot(values);
        assertEquals("value_one", result);
    }

    @Test
    public void testGetMerkleRootTwoTransactions() {

        List<String> values = new ArrayList<>();
        values.add("value_one");
        values.add("value_two");

        String result = StringUtil.getMerkleRoot(values);
        assertEquals("307ecde187d12787b3ad0a8380e90512bd439ba06acf75557df3939de172aa83", result);
    }

    @Test
    public void testGetMerkleRootFourTransactions() {

        List<String> values = new ArrayList<>();
        values.add("value_one");
        values.add("value_two");
        values.add("value_three");
        values.add("value_four");

        String result = StringUtil.getMerkleRoot(values);
        assertEquals("a29e2c2a96813c3326e76af74e0efb37b2055c3716e4c2adcb6a648b82980288", result);
    }

}