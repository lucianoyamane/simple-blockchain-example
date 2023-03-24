package br.com.lucianoyamane.example.models;

public class Block {

    private String hash;
    private String previousHash;
    private String merkleRoot;
    private long timeStamp;
    private int nonce;

    private Block() {
        this.setTimeStamp(System.currentTimeMillis());
    }

    public static Block init() {
        return new Block();
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNonce() {
        return nonce;
    }

    public void increaseNonce() {
        this.nonce++;
    }
}
