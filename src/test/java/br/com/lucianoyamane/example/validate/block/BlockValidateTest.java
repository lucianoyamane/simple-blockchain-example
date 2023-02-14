package br.com.lucianoyamane.example.validate.block;

import br.com.lucianoyamane.example.blockchain.BlockBlockChain;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BlockValidateTest {

    @Test
    void testValidate() {
        BlockBlockChain blockBlockChainMock = mock(BlockBlockChain.class);
        BlockValidate validateTest = BlockValidate.validate(blockBlockChainMock);
        assertNotNull(validateTest);
    }

    @Test
    void testGetCurrentBlockPreviousHash() {
        BlockBlockChain blockBlockChainMock = mock(BlockBlockChain.class);
        when(blockBlockChainMock.getPreviousHash()).thenReturn("previous_hash");

        BlockValidate validateTest = BlockValidate.validate(blockBlockChainMock);
        String result = validateTest.getCurrentBlockPreviousHash();
        assertEquals("previous_hash", result);
    }

    @Test
    void testGetCurrentBlockHash() {
        BlockBlockChain blockBlockChainMock = mock(BlockBlockChain.class);
        when(blockBlockChainMock.getHash()).thenReturn("current_hash");

        BlockValidate validateTest = BlockValidate.validate(blockBlockChainMock);
        String result = validateTest.getCurrentBlockHash();
        assertEquals("current_hash", result);
    }

    @Test
    void testGetCurrentBlockCalculatedHash() {
        BlockBlockChain blockBlockChainMock = mock(BlockBlockChain.class);
        when(blockBlockChainMock.calculateHash()).thenReturn("calculate_hash");

        BlockValidate validateTest = BlockValidate.validate(blockBlockChainMock);
        String result = validateTest.getCurrentBlockCalculatedHash();
        assertEquals("calculate_hash", result);
    }

    @Test
    void testGetLevel() {
        BlockValidate validateTest = BlockValidate.validate(mock(BlockBlockChain.class));
        String result = validateTest.getLevel();;
        assertEquals("BLOCK", result);
    }

    //TODO: refactoring execute Validate
    @Test
    void testProcessNextBlockData() {
    }



}