package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.blockchain.BlockExecutor;
import br.com.lucianoyamane.example.blockchain.TransactionExecutor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BlockExecutorTest {


    @Test
    void testGenesis() {
        TransactionExecutor transactionExecutor = mock(TransactionExecutor.class);
        BlockExecutor blockExecutor = BlockExecutor.genesis(transactionExecutor);
        String result = blockExecutor.getPreviousHash();

        assertNotNull(blockExecutor);
        assertEquals("0", result);
    }

    @Test
    void testInit() {
        TransactionExecutor transactionExecutor = mock(TransactionExecutor.class);
        when(transactionExecutor.getHash()).thenReturn("hash_transaction_id");
        when(transactionExecutor.processTransaction()).thenReturn(Boolean.TRUE);

        BlockExecutor blockExecutor = BlockExecutor.init(transactionExecutor, "previous_hash");
        String resultHash = blockExecutor.getPreviousHash();
        String resultMerklet = blockExecutor.getMerklet();

        assertNotNull(blockExecutor);
        assertEquals("previous_hash", resultHash);
        assertNotNull(blockExecutor.getHash());
        assertEquals("hash_transaction_id", resultMerklet);
    }

    @Test
    void testCalculateHash() {
        TransactionExecutor transactionExecutor = mock(TransactionExecutor.class);
        when(transactionExecutor.getHash()).thenReturn("hash_transaction_id");

        BlockExecutor blockExecutor = BlockExecutor.init(transactionExecutor, "previous_hash");

        String firstResult = blockExecutor.calculateHash();
        String secondResult = blockExecutor.calculateHash();

        assertEquals(firstResult, secondResult);

    }

    @Test
    void testMine() {
        TransactionExecutor transactionExecutor = mock(TransactionExecutor.class);
        BlockExecutor blockExecutor = BlockExecutor.init(transactionExecutor, "previous_hash");
        blockExecutor.mine(3);
        String result = blockExecutor.getHash();
        assertEquals("000", result.substring( 0, 3));
    }


}
