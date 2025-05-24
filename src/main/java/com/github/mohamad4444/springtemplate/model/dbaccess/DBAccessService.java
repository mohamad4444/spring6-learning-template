package com.github.mohamad4444.springtemplate.model.dbaccess;

import com.github.mohamad4444.springtemplate.model.Benutzer;
import com.github.mohamad4444.springtemplate.model.Notiz;
import com.github.mohamad4444.springtemplate.model.dbaccess.repos.BenutzerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DBAccessService
{
    private BenutzerRepository benutzerRepository;

    @Autowired
    public DBAccessService(BenutzerRepository benutzerRepository)
    {
        this.benutzerRepository = benutzerRepository;
    }

    public List<Benutzer> getAllBenutzer()
    {
        return benutzerRepository.getAll();
    }

    public Benutzer getBenutzerByBenutzername(String benutzername)
    {
        return  benutzerRepository.getBenutzerByBenutzername(benutzername);
    }

    public Benutzer getBenutzerById(int id)
    {
        return benutzerRepository.getById(id);
    }

    public Benutzer createBenutzer(String vorname, String nachname, String benutzername, String password)
    {
        return benutzerRepository.createBenutzer( vorname,  nachname,  benutzername,  password);
    }
    public Benutzer updateBenutzer(int id, String vorname, String nachname, String password)
    {
        return benutzerRepository.update(id, vorname, nachname, password );
    }
    public void deleteBenutzer(int id)
    {
        benutzerRepository.delete(id);
    }


    public Notiz addNotiz(Benutzer benutzer, String inhalt)
    {
        Notiz notiz = new Notiz(inhalt);
        benutzer.addNotiz( notiz);

        benutzerRepository.mergeAndRefresh( benutzer );

        return notiz;
    }

}
