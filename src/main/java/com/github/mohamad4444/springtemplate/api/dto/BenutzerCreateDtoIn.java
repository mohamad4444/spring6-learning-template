package com.github.mohamad4444.springtemplate.api.dto;

public class BenutzerCreateDtoIn
{
    String vorname;
    String nachname;
    String benutzername;
    String password;

    public String getVorname()
    {
        return vorname;
    }

    public void setVorname(String vorname)
    {
        this.vorname = vorname;
    }

    public String getNachname()
    {
        return nachname;
    }

    public void setNachname(String nachname)
    {
        this.nachname = nachname;
    }

    public String getBenutzername()
    {
        return benutzername;
    }

    public void setBenutzername(String benutzername)
    {
        this.benutzername = benutzername;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
