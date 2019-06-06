package com.mail.se;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class newtech {

    public newtech() {
        //createAndSaveRnd();
    }

    public void createAndSaveRnd() {

        try {
            Random r = new Random();
            //System.out.println("Numbers are");
            File file = new File("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config" + "/mail/p.csv");

            FileWriter writer = new FileWriter(file);

            System.out.println("start");
            for (int i = 1; i <= 100; i++) {
                for (int j = 1; j <= 100; j++) {
                    int value;
                    value = r.nextInt(100);
                    writer.append(Integer.toString(value));
                    writer.append('\n');
                }

            }
            writer.flush();
            writer.close();
            System.out.println("end");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void createAndSaveRndKeys(double[][] array, ArrayList<String> info) {

        try {
            Random r = new Random();
            //System.out.println("Numbers are");
            File file = new File("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config" + "/mail/keys/" + info.get(0) + "/p.csv");

            FileWriter writer = new FileWriter(file);

            System.out.println("start");
            for (int i = 0; array.length > i; i++) {
                for (int j = 0; array[i].length > j; j++) {
                    int value;
                    value = (int) array[i][j];
                    writer.append(Integer.toString(value));
                    writer.append('\n');
                }

            }

            writer.flush();
            writer.close();
            System.out.println("end");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public ArrayList<String> generatekey() {
        ArrayList<String> key = new ArrayList<String>();
        System.out.println("generatekey();");
        try {

            Scanner scanner = new Scanner(new File("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config\\mail\\keys\\p.csv"));

            while (scanner.hasNextLine()) {
                key.add(scanner.nextLine());

            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return key;
    }

    public ArrayList<String> generateNewkey(ArrayList<String> info) {
        System.out.println("generatenewkey()" + info.toString());
        ArrayList<String> key = new ArrayList<String>();
        try {

            Scanner scanner = new Scanner(new File("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config\\mail\\keys\\" + info.get(0) + "\\"+info.get(4)+".csv"));

            while (scanner.hasNextLine()) {
                key.add(scanner.nextLine());

            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return key;
    }

    public ArrayList<String> encrypt(String word, ArrayList<String> key) {
        String chr[] = word.split("(?!^)");
        ArrayList<String> encrypted = new ArrayList<String>();

        for (int i = 0; i < chr.length; i++) {

            int asci = chr[i].charAt(0);

            //  System.out.println(key);
            int result = asci + Integer.parseInt(key.get(i));
            encrypted.add(new Character((char) result).toString());

        }
        //  Diffie_Hellman d = new Diffie_Hellman();

         System.out.println("newTech:  Encrypted : " + encrypted);
        return encrypted;
    }

    public ArrayList<String> encryptForSearching(String word, ArrayList<String> key) {
        String chr[] = word.split("(?!^)");
        ArrayList<String> encrypted = new ArrayList<String>();
//System.out.println(key);
        for (int i = 0; i < chr.length; i++) {

            int asci = chr[i].charAt(0);

            //  System.out.println(key);
            int result = asci + 1;
            //a  System.out.println(new Character((char) result).toString());
            encrypted.add(new Character((char) result).toString());

        }

        // System.out.println("newTech:  Encrypted : " + encrypted);
        return encrypted;
    }


    public static void main(String args[]) {
        newtech n = new newtech();

    }

    public String decrypt(List<String> al, ArrayList<String> key) {
        ArrayList<String> decrypted = new ArrayList<String>();
//System.out.println(al);
        for (int i = 0; i < al.size(); i++) {
//System.out.println(al.get(i));
//System.out.println(al.size());

            int result = al.get(i).charAt(0);

            int asci = result - Integer.parseInt(key.get(i));

            char chracter = (char) asci;

            decrypted.add(chracter + "");

        }

        //   System.out.println("Decrypted : " + decrypted);
        StringBuilder listString = new StringBuilder();

        for (String s : decrypted) {
            listString.append(s);
        }
        return listString.toString();
    }

    public String decryptFromApp(List<String> al, ArrayList<String> key, int id) {

        ArrayList<String> decrypted = new ArrayList<String>();
//System.out.println(al);
        for (int i = 0; i < al.size(); i++) {
//System.out.println(al.get(i).charAt(0)+ " i =  " + i);
            int result = al.get(i).charAt(0);

            int asci = result - Integer.parseInt(key.get(i));

            char chracter = (char) asci;

            decrypted.add(chracter + "");

        }

        //   System.out.println("Decrypted : " + decrypted);
        StringBuilder listString = new StringBuilder();

        for (String s : decrypted) {
            listString.append(s);
        }
        return listString.toString();
    }

    public void createAndSaveRndSynKeys(double[][] array, ArrayList<String> info) {
        
       // System.out.println(info.toString() + " from last method");
        try {

            File file = new File("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config\\mail\\keys\\" + info.get(0) + "\\"+info.get(4)+".csv");
            FileWriter writer = new FileWriter(file);

            System.out.println("start");
            for (int i = 0; array.length > i; i++) {
                for (int j = 0; array[i].length > j; j++) {
                    int value;
                    value = (int) array[i][j];
                    writer.append(Integer.toString(value));
                    writer.append('\n');
                }

            }

            writer.flush();
            writer.close();
            System.out.println("end");
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        try {

            File file = new File("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config\\mail\\keys\\" + info.get(3) + "\\"+info.get(4)+".csv");
            FileWriter writer = new FileWriter(file);

            System.out.println("start");
            for (int i = 0; array.length > i; i++) {
                for (int j = 0; array[i].length > j; j++) {
                    int value;
                    value = (int) array[i][j];
                    writer.append(Integer.toString(value));
                    writer.append('\n');
                }

            }

            writer.flush();
            writer.close();
            System.out.println("end");
        } catch (Exception e) {
            System.out.println(e);
        }
    
    }

}
