/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchain2;

/**
 *
 * @author hp
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


/**
 *
 * @author khaled
 */
public final class Block {

    static {
        File myFile = new File("BlockChaine.txt");
        try {

            if (myFile.createNewFile()) {
                System.out.println(" File created: " + myFile.getName());
            } else {
                System.out.println("already exists ...");

            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    String Prev_Hash;
    int Version;
    String MerkleRoot;
    String Hash;
    long Time_Stamp;
    int Nonce = 0;
    String data;
    static ArrayList<Block> blockChain = new ArrayList<Block>();
    int Difficulty = 3;

    public Block() {
    }

    public Block(String text, String previousHash) {
        this.data = text;
        this.Time_Stamp = new Date().getTime();
        this.Prev_Hash = previousHash;
        this.Hash = calculate();

    }

    public void SetBlock(Block block) {
        MineBlock();
        block.Hash = Hash;
        blockChain.add(block);
    }

    public static void StoringData(ArrayList<Block> blockChain) {
        try {
            try (FileWriter myWriter = new FileWriter("BlockChaine.txt")) {
                for (int i = 0; i < blockChain.size(); i++) {
                    Block block = blockChain.get(i);
                    myWriter.write("Hash:" + block.Hash + ",previous hash:" + block.Prev_Hash
                            + ",timestamp:" + block.Time_Stamp + "\n");
                    
                }
            }
            System.out.println("Successfully wrote...");
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }

    public static void readFromFile() {
        try {
            try (Scanner myReader = new Scanner(new File("BlockChaine.txt"))) {
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    SortingFile(data);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    private static void SortingFile(String line) {
        String[] x = line.split(",");
        String hash = x[0].substring(x[0].indexOf(":") + 1, x[0].length());
        String prevHash = x[1].substring(x[1].indexOf(":") + 1, x[1].length());
        long TimeStamp = Long.parseLong(x[2].substring(x[2].indexOf(":") + 1, x[2].length()));
        Block b = new Block();
        b.Hash = hash;
        b.Prev_Hash = prevHash;
        b.Time_Stamp = TimeStamp;
        blockChain.add(b);
    }

    public Block GetBlock(String HashOfBlock) {
        for (int i = 0; i < blockChain.size(); i++) {
            if (blockChain.get(i).Hash.equals(HashOfBlock)) {
                return blockChain.get(i);
            }
        }
        return null;
    }

    public void MineBlock() {
        String target = "";
        for (int i = 0; i < Difficulty; i++) {
            target += "0";
        }
        System.out.println("mining in process...");
        while (!Hash.substring(0, Difficulty).equals(target)) {
            Nonce++;
            Hash = calculate();
        }

    }

    public static void ExploreBlocks() {
        for (int i = 0; i < blockChain.size(); i++) {
            Block block = blockChain.get(i);
            System.out.println("block #" + (i + 1) + "\npreviousHash:" + block.Prev_Hash
                    + "\nHash:" + block.Hash + "\ntimeStamp:" + block.Time_Stamp
            );

        }

    }

    public String calculate() {
        String calculatedhash = Sha256(Prev_Hash
                + Long.toString(Time_Stamp)
                + data
                + Nonce
        );
        return calculatedhash;
    }

    public static String Sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
