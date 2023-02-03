package br.com.lucianoyamane.example.valida.block;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.block.BlockBlockChain;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Valida;
import br.com.lucianoyamane.example.valida.block.condicao.HashAtualDiferenteDoAnteriorCondicao;
import br.com.lucianoyamane.example.valida.block.condicao.HashNaoFoiMineradoCondicao;
import br.com.lucianoyamane.example.valida.block.condicao.HashRegistradoDiferenteCalculadoBlockCondicao;
import br.com.lucianoyamane.example.valida.transaction.TransactionValida;

import java.util.Objects;

public class BlockValida extends Valida {

    private BlockBlockChain blockBlockChain;

    private BlockValida(BlockBlockChain blockBlockChain) {
        this.setBlockBlockChain(blockBlockChain);
    }

    @Override
    protected void defineCondicoes() {
        this.addCondicao(HashAtualDiferenteDoAnteriorCondicao.inicia(this));
        this.addCondicao(HashRegistradoDiferenteCalculadoBlockCondicao.inicia(this));
        this.addCondicao(HashNaoFoiMineradoCondicao.inicia(this));
    }

    public static BlockValida valida(BlockBlockChain blockBlockChain) {
        return new BlockValida(blockBlockChain);
    }

    private BlockBlockChain getBlockBlockChain() {
        return blockBlockChain;
    }

    private void setBlockBlockChain(BlockBlockChain blockBlockChain) {
        this.blockBlockChain = blockBlockChain;
    }

    public Boolean compareRegisteredAndCalculatedHash() {
        return this.getBlockBlockChain().getBlock().getHash().equals(this.getBlockBlockChain().calculateHash());
    }
    public Boolean compareWithPreviousHash(String previousHash) {
        return this.getBlockBlockChain().getBlock().getPreviousHash().equals(previousHash);
    }

    public Boolean hashIsSolved(int difficulty) {
        return this.getBlockBlockChain().getBlock().getHash().substring( 0, difficulty).equals(StringUtil.getCharsZeroByDifficuty(difficulty));
    }

    @Override
    public void processaDadosProximoBloco(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        TransactionBlockChain currentBlockTransaction = this.getBlockBlockChain().getTransactionBlockChain();
        if (Objects.nonNull(currentBlockTransaction)) {
            TransactionValida.valida(currentBlockTransaction).executa(previousBlockData);
        }
        previousBlockData.setPreviousHash(this.getBlockBlockChain().getHash());
    }

}