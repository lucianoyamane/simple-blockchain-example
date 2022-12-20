package br.com.lucianoyamane.example;

public class Block {

    private String hash;
    private String previousHash;
    private String data;
    private Long timeStamp;

    private Block(String previousHash, String data) {
        this.setData(data);
        this.setPreviousHash(previousHash);
        this.setTimeStamp(System.currentTimeMillis());
        this.setHash(calculateHash());
    }

    public static Block genesis() {
        return new Block("Hi im the first block", "0");
    }

    public static Block block(String data, String previousHash) {
        return new Block(data, previousHash);
    }

    public String calculateHash() {
        return StringUtil.encode(this.getCompositionInformationToEncode());
    }

    private String getCompositionInformationToEncode() {
        StringBuilder input = new StringBuilder().append(this.getPreviousHash()).append(this.getTimeStamp()).append(this.getData());
        return input.toString();
    }

    public String getHash() {
        return this.hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    private String getData() {
        return this.data;
    }

    private String getTimeStamp() {
        return Long.toString(this.timeStamp);
    }

    private void setHash(String hash) {
        this.hash = hash;
    }

    private void setPreviousHash(String hash) {
        this.previousHash = hash;
    }

    private void setData(String data) {
        this.data = data;
    }

    private void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
}
