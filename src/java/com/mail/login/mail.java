/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mail.login;

import com.mail.database.config;
import com.mail.se.controller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 *
 * @author aarsh
 */
@ManagedBean(name = "mail", eager = true)
@Dependent
@RequestScoped
public class mail {

    private Part uploadedFile;
    private String folder = System.getProperty("user.dir") + "/mail/";
    String receiver_name, sender_name;
    String content, sender, receiver, attachment, title;
    String clicked_id;

    public String getClicked_id() {
        return clicked_id;
    }

    public void setClicked_id(String clicked_id) {
        this.clicked_id = clicked_id;
    }
    int id;
    int selected_id;
    String search_keyword;

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getSearch_keyword() {
        return search_keyword;
    }

    public void setSearch_keyword(String search_keyword) {
        this.search_keyword = search_keyword;
    }

    public int getSelected_id() {
        return selected_id;
    }

    public void setSelected_id(int selected_id) {
        this.selected_id = selected_id;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String search() throws IOException, FileNotFoundException, ClassNotFoundException, Exception {

        controller c = new controller();
        c.searchToken(this.search_keyword);
        Collection<Integer> i = c.search();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("keyword", this.search_keyword);
        sessionMap.put("searched_result", i);
        return "search_result?faces-redirect=true";

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

  
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<mail> gMail() throws SQLException {

       // System.out.println(this.getClicked_id()+" here is ");
        
        
       // List<mail> mailList = new ArrayList<mail>();
        config c = new config();

      //  mailList = c.mails();
      //  System.out.println(mailList);
      
        return c.mails();
    }

    public List<mail> gSearchedMail() throws SQLException {

        System.out.println(folder);
        List<mail> mailList = new ArrayList<mail>();
        config c = new config();

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        Collection<Integer> i = (Collection<Integer>) sessionMap.get("searched_result");
        
        if (i != null) {
            mailList = c.getSpecificmails(i);
        }
        return mailList;

    }

    public List<mail> gClickedMail() throws SQLException {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String idd = request.getParameter("txtAnotherProperty");
        System.out.println(idd+" id is ");
        List<mail> mailList = new ArrayList<mail>();
        config c = new config();
        int id = Integer.parseInt(idd);
        if (id != 0) {
            mailList = c.getSpecificmails(id);
        }
        return mailList;

    }

    public List<mail> sentMail() throws SQLException {
       // List<mail> mailList = new ArrayList<mail>();
        config c = new config();
       // mailList = c.sentMails();
        return c.sentMails();

    }

    public void sendMail() throws SQLException, Exception {
        config c = new config();


            String content1 = content;
        System.out.println(content1);
        System.out.println(c.sent(getTitle(), content1, receiver, content, uploadedFile));
    }

    public mail() {
    }


}
