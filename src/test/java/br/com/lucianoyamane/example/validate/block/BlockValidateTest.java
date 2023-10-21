package br.com.lucianoyamane.example.validate.block;

import br.com.lucianoyamane.example.blockchain.BlockExecutor;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BlockValidateTest {

    @Test
    void testInitState() {
        BlockExecutor blockExecutorMock = mock(BlockExecutor.class);
        BlockValidate validateTest = BlockValidate.validate(blockExecutorMock);
        assertNotNull(validateTest);
        assertEquals("BLOCK", validateTest.getLevel());
    }

    @Test
    void testGetCurrentBlockPreviousHash() {
        BlockExecutor blockExecutorMock = mock(BlockExecutor.class);
        when(blockExecutorMock.getPreviousHash()).thenReturn("previous_hash");

        BlockValidate validateTest = BlockValidate.validate(blockExecutorMock);
        String result = validateTest.getCurrentBlockPreviousHash();
        assertEquals("previous_hash", result);
    }

    @Test
    void testGetCurrentBlockHash() {
        BlockExecutor blockExecutorMock = mock(BlockExecutor.class);
        when(blockExecutorMock.getHash()).thenReturn("current_hash");

        BlockValidate validateTest = BlockValidate.validate(blockExecutorMock);
        String result = validateTest.getCurrentBlockHash();
        assertEquals("current_hash", result);
    }

    @Test
    void testGetCurrentBlockCalculatedHash() {
        BlockExecutor blockExecutorMock = mock(BlockExecutor.class);
        when(blockExecutorMock.calculateHash()).thenReturn("calculate_hash");

        BlockValidate validateTest = BlockValidate.validate(blockExecutorMock);
        String result = validateTest.getCurrentBlockCalculatedHash();
        assertEquals("calculate_hash", result);
    }

    @Test
    void testGetLevel() {
        BlockValidate validateTest = BlockValidate.validate(mock(BlockExecutor.class));
        String result = validateTest.getLevel();;
        assertEquals("BLOCK", result);
    }

    @Test
    void testProcessNextBlockData() {
        BlockExecutor blockExecutorMock = mock(BlockExecutor.class);
        when(blockExecutorMock.getHash()).thenReturn("block_hash");

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);

        BlockValidate validateTest = BlockValidate.validate(blockExecutorMock);
        validateTest.processNextBlockData(previousBlockDataMock);

        verify(previousBlockDataMock, times(1)).setPreviousHash(eq("block_hash"));

    }



}