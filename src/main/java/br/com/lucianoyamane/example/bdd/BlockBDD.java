package br.com.lucianoyamane.example.bdd;

import br.com.lucianoyamane.example.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockBDD implements BDD {

    private BlockChainBDD mainBDD;

    private List<TransactionBDD> transactions;


    private BlockBDD(BlockChainBDD mainBDD) {
        this.mainBDD = mainBDD;
        this.transactions = new ArrayList<>();
    }

    public static BlockBDD init(BlockChainBDD mainBDD) {
        return new BlockBDD(mainBDD);
    }

    public TransactionBDD addTransaction() {
        TransactionBDD transactionBDD = TransactionBDD.init(this);
        this.transactions.add(transactionBDD);
        return transactionBDD;
    }

    public BlockChainBDD end() {
        return this.mainBDD;
    }

    public Block execute() {
        Block block = Block.init();
        for(TransactionBDD transactionBDD : this.transactions) {
            block.addTransaction(transactionBDD.execute());
        }
        return block;

    }
}
