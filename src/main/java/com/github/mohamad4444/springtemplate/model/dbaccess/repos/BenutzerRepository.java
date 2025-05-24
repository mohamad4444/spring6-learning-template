package com.github.mohamad4444.springtemplate.model.dbaccess.repos;

import com.github.mohamad4444.springtemplate.model.Benutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenutzerRepository extends JpaRepository<Benutzer, Integer>, BenutzerRepositoryJPA
{
    @Query("select b from Benutzer b")
    public List<Benutzer> getAll();

    @Query("select b from Benutzer b where b.benutzername = ?1")
    public Benutzer getBenutzerByBenutzername(String benutzername);

    @Query("select b from Benutzer b where b.id = ?1")
    public Benutzer getById(int id);
}
