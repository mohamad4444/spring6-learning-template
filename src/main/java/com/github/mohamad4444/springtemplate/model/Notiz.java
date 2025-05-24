package com.github.mohamad4444.springtemplate.model;

import com.github.mohamad4444.springtemplate.model.converter.LocalDateTimeConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notiz")
public class Notiz implements Comparable<Notiz>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime datum;
    private String inhalt;

    public Notiz()
    {

    }

    public Notiz(String inhalt)
    {
        this.inhalt = inhalt;
        this.datum = LocalDateTime.now();
    }

    public int getId()
    {
        return id;
    }

    public LocalDateTime getDatum()
    {
        return datum;
    }

    public void setDatum(LocalDateTime datum)
    {
        this.datum = datum;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setInhalt(String inhalt)
    {
        this.inhalt = inhalt;
    }

    public String getInhalt()
    {
        return inhalt;
    }

    @Override
    public int compareTo(Notiz other)
    {
        return other.datum.compareTo( this.datum);
    }

    @Override
    public boolean equals(Object o)
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Notiz notiz = (Notiz) o;
        return id == notiz.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    @Override
    public String toString()
    {
        return "Notiz{" +
                "erzeugungsDatum=" + datum +
                ", inhalt='" + inhalt + '\'' +
                '}';
    }
}
