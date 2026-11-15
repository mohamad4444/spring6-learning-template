package com.github.mohamad4444.springtemplate.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "benutzer")
public class Benutzer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    private String vorname;
    private String nachname;
    private String benutzername;
    @Column(name = "password_hash", nullable = false)
    private byte[] passwordHash;

    @Column(name = "password_salt", nullable = false)
    private byte[] passwordSalt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="benutzer_id")
    private final Set<Notiz> notizen = new TreeSet<>();


    public Benutzer(String vorname, String nachname, String benutzername, byte[] passwordSalt, byte[] passwordHash)
    {
        this.vorname = vorname;
        this.nachname = nachname;
        this.benutzername = benutzername;
        this.passwordSalt = passwordSalt;
        this.passwordHash = passwordHash;
    }

    public Benutzer() {}

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

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

    public byte[] getPasswordHash()
    {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordSalt()
    {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt)
    {
        this.passwordSalt = passwordSalt;
    }

    public void addNotiz(Notiz notiz)
    {
        notizen.add( notiz );
    }

    public Set<Notiz> getNotizen()
    {
        return Collections.unmodifiableSet( notizen );
    }

    @Override
    public boolean equals(Object o)
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Benutzer person = (Benutzer) o;
        return id == person.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                '}';
    }
}
