package com.github.mohamad4444.springtemplate.api.dto;


import com.github.mohamad4444.springtemplate.model.Benutzer;

public class BenutzerDtoOut
{
    int id;
    String vorname;
    String nachname;
    String benutzername;

    public BenutzerDtoOut(Benutzer person)
    {
        this.id = person.getId();
        this.vorname = person.getVorname();
        this.nachname = person.getNachname();
        this.benutzername = person.getBenutzername();
    }

    public int getId()
    {
        return id;
    }

    public String getVorname()
    {
        return vorname;
    }

    public String getNachname()
    {
        return nachname;
    }

    public String getBenutzername()
    {
        return benutzername;
    }
}
