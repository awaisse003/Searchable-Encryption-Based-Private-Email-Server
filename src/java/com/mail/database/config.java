package com.mail.database;

import com.mail.key.Diffie_Hellman;
import com.mail.key.keyMain;
import com.mail.login.mail;
import com.mail.se.controller;
import com.mail.se.newtech;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

public class config {

    private Connection connection;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mailserver?autoReconnect=true&useSSL=false", "root", "root");
        } catch (SQLException e) {
            System.out.println(e);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(config.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public boolean login(String username, String password) throws Exception {
        Statement stmt = null;
        try {
            connection = getConnection();
            System.out.println(connection + "here you go");
            stmt = connection.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
        String q = "Select * from login_info where username = '" + username + "' AND password = '" + password + "' limit 1";
        System.out.println(q);
        int id = 0;
        ResultSet rst = stmt.executeQuery(q);
        while (rst.next()) {
            username = rst.getString("username");
            id = rst.getInt("userId");
            System.out.println(id);

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();

            sessionMap.put("username", username);
            sessionMap.put("id", id);
            connection.close();
            return true;
        }
        return false;

    }

    public ArrayList<String> loginFromApplication(String username, String password) throws Exception {
        Statement stmt = null;
        try {
            connection = getConnection();
            System.out.println(connection + "here you go");
            stmt = connection.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
        String q = "Select * from login_info where username = '" + username + "' AND password = '" + password + "' limit 1";
        System.out.println(q);
        int id = 0;
        String name;
        ArrayList<String> info = new ArrayList<String>();

        ResultSet rst = stmt.executeQuery(q);
        while (rst.next()) {
            username = rst.getString("username");
            id = rst.getInt("userId");
            name = rst.getString("Name");
//   System.out.println(id);

            connection.close();

            info.add(username);
            info.add(Integer.toString(id));
            info.add(name);

            return info;
        }
        return info;

    }

        public List<mail> mails() throws SQLException {

        List<mail> mailList = new ArrayList<mail>();

        Connection conection = getConnection();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        if (!sessionMap.containsKey("id")) {
            return mailList;
        }
        int id = (int) sessionMap.get("id"); 
        String query = "SELECT * FROM mail_content join login_info On (mail_content.sender_id = login_info.userId AND mail_content.reciver_id = " + id + " ) order by mail_id desc ";
        PreparedStatement pstmt = conection
                .prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            mail m = new mail();

            m.setId(rs.getInt("mail_id"));
            m.setTitle(rs.getString("title"));
            m.setAttachment(rs.getString("mail_attachment"));
            m.setSender(rs.getString("sender_id"));
            // m.setReceiver_name(rs.getString("Name"));
            m.setContent(rs.getString("mail_content"));
            m.setSender_name(rs.getString("Name"));
            m.setDate(rs.getString("date"));
            // System.out.println(rs.getString("Name"));

            mailList.add(m);

        }
        rs.close();
        pstmt.close();
        connection.close();

        return mailList;

    }

    public boolean checkEligibility(int id, String cipher) throws SQLException {

        // List<mail> mailList = new ArrayList<mail>();
        // System.out.println("inside method" );
        Connection conection = getConnection();

        String query = "SELECT * FROM mail_content where  reciver_id = " + id + " and mail_content = '" + cipher + "'";

        PreparedStatement pstmt = conection
                .prepareStatement(query);

        ResultSet rs = pstmt.executeQuery();
        System.out.println(pstmt.toString());
        while (rs.next()) {

            return true;

        }
        return false;

    }

    public boolean checkContent(int id, String cipher) throws SQLException {

        // List<mail> mailList = new ArrayList<mail>();
        // System.out.println("inside method" );
        Connection conection = getConnection();

        String query = "SELECT * FROM mail_content where  sender_id = " + id + " and mail_content = '" + cipher + "'";

        PreparedStatement pstmt = conection
                .prepareStatement(query);

        ResultSet rs = pstmt.executeQuery();
        System.out.println(pstmt.toString());
        while (rs.next()) {

            return true;

        }
        return false;

    }

    //sent mail 
    public int sent(String title, String mail_content, String receiver_email, String content, Part file) throws SQLException, IOException, FileNotFoundException, ClassNotFoundException, Exception {

        Connection conection = getConnection();
        String query = "select * from login_info where email = ?";
        PreparedStatement pstmt = conection
                .prepareStatement(query);
        pstmt.setString(1, receiver_email);
        System.out.println(pstmt);
        ResultSet rs = pstmt.executeQuery();
        int r_id = 0;

        if (rs.next()) {
            r_id = rs.getInt("userId");

            // mail_content = 
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            int id = (int) sessionMap.get("id");
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            Date date = new Date();
            String d = formatter.format(date);

            System.out.println(title + r_id + d + mail_content);
            if (file != null) {
                query = "update mail_content set title=? , reciver_id = ? , date = ? , flag_enc = 0 where mail_content=?";
                pstmt = conection
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, title);

                pstmt.setInt(2, r_id);
                pstmt.setString(3, d);
                pstmt.setString(4, mail_content);

            } else {
                query = "update mail_content set title=? , reciver_id = ? , date = ? , flag_enc = 0 where mail_content = ?";
                pstmt = conection
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, title);

                pstmt.setInt(2, r_id);
                pstmt.setString(3, d);
                pstmt.setString(4, mail_content);

            }
//System.out.println(pstmt.toString());
            int r = pstmt.executeUpdate();
            //  System.out.println(query);
            ResultSet getlast = pstmt.getGeneratedKeys();
            if (getlast.next()) {

            }

            return r;
        } else {
            return 0;
        }
    }

    public int sentFromClientApplication(int id, String mail_content, String content, int rA) throws SQLException, IOException, FileNotFoundException, ClassNotFoundException, Exception {

        Connection conection = getConnection();
        PreparedStatement pstmt;

        String query = "insert into mail_content(mail_content,sender_id,r_A , flag_enc) values(? , ? , ? , 0)";
        pstmt = conection
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, mail_content);
        pstmt.setInt(2, id);
        pstmt.setInt(3, rA);

        int r = pstmt.executeUpdate();
        ResultSet getlast = pstmt.getGeneratedKeys();
        if (getlast.next()) {
            int last_inserted_id = getlast.getInt(1);

            controller c = new controller();
            System.out.println(content + " mail content " + last_inserted_id);
            c.encryopRecord(content, last_inserted_id);
            c.writeDocIDFileNameMapping();
 
            return r;

        } else {
            return 0;
        }
    }

    public List<mail> sentMails() throws SQLException {

        List<mail> mailList = new ArrayList<mail>();

        Connection conection = getConnection();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        int id = (int) sessionMap.get("id");
        String query = "SELECT * FROM mail_content join login_info On (mail_content.reciver_id = login_info.userId AND mail_content.sender_id = " + id + ")  order by mail_id desc ";
        PreparedStatement pstmt = conection
                .prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            mail m = new mail();

            m.setId(rs.getInt("mail_id"));
            m.setTitle(rs.getString("title"));
            m.setAttachment(rs.getString("mail_attachment"));
            m.setSender(rs.getString("sender_id"));
            // m.setReceiver_name(rs.getString("Name"));
            m.setContent(rs.getString("mail_content"));
            m.setSender_name(rs.getString("Name"));
            m.setDate(rs.getString("date"));
// System.out.println(rs.getString("Name"));

            mailList.add(m);

        }
        rs.close();
        pstmt.close();
        connection.close();

        return mailList;

    }

    public static void main(String args[]) throws SQLException, Exception {
        config c = new config();
        c.login("Awais003", "Awais12345");

    }

    public List<mail> getSpecificmails(int i) throws SQLException {
        List<mail> mailList = new ArrayList<mail>();
        newtech n = new newtech();

        Connection conection = getConnection();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        int id = (int) sessionMap.get("id");
        String query = "SELECT * FROM mail_content join login_info On (mail_content.sender_id = login_info.userId AND( mail_content.sender_id = " + id + " OR mail_content.reciver_id = " + id + "   )AND mail_id=" + i + " ) order by mail_id desc ";
        System.out.println(query);
        PreparedStatement pstmt = conection
                .prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            mail m = new mail();
            ArrayList<String> key = n.generatekey();
            String en = rs.getString("mail_content");
            // System.out.println(en);
            String str[] = en.split(" ");
            List<String> al = new ArrayList<String>();
            al = Arrays.asList(str);
            System.out.println(al);
            // System.out.println(n.decrypt(al, key));
            m.setId(rs.getInt("mail_id"));
            m.setTitle(rs.getString("title"));
            m.setAttachment(rs.getString("mail_attachment"));
            m.setSender(rs.getString("sender_id"));
            ArrayList<String> encrypted = null;
            // m.setReceiver_name(rs.getString("Name"));
            m.setContent(n.decrypt(al, key));
            m.setSender_name(rs.getString("Name"));
            m.setDate(rs.getString("date"));
            // System.out.println(rs.getString("Name"));

            mailList.add(m);

        }
        rs.close();
        pstmt.close();
        connection.close();

        // return mailList;
        return mailList;
    }

    
    public List<mail> deletemails(Collection<Integer> i) throws SQLException {
        List<mail> mailList = new ArrayList<mail>();
        System.out.println(i);
        for (int elem : i) {

            System.out.println(elem);

            Connection conection = getConnection();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            int id = (int) sessionMap.get("id");
            String query = "Delete From mail_content where mail_content.mail_id = " + elem ;
            System.out.println(query);
            PreparedStatement pstmt = conection
                    .prepareStatement(query);
            pstmt.executeUpdate();
//
//            while (rs.next()) {
//                mail m = new mail();
//
//                m.setId(rs.getInt("mail_id"));
//                m.setTitle(rs.getString("title"));
//                m.setAttachment(rs.getString("mail_attachment"));
//                m.setSender(rs.getString("sender_id"));
//                // m.setReceiver_name(rs.getString("Name"));
//                m.setContent(rs.getString("mail_content"));
//                m.setSender_name(rs.getString("Name"));
//                m.setDate(rs.getString("date"));
//                // System.out.println(rs.getString("Name"));
//
//                mailList.add(m);
//
//            }
           // rs.close();
            pstmt.close();
            connection.close();

            // return mailList;
        }
        return mailList;
    }

    
    public List<mail> getSpecificmails(Collection<Integer> i) throws SQLException {
        List<mail> mailList = new ArrayList<mail>();
        System.out.println(i);
        for (int elem : i) {

            System.out.println(elem);

            Connection conection = getConnection();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            int id = (int) sessionMap.get("id");
            String query = "SELECT * FROM mail_content join login_info On (mail_content.sender_id = login_info.userId AND( mail_content.sender_id = " + id + " OR mail_content.reciver_id = " + id + "   )AND mail_id=" + elem + " AND date is not null) order by mail_id desc ";
            System.out.println(query);
            PreparedStatement pstmt = conection
                    .prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                mail m = new mail();

                m.setId(rs.getInt("mail_id"));
                m.setTitle(rs.getString("title"));
                m.setAttachment(rs.getString("mail_attachment"));
                m.setSender(rs.getString("sender_id"));
                // m.setReceiver_name(rs.getString("Name"));
                m.setContent(rs.getString("mail_content"));
                m.setSender_name(rs.getString("Name"));
                m.setDate(rs.getString("date"));
                // System.out.println(rs.getString("Name"));

                mailList.add(m);

            }
            rs.close();
            pstmt.close();
            connection.close();

            // return mailList;
        }
        return mailList;
    }

    public String getPass(int id) throws SQLException {
        Connection conection = getConnection();
        String query = "SELECT * FROM login_info where userId =  " + id + " limit 1  ";
        System.out.println(query);
        PreparedStatement pstmt = conection
                .prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        String pas = "";
        while (rs.next()) {
            pas = rs.getString("password");
        }

        return pas;

    }

    public boolean checkStatus(String cipherText) throws SQLException {
        Connection conection = getConnection();
        String query = "select * from  mail_content  where mail_content = ?";

        //System.out.println(query);
        PreparedStatement pstmt = conection
                .prepareStatement(query);

        pstmt.setString(1, cipherText);
        System.out.println(pstmt.toString());
        ResultSet rst = pstmt.executeQuery();
        while (rst.next()) {
            if (rst.getInt("flag_enc") == 0) {
                return true;

            } else {
                return false;
            }
        }

        return false;
    }

    public ArrayList<String> saveR_b(String cipherText, int rB, ArrayList<String> info) throws SQLException {

        newtech n = new newtech();
        String str[] = cipherText.split(" ");
        List<String> al = new ArrayList<String>();
        al = Arrays.asList(str);
        int id = Integer.parseInt(info.get(1));
        String decrypted = n.decrypt(al, n.generatekey());
        //first decryption 
        
        
        System.out.println("decrypted : " + decrypted);
        int a = Character.getNumericValue(decrypted.charAt(decrypted.length() - 2));
        System.out.println("this is secret " + a);
       
        //key sync start
        
        Diffie_Hellman d = new Diffie_Hellman();
        keyMain k = new keyMain();

        Connection conection = getConnection();
        String query = "SELECT B.*,A.* FROM mail_content A INNER JOIN login_info B ON A.sender_id = B.userId WHERE A.mail_content = '" + cipherText + "' limit 1";
        PreparedStatement pstmt = conection
                .prepareStatement(query);

        System.out.println(pstmt.toString());
        ResultSet rst = pstmt.executeQuery(query);
        while (rst.next()) {
            info.set(3, rst.getString("username"));
            info.set(4, rst.getInt("mail_id") + "");
        }
        System.out.println(info.toString() + "save method ");
        k.generateNewKey(id, d.computekeyForA(rB, a), info);
       
 //key sync ho gae ha       

// System.out.println(decrypted.substring(0, decrypted.length() - 2));
        System.out.println(n.generateNewkey(info));
        ArrayList<String> enc = n.encrypt(decrypted.substring(0, decrypted.length() - 2), n.generateNewkey(info));

        StringBuilder listString = new StringBuilder();
        for (String s : enc) {

            listString.append(s + " ");
        }
        String enc_content = listString.toString();

        conection = getConnection();
        query = "update   mail_content set r_B =?,flag_enc = 1 , mail_content = ? where mail_content = ?";

        //System.out.println(query);
        pstmt = conection
                .prepareStatement(query);

        pstmt.setInt(1, rB);
        pstmt.setString(2, enc_content);

        pstmt.setString(3, cipherText);
        System.out.println(pstmt.toString());
        pstmt.executeUpdate();
        return enc;
    }

}
