package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.blockchain.BlockBlockChain;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BlockBlockChainTest {


    @Test
    void testGenesis() {
        TransactionBlockChain transactionBlockChain = mock(TransactionBlockChain.class);
        BlockBlockChain blockBlockChain = BlockBlockChain.genesis(transactionBlockChain);
        String result = blockBlockChain.getPreviousHash();

        assertNotNull(blockBlockChain);
        assertEquals("0", result);
    }

    @Test
    void testInit() {
        TransactionBlockChain transactionBlockChain = mock(TransactionBlockChain.class);
        when(transactionBlockChain.getHash()).thenReturn("hash_transaction_id");
        when(transactionBlockChain.processTransaction()).thenReturn(Boolean.TRUE);

        BlockBlockChain blockBlockChain = BlockBlockChain.init(transactionBlockChain, "previous_hash");
        String resultHash = blockBlockChain.getPreviousHash();
        String resultMerklet = blockBlockChain.getMerklet();

        assertNotNull(blockBlockChain);
        assertEquals("previous_hash", resultHash);
        assertNotNull(blockBlockChain.getHash());
        assertEquals("hash_transaction_id", resultMerklet);


    }
}
