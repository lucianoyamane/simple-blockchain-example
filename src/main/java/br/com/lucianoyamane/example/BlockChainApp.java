package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.blockchain.BlockBlockChain;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BlockChainApp {

	private List<BlockBlockChain> blockchain;

	private BlockBlockChain genesis;

	private BlockChainApp() {
		this.blockchain = new ArrayList<>();
	}

	public static BlockChainApp create() {
		return new BlockChainApp();
	}

	public String transactionGenesis(TransactionBlockChain transaction) {
		BlockBlockChain genesis = BlockBlockChain.genesis(transaction);
		this.setGenesis(genesis);
		return this.proofOfWork(genesis);
	}

	public String transaction(TransactionBlockChain transaction, String previousHash) {
		BlockBlockChain blockBlockChain = BlockBlockChain.init(transaction, previousHash);
		this.addBlock(blockBlockChain);
		return this.proofOfWork(blockBlockChain);
	}

	private String proofOfWork(BlockBlockChain blockBlockChain) {
		return blockBlockChain
				.mine(Difficulty.getInstance().getDifficulty())
				.getHash();
	}

	public void addBlock(BlockBlockChain newBlockBlockChain) {
		blockchain.add(newBlockBlockChain);
	}

	private void setGenesis(BlockBlockChain newBlockBlockChain) {
		this.genesis = newBlockBlockChain;
	}

	private BlockBlockChain getGenesis() {
		return this.genesis;
	}

	public void validate() {
		BlockChainValidateApp blockChainValidateApp = BlockChainValidateApp.init();
		blockChainValidateApp.validate(this.getGenesis(), this.blockchain);
		List<Map<String, String>> errorMessage = blockChainValidateApp.getErrorsMessages();
		if (!errorMessage.isEmpty()) {
			SystemOutPrintlnDecorator.vermelho("INVALID!!");
			throw new BlockChainException(new Gson().toJson(errorMessage));

		}
		SystemOutPrintlnDecorator.roxo("Blockchain is valid");
	}

}
