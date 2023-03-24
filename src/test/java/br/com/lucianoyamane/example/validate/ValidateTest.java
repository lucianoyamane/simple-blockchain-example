package br.com.lucianoyamane.example.validate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
                previousBlockData.getPreviousHash();
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

    @Test
    void testExecuteValidate() {
        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(condition.check(eq(previousBlockDataMock))).thenReturn(Boolean.TRUE);
        when(condition.getMessage()).thenReturn("condition_message");

        validateAnonymous.execute(previousBlockDataMock);

        List<Map<String, String>> result = validateAnonymous.getConditionsErrors();
        assertTrue(result.size() == 1);

        Map<String, String> itemResult = result.get(0);
        assertEquals("test_level", itemResult.get("LEVEL"));
        assertEquals("condition_message", itemResult.get("MESSAGE"));
    }

    @Test
    void testExecuteSubValidates() {
        Validate subValidateMock = mock(Validate.class);
        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);

        validateAnonymous.addValidate(subValidateMock);
        validateAnonymous.execute(previousBlockDataMock);

        verify(subValidateMock, times(1)).execute(eq(previousBlockDataMock));
    }

    @Test
    void testExecuteProcessNextBlockData() {
        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        validateAnonymous.execute(previousBlockDataMock);

        verify(previousBlockDataMock, times(1)).getPreviousHash();
    }


}