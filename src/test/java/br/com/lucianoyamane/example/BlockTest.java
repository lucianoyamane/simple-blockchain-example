package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.transaction.Transaction;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BlockTest {


    @Test
    void testMineBlock() {
        Transaction transaction = mock(Transaction.class);
        when(transaction.processTransaction()).thenReturn(true);
        when(transaction.getTransactionId()).thenReturn("transaction_one");

        Block block = Block.init("teste");
        block.addTransaction(transaction);

        block.mineBlock(2);
        String resultHash = block.getHash();
        assertTrue(resultHash.startsWith("00"));

    }
}
