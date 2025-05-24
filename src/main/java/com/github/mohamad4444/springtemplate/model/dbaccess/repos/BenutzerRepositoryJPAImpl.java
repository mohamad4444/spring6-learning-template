package com.github.mohamad4444.springtemplate.model.dbaccess.repos;

import com.github.mohamad4444.springtemplate.model.Benutzer;
import com.github.mohamad4444.springtemplate.util.PasswordTools;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BenutzerRepositoryJPAImpl implements BenutzerRepositoryJPA
{
    EntityManager entityManager;

    @Autowired
    public BenutzerRepositoryJPAImpl(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public Benutzer createBenutzer(String vorname, String nachname, String benutzername, String password)
    {
            byte[] passwordSalt = PasswordTools.generateSalt();
            byte[] passwordHash = PasswordTools.generatePasswordHash(password, passwordSalt);

            Benutzer benutzer = new Benutzer(vorname, nachname, benutzername, passwordSalt, passwordHash);

            entityManager.persist(benutzer);
            entityManager.flush();
            entityManager.refresh(benutzer);

            return benutzer;
    }

    @Override
    public Benutzer update(int id, String vorname, String nachname, String password)
    {
        Benutzer benutzer = entityManager.find(Benutzer.class, id);
        byte[] pwd_salt = benutzer.getPasswordSalt();
        byte[] pwd_hash = PasswordTools.generatePasswordHash(password, pwd_salt);

        benutzer.setVorname( vorname );
        benutzer.setNachname( nachname );
        benutzer.setPasswordHash( pwd_hash );

        entityManager.merge(benutzer);
        entityManager.flush();
        entityManager.refresh(benutzer);

        return benutzer;
    }

    @Override
    public Benutzer mergeAndRefresh(Benutzer benutzer)
    {
        entityManager.persist(benutzer);
        entityManager.flush();
        entityManager.refresh(benutzer);

        return benutzer;
    }


    @Override
    public void delete(int id)
    {
        Benutzer benutzer = entityManager.find(Benutzer.class, id);
        entityManager.remove(benutzer);
        return;
    }
}
