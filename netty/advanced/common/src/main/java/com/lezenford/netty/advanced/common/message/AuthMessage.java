package com.lezenford.netty.advanced.common.message;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.security.KeyStore;
import java.util.Map;

public class AuthMessage extends Message implements LoginModule {
    private LoginContext login;
    private KeyStore.PasswordProtection pass;
//    private String login;
//    private String pass;

    public LoginContext getLogin() {return login;}
    public void setLogin(LoginContext login) {
        this.login = login;
    }

    public KeyStore.PasswordProtection getPass() { return pass; }
    public void setPass(KeyStore.PasswordProtection pass) {
        this.pass = pass;
    }

    @Override
    public String toString(){
        return "AuthMessage{"+
                "login="+ login + '\'' +
                "pass="+ pass +
                '}';
    }

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {

    }

    @Override
    public boolean login() throws LoginException {
        return false;
    }

    @Override
    public boolean commit() throws LoginException {
        return false;
    }

    @Override
    public boolean abort() throws LoginException {
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        return false;
    }
}
