package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.blockchain.BlockExecutor;
import br.com.lucianoyamane.example.blockchain.TransactionExecutor;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BlockChainApp {

	private List<BlockExecutor> blockchain;

	private BlockExecutor genesis;

	private BlockChainApp() {
		this.blockchain = new ArrayList<>();
	}

	public static BlockChainApp create() {
		return new BlockChainApp();
	}

	public String transactionGenesis(TransactionExecutor transaction) {
		BlockExecutor genesis = BlockExecutor.genesis(transaction);
		this.setGenesis(genesis);
		return this.proofOfWork(genesis);
	}

	public String transaction(TransactionExecutor transaction, String previousHash) {
		BlockExecutor blockExecutor = BlockExecutor.init(transaction, previousHash);
		this.addBlock(blockExecutor);
		return this.proofOfWork(blockExecutor);
	}

	private String proofOfWork(BlockExecutor blockExecutor) {
		return blockExecutor
				.mine(Difficulty.getInstance().getDifficulty())
				.getHash();
	}

	public void addBlock(BlockExecutor newBlockExecutor) {
		blockchain.add(newBlockExecutor);
	}

	private void setGenesis(BlockExecutor newBlockExecutor) {
		this.genesis = newBlockExecutor;
	}

	private BlockExecutor getGenesis() {
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
