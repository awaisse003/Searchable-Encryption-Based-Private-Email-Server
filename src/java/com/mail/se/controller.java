package com.mail.se;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class controller {

    private Connection connection;
    LinkedList<String> tokens = null;
 public   HashMap<Integer, String> idfilemap;
    String st = null;

    public controller() throws FileNotFoundException, IOException, ClassNotFoundException {
         System.out.println(System.getProperty("user.dir"));
        this.idfilemap = new HashMap<Integer, String>();
        this.fileidmap = new HashMap<>();
        this.emap = ArrayListMultimap.create();

        FileInputStream fis;
        ObjectInputStream ois;
        fis = new FileInputStream("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config\\mail\\index.txt");
        ois = new ObjectInputStream(fis);
        this.emap = (Multimap<String, Integer>) ois.readObject();

        fis = new FileInputStream("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config\\mail\\idfilemap.txt");
        ois = new ObjectInputStream(fis);
        idfilemap = (HashMap<Integer, String>) ois.readObject();

       

    }
    HashMap<String, Integer> fileidmap;
    public Multimap<String, Integer> emap;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mailserver", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);

        }
        return connection;
    }

    public void encryopRecord(String content, int id) throws SQLException, Exception {

        init(content, id);
    }

    public void init(String content, int id) throws Exception {

        int i = id;
        String o = content;
        String o1 = content;

        idfilemap.put(i, o);
        fileidmap.put(o1, i);

        i++;
        // System.out.println(idfilemap);
        encrypt(content);
    }

    public void encrypt(String content) throws Exception {
//        kg1.generate();
//        kg1.store("mail/key.txt");
//        
System.out.println(content + " from controller ");
        HashMap<String, LinkedList<String>> map = new HashMap<String, LinkedList<String>>();

        Scanner scn = new Scanner(content);
        for (int i = 0; i < 1000 && scn.hasNext(); i++) {
            String s = scn.next();
             System.out.println(s);
            if (map.containsKey(s)) {
                map.get(s).add(content);

                map.get(s).add(content);
            } else {

                LinkedList<String> l = new LinkedList<String>();
                l.add(content);
                map.put(s, l);
            }
        }
        //  System.out.println(map);

        String buff = null;
        for (HashMap.Entry m : map.entrySet()) {
            int j = 1;
            LinkedList<String> ls = (LinkedList<String>) m.getValue();
            for (String s : ls) {

                String w = (String) m.getKey() + j;

                //  System.out.println(w);
                j++;

                
                newtech n = new newtech();
                ArrayList<String> encrypted;
                encrypted = n.encryptForSearching(w, n.generatekey());
                //System.out.println(w);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(baos);
                for (String element : encrypted) {
                    out.writeUTF(element);
                }

                byte[] byteCipherText = baos.toByteArray();
                emap.put(new String(byteCipherText), fileidmap.get(s));

            }
        }
        //   System.out.println(emap);
        FileOutputStream f = new FileOutputStream("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config\\mail\\index.txt");
        ObjectOutputStream o = new ObjectOutputStream(f);
       
        o.writeObject(emap);
        o.close();

    }

    public void searchToken(String keyword) throws Exception {
        FileInputStream fis = new FileInputStream("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config/mail/idfilemap.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        HashMap<Integer, String> m = (HashMap<Integer, String>) ois.readObject();
        LinkedList<String> l = new LinkedList<String>();
        ois.close();
        Multimap<String, Integer> emap = this.emap;
            newtech n = new newtech();

        for (int i = 1; i <= m.size(); i++) {
            String s = keyword + i;
            
           // n.createAndSaveRnd();
            ArrayList<String> encrypted;
            ArrayList<String> k = n.generatekey();
            
         //   System.out.println(k);
            encrypted = n.encryptForSearching(s, k);
            System.out.println(encrypted);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            for (String element : encrypted) {
                out.writeUTF(element);
            }

            byte[] byteCipherText = baos.toByteArray();
            l.add(new String(byteCipherText));
        }
        tokens = l;

    }

    public Collection<Integer> search() throws Exception {

        FileInputStream fis;
        ObjectInputStream ois;
        List<Integer> list = new ArrayList<Integer>();
        fis = new FileInputStream("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config/mail/index.txt");
        ois = new ObjectInputStream(fis);
        Multimap<String, Integer> emap = this.emap;
        ois.close();
        // System.out.println(emap);
        for (String b : tokens) {
            System.out.println(b);
            if (!emap.containsKey(b)) {
               System.out.println("not found :"+ b);
                break;
            }
           
            return emap.get(b) ;

        }
        return null;
    }

    public void writeDocIDFileNameMapping() throws Exception {

        FileOutputStream fos = new FileOutputStream("C:\\Users\\hp\\AppData\\Roaming\\NetBeans\\8.1\\config\\GF_4.1.1\\domain1\\config/mail/idfilemap.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(idfilemap);
        oos.close();
    }

    public static void main(String args[]) throws SQLException, Exception {

        controller c = new controller();
  //    c.encryopRecord("Hello This is my secret message  ", 1);
    //    c.encryopRecord("this email is for abdullah arshad \n hello ", 2);
  //      c.encryopRecord("talha  ", 3);
//        c.encryopRecord("hamza ", 4);
//        c.encryopRecord("between hamza ", 5);
//        c.encryopRecord("arshad ", 6);
//        c.encryopRecord("Thank you for your fast reply. I finally see a clean code on my page without any red lines. I'm assuming with your code, you call it by <h:outputText value=\"#{textFileBean.readData} /> While it launches with no problem. ", 7);
 //    c.encryopRecord("abc def ghi jkl mno pqr stu vwx z ", 8);
//        c.encryopRecord("", 9);
//        c.encryopRecord("hamza ", 10);
//        c.encryopRecord("between hamza ", 11);
//        c.encryopRecord("arshad ", 12);

//        c.encryopRecord("awais is good boy , how about you bro ?  ", 3);
        // System.out.println(c.emap);
    //   c.writeDocIDFileNameMapping();

        c.searchToken("hamza");
     //   System.out.println(c.emap);
      System.out.println(  c.search());

    }
}
