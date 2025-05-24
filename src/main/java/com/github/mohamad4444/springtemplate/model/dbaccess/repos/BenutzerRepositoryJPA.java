package com.github.mohamad4444.springtemplate.model.dbaccess.repos;


import com.github.mohamad4444.springtemplate.model.Benutzer;

public interface BenutzerRepositoryJPA
{
    public Benutzer createBenutzer(String vorname, String nachname, String benutzername, String password);
    public Benutzer update(int id, String vorname, String nachname, String password);
    public Benutzer mergeAndRefresh(Benutzer benutzer);
    public void delete(int id);
}
