package br.com.lucianoyamane.example.validate.block.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.block.BlockValidate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisteredAndCalculatedHashNotEqualsConditionTest {

    @Test
    void testInitState() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn("valid_hash");
        when(blockValidateMock.getCurrentBlockCalculatedHash()).thenReturn("calculated_hash");
        RegisteredAndCalculatedHashNotEqualsCondition testCondition = RegisteredAndCalculatedHashNotEqualsCondition.init(blockValidateMock);
        assertNotNull(testCondition);
        assertEquals("Current(valid_hash) and Calculated(calculated_hash) are not equal.", testCondition.getMessage());
    }

    @Test
    void testTrueCheck() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn("valid_hash");
        when(blockValidateMock.getCurrentBlockCalculatedHash()).thenReturn("not_same_hash");
        RegisteredAndCalculatedHashNotEqualsCondition testCondition = RegisteredAndCalculatedHashNotEqualsCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);

        Boolean result = testCondition.check(previousBlockDataMock);
        assertTrue(result);
    }

    @Test
    void testFalseCheck() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn("same_hash");
        when(blockValidateMock.getCurrentBlockCalculatedHash()).thenReturn("same_hash");
        RegisteredAndCalculatedHashNotEqualsCondition testCondition = RegisteredAndCalculatedHashNotEqualsCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);

        Boolean result = testCondition.check(previousBlockDataMock);
        assertFalse(result);
    }

    @Test
    void testCurrentBlockHashNull() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn(null);
        RegisteredAndCalculatedHashNotEqualsCondition testCondition = RegisteredAndCalculatedHashNotEqualsCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);

        Boolean result = testCondition.check(previousBlockDataMock);
        assertTrue(result);
    }

    @Test
    void testCalculatedBlockHashNull() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn("valid hash");
        when(blockValidateMock.getCurrentBlockCalculatedHash()).thenReturn(null);
        RegisteredAndCalculatedHashNotEqualsCondition testCondition = RegisteredAndCalculatedHashNotEqualsCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);

        Boolean result = testCondition.check(previousBlockDataMock);
        assertTrue(result);
    }

}