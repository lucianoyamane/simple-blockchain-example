package br.com.lucianoyamane.example.validate.block.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.block.BlockValidate;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CurrentAndPreviousHashNotEqualsConditionTest {

    @Test
    void testInitState() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        CurrentAndPreviousHashNotEqualsCondition testCondition = CurrentAndPreviousHashNotEqualsCondition.init(blockValidateMock);
        assertNotNull(testCondition);
        assertEquals("Previous Hashes not equal", testCondition.getMessage());
    }

    @Test
    void testTrueCheck() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockPreviousHash()).thenReturn("hash_current_block");
        CurrentAndPreviousHashNotEqualsCondition testCondition = CurrentAndPreviousHashNotEqualsCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getPreviousHash()).thenReturn("hash_previous_block_data");

        Boolean result = testCondition.check(previousBlockDataMock);
        assertTrue(result);
    }

    @Test
    void testFalseCheck() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockPreviousHash()).thenReturn("hash_equals");
        CurrentAndPreviousHashNotEqualsCondition testCondition = CurrentAndPreviousHashNotEqualsCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getPreviousHash()).thenReturn("hash_equals");

        Boolean result = testCondition.check(previousBlockDataMock);
        assertFalse(result);
    }

    @Test
    void testCurrentBlockPreviousHashNull() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockPreviousHash()).thenReturn(null);
        CurrentAndPreviousHashNotEqualsCondition testCondition = CurrentAndPreviousHashNotEqualsCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getPreviousHash()).thenReturn("hash_previous_block_data");

        Boolean result = testCondition.check(previousBlockDataMock);
        assertTrue(result);
    }

    @Test
    void testPreviousHashNull() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockPreviousHash()).thenReturn(null);
        CurrentAndPreviousHashNotEqualsCondition testCondition = CurrentAndPreviousHashNotEqualsCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getPreviousHash()).thenReturn("hash_previous_block_data");

        Boolean result = testCondition.check(previousBlockDataMock);
        assertTrue(result);
    }

}