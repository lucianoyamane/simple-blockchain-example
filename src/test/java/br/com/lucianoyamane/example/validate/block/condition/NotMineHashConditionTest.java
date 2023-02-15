package br.com.lucianoyamane.example.validate.block.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.block.BlockValidate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotMineHashConditionTest {


    @Test
    void testInitState() {
        BlockValidate validateMock = mock(BlockValidate.class);
        when(validateMock.getCurrentBlockHash()).thenReturn("current_block_hash");

        NotMineHashCondition notMineHashCondition = NotMineHashCondition.init(validateMock);
        assertNotNull(notMineHashCondition);

        String messageResult = notMineHashCondition.getMessage();
        assertEquals("This block (current_block_hash) hasn't been mined", messageResult);
    }

    @Test
    void testRuleTrue() {
        BlockValidate validateMock = mock(BlockValidate.class);
        when(validateMock.getCurrentBlockHash()).thenReturn("current_block_hash");

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getDifficulty()).thenReturn(5);

        NotMineHashCondition notMineHashCondition = NotMineHashCondition.init(validateMock);
        Boolean result = notMineHashCondition.rule(previousBlockDataMock);

        assertTrue(result);

    }

    @Test
    void testRuleFalse() {
        BlockValidate validateMock = mock(BlockValidate.class);
        when(validateMock.getCurrentBlockHash()).thenReturn("00000_valid_hash");

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getDifficulty()).thenReturn(5);

        NotMineHashCondition notMineHashCondition = NotMineHashCondition.init(validateMock);
        Boolean result = notMineHashCondition.rule(previousBlockDataMock);

        assertFalse(result);

    }


    @Test
    void testRuleCurrentBlockHashNull() {
        BlockValidate validateMock = mock(BlockValidate.class);
        when(validateMock.getCurrentBlockHash()).thenReturn(null);

        BlockChainValidateApp.PreviousBlockData previousBlockDataMock = mock(BlockChainValidateApp.PreviousBlockData.class);
        when(previousBlockDataMock.getDifficulty()).thenReturn(5);

        NotMineHashCondition notMineHashCondition = NotMineHashCondition.init(validateMock);
        Boolean result = notMineHashCondition.rule(previousBlockDataMock);

        assertTrue(result);
    }



}