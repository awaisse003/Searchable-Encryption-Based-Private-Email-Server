/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mail.login;

import com.mail.database.config;
import java.sql.SQLException;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


@ManagedBean(name = "loginBean", eager = true)
@Dependent
public class loginBean {

    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        System.out.println(password);
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String check() throws SQLException, Exception {
        config c = new config();
        boolean res = c.login(this.username, this.password);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
       String user =  (String) sessionMap.get("username");
        if (res == true && user.equals("Admin")) {
          //System.out.println("admin ishere ");
            return "/admin/index?faces-redirect=true";
        } else if (res == true) {
            return "/home/index?faces-redirect=true";
        } else {
            return "login?faces-redirect=true";
        }
    }

    String password;

    public String onload() {
        System.out.println("onload");

        if (username == null) {
            return "login";

        } else {
            return "/home/index";

        }

    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
    }

    public loginBean() {
    }

}
