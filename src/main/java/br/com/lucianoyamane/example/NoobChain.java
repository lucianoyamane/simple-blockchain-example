package br.com.lucianoyamane.example;

import java.util.Date;

public class NoobChain {

    public static void main(String[] args) {

        Block genesisBlock = Block.genesis();
        System.out.println("Hash for block 1 : " + genesisBlock.getHash());

        Block secondBlock = Block.block(genesisBlock.getHash(), "Yo im the second block");
        System.out.println("Hash for block 2 : " + secondBlock.getHash());

        Block thirdBlock = Block.block(secondBlock.getHash(), "Hey im the third block");
        System.out.println("Hash for block 3 : " + thirdBlock.getHash());

    }
}
