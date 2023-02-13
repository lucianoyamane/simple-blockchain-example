package br.com.lucianoyamane.example.validate.block.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.block.BlockValidate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotMineHashConditionTest {

    @Test
    void testInitState() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn("current_hash");
        NotMineHashCondition testCondition = NotMineHashCondition.init(blockValidateMock);
        assertNotNull(testCondition);
        assertEquals("This block (current_hash) hasn't been mined", testCondition.getMessage());
    }
    @Test
    void testTrueCheck() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn("test_hash_invalid");
        NotMineHashCondition testCondition = NotMineHashCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getDifficulty()).thenReturn(3);

        Boolean result = testCondition.check(previousBlockDataMock);
        assertTrue(result);
    }

    @Test
    void testFalseCheck() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn("000test_hash_valid");
        NotMineHashCondition testCondition = NotMineHashCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getDifficulty()).thenReturn(3);

        Boolean result = testCondition.check(previousBlockDataMock);
        assertFalse(result);
    }

    @Test
    void testCurrentBlockHashNull() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn(null);
        NotMineHashCondition testCondition = NotMineHashCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getDifficulty()).thenReturn(3);

        Boolean result = testCondition.check(previousBlockDataMock);
        assertTrue(result);
    }

    @Test
    void testDifficultyNull() {
        BlockValidate blockValidateMock = mock(BlockValidate.class);
        when(blockValidateMock.getCurrentBlockHash()).thenReturn("test_hash_invalid");
        NotMineHashCondition testCondition = NotMineHashCondition.init(blockValidateMock);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getDifficulty()).thenReturn(null);

        Boolean result = testCondition.check(previousBlockDataMock);
        assertTrue(result);
    }




}