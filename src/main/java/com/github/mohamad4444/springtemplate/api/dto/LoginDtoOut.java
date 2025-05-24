package com.github.mohamad4444.springtemplate.api.dto;


import com.github.mohamad4444.springtemplate.api.security.AccessToken;

public class LoginDtoOut
{
    private final int id;
    private final String benutzername;
    private final AccessToken credential;

    public LoginDtoOut(int id, String benutzername, AccessToken accessToken)
    {
        this.id = id;
        this.benutzername = benutzername;
        this.credential = accessToken;
    }

    public int getId()
    {
        return id;
    }

    public String getBenutzername()
    {
        return benutzername;
    }

    public AccessToken getCredential()
    {
        return credential;
    }
}
