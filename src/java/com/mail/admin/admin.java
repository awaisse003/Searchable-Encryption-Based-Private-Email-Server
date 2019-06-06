/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mail.admin;

import com.mail.database.config;
import com.mail.login.mail;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aarsh
 */
@Named(value = "admin")
@ManagedBean
@RequestScoped
public class admin {

 
    int id;
    String name;
    String email;
    String password;
    String gender;
    String address;
    ArrayList usersList ;
    private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    Connection connection;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    } 
    // Used to establish connection
    public Connection getConnection(){
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
    public void test()
    {
        System.out.println( "{you clicked me ");
    }
    
    // Used to fetch all records
    public ArrayList usersList(){
        System.out.println(System.getProperty("user.dir"));
        try{
            usersList = new ArrayList();
            connection = getConnection();
            Statement stmt=getConnection().createStatement();  
            ResultSet rs=stmt.executeQuery("select * from login_info");  
            while(rs.next()){
                
                System.out.println(rs.getString("name"));
                User user = new User();
                user.setId(rs.getInt("userId"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getString("gender"));
                user.setAddress(rs.getString("address"));
                usersList.add(user);
            }
            connection.close();        
        }catch(Exception e){
            System.out.println(e);
        }
        return usersList;
    }
    // Used to save user record
    public String save(){
        int result = 0;
        try{
         //   System.out.println(name+" name is :"+gender+" gender : " +getGender());
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into login_info(Name,username,email,password,gender,address) values(?,?,?,?,?,?)");
            stmt.setString(1, name);
            stmt.setString(2, name.replaceAll("\\s+",""));
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, gender);
            stmt.setString(6, address);
            result = stmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }
        if(result !=0)
            return "index.xhtml?faces-redirect=true";
        else return "create.xhtml?faces-redirect=true";
    }
    // Used to fetch record to update
    public String edit(int id){
        User user = null;
        System.out.println(id);
        try{
            connection = getConnection();
            Statement stmt=getConnection().createStatement();  
            ResultSet rs=stmt.executeQuery("select * from login_info where userId = "+(id));
            rs.next();
            user = new User();
            user.setId(rs.getInt("userId"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setGender(rs.getString("gender"));
            user.setAddress(rs.getString("address"));
            user.setPassword(rs.getString("password"));  
            System.out.println(rs.getString("password"));
            sessionMap.put("editUser", user);
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }       
        return "edit.xhtml?faces-redirect=true";
    }
    // Used to update user record
    public String update(User u){
        //int result = 0;
        try{
            connection = getConnection();  
            PreparedStatement stmt=connection.prepareStatement("update login_info set name=?,email=?,password=?,gender=?,address=? where userId=?");  
            stmt.setString(1,u.getName());  
            stmt.setString(2,u.getEmail());  
            stmt.setString(3,u.getPassword());  
            stmt.setString(4,u.getGender());  
            stmt.setString(5,u.getAddress());  
            stmt.setInt(6,u.getId());  
            stmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println();
        }
        return "index.xhtml?faces-redirect=true";      
    }
    // Used to delete user record
    public void delete(int id){
        try{
            connection = getConnection();  
            PreparedStatement stmt = connection.prepareStatement("delete from login_info where userId = "+id);  
            stmt.executeUpdate();  
        }catch(Exception e){
            System.out.println(e);
        }
    }
    // Used to set user gender
    public String getGenderName(char gender){
        if(gender == 'M'){
            return "Male";
        }else return "Female";
    }
   
    public admin() {
    }
    
//    public static void main(String args[]) throws SQLException{
//        admin a = new admin();
//        a.getUser();
//    }
    
}
