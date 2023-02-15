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
                return "level_test";
            }
        } ;
    }

    @Test
    void testValidate() {
        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);

        when(condition.check(eq(previousBlockDataMock))).thenReturn(Boolean.TRUE);
        when(condition.getMessage()).thenReturn("condition_message");
        validateAnonymous.validate(previousBlockDataMock);

        List<Map<String, String>> result = validateAnonymous.getConditionsErrors();
        assertTrue(result.size() == 1);
        Map<String, String> item = result.get(0);
        assertEquals("level_test", item.get("LEVEL"));
        assertEquals("condition_message", item.get("MESSAGE"));

    }
}