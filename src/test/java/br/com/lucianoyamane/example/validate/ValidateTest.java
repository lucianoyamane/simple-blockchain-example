package br.com.lucianoyamane.example.validate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidateTest {

    static Validate anonymousValidate;

    static Condition conditionMock;

    @BeforeEach
    public void createValidate() {
        anonymousValidate = new Validate() {
            @Override
            protected void configConditions() {
                conditionMock = mock(Condition.class);
                this.addCondition(conditionMock);
            }

            @Override
            protected void processNextBlockData(BlockChainValidateApp.PreviousBlockData previousBlockData) {
                previousBlockData.setPreviousHash("test_previous_hash");
            }

            @Override
            protected String getLevel() {
                return "test_anonymous_level";
            }
        };
    }

    @Test
    void testValidate() {
        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(conditionMock.getMessage()).thenReturn("condition_message");
        when(conditionMock.check(previousBlockDataMock)).thenReturn(Boolean.TRUE);

        anonymousValidate.validate(previousBlockDataMock);

        List<Map<String, String>> result = anonymousValidate.getConditionsErrors();
        assertTrue(result.size() == 1);
        Map<String, String> itemResult = result.get(0);
        assertEquals("test_anonymous_level", itemResult.get("LEVEL"));
        assertEquals("condition_message", itemResult.get("MESSAGE"));
    }

    @Test
    void testProcessNextBlockData() {
        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);

        anonymousValidate.processNextBlockData(previousBlockDataMock);
        verify(previousBlockDataMock, times(1)).setPreviousHash(eq("test_previous_hash"));
    }

}