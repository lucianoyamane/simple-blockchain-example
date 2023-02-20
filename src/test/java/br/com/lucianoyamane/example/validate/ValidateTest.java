package br.com.lucianoyamane.example.validate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidateTest {


    static Validate validateAnonymous;

    static Condition condition;

    @BeforeEach
    void setUp() {
        validateAnonymous = new Validate() {
            @Override
            protected void configConditions() {
                condition = mock(Condition.class);
                this.addCondition(condition);
            }

            @Override
            protected void processNextBlockData(BlockChainValidateApp.PreviousBlockData previousBlockData) {

            }

            @Override
            protected String getLevel() {
                return "test_level";
            }
        };
    }

    @Test
    void testConfigConditions() {
        List<Condition> conditions = validateAnonymous.getConditions();
        assertTrue(conditions.size() == 1);
    }

    @Test
    void testGetLevel() {
        String result = validateAnonymous.getLevel();
        assertEquals("test_level", result);
    }


}