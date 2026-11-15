package com.github.mohamad4444.springtemplate.controller;

import com.github.mohamad4444.springtemplate.model.*;
import com.github.mohamad4444.springtemplate.security.AccessManager;
import com.github.mohamad4444.springtemplate.security.AccessToken;
import com.github.mohamad4444.springtemplate.model.Benutzer;
import com.github.mohamad4444.springtemplate.model.Notiz;
import com.github.mohamad4444.springtemplate.model.dbaccess.DBAccessService;
import com.github.mohamad4444.springtemplate.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import com.github.mohamad4444.springtemplate.model.LoginDtoOut;

@CrossOrigin
@org.springframework.context.annotation.Profile("academic")
@RestController
@RequestMapping("/benutzer")
public class BenutzerController
{
    private final DBAccessService dbAccessService;

    private final AccessManager accessManager;

    @Autowired
    public BenutzerController(AccessManager accessManager, DBAccessService dbAccessService)
    {
        this.accessManager = accessManager;
        this.dbAccessService = dbAccessService;
    }

    @GetMapping
    public ResponseEntity<Collection<BenutzerDtoOut>> getAllBenutzer(@RequestHeader AccessToken accessToken)
    {
        checkAnmeldung( accessToken );

        return ResponseEntity.ok(dbAccessService.getAllBenutzer().stream()
                .map(BenutzerDtoOut::new)
                .toList()
        );
    }


    @GetMapping("{id}")
    public ResponseEntity<BenutzerDtoOut> getBenutzerById(@RequestHeader AccessToken accessToken, @PathVariable int id)
    {
        checkAnmeldung( accessToken );

        try {
            return ResponseEntity.ok(new BenutzerDtoOut(dbAccessService.getBenutzerById(id)));
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }


    @PostMapping
    public ResponseEntity<LoginDtoOut> createBenutzer(@RequestBody BenutzerCreateDtoIn benutzerCreateDtoIn)
    {
        try {
            Benutzer benutzer = dbAccessService.createBenutzer(benutzerCreateDtoIn.getVorname(),
                    benutzerCreateDtoIn.getNachname(),
                    benutzerCreateDtoIn.getBenutzername(),
                    benutzerCreateDtoIn.getPassword());

            AccessToken accessToken = accessManager.createBenutzerToken(benutzer);

            LoginDtoOut returnData = new LoginDtoOut( benutzer.getId(), benutzer.getBenutzername(), accessToken);
            return ResponseEntity.ok(returnData);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBenutzerById(@RequestHeader AccessToken accessToken, @PathVariable("id") int benutzerId)
    {
        checkAnmeldung( accessToken );
        checkBerechtigung( accessToken, benutzerId );

        try {
            accessManager.removeBenutzerToken( accessToken );
            dbAccessService.deleteBenutzer(benutzerId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<BenutzerDtoOut> updateBenutzer(@RequestHeader AccessToken accessToken, @PathVariable("id") int benutzerId, @RequestBody BenutzerUpdateDtoIn benutzerUpdateDtoIn)
    {
        checkAnmeldung( accessToken );
        checkBerechtigung( accessToken, benutzerId );

        try {
            return ResponseEntity.ok(
                    new BenutzerDtoOut(dbAccessService.updateBenutzer(benutzerId,
                            benutzerUpdateDtoIn.getVorname(),
                            benutzerUpdateDtoIn.getNachname(),
                            benutzerUpdateDtoIn.getPassword()))
            );
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    @GetMapping("{id}/notizen")
    public ResponseEntity<Collection<NotizDtoOut>> getNotizen(@RequestHeader AccessToken accessToken, @PathVariable("id") int benutzerId)
    {
        checkAnmeldung( accessToken );
        checkBerechtigung( accessToken, benutzerId );

        try {
            Benutzer benutzer = dbAccessService.getBenutzerById(benutzerId);

            return ResponseEntity.ok(
                    benutzer.getNotizen().stream()
                            .map(NotizDtoOut::new)
                            .toList()
            );
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    @PostMapping("{id}/notizen")
    public ResponseEntity<NotizDtoOut> createNotiz(@RequestHeader AccessToken accessToken, @PathVariable("id") int benutzerId, @RequestBody NotizDtoIn notizDtoIn)
    {
        checkAnmeldung( accessToken );
        checkBerechtigung( accessToken, benutzerId );

        try {
            Benutzer benutzer = dbAccessService.getBenutzerById(benutzerId);
            Notiz notiz = dbAccessService.addNotiz(benutzer, notizDtoIn.getInhalt() );

            return ResponseEntity.ok(new NotizDtoOut( notiz ));
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }

    private void checkAnmeldung(AccessToken accessToken)
    {
        if( !accessManager.hasAccess(accessToken) )
        {
            throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Benutzer nicht angemeldet");
        }
    }

    private void checkBerechtigung(AccessToken accessToken, int benutzerId )
    {
        if( accessManager.getBenutzer(accessToken).getId() != benutzerId )
        {
            throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Benutzer hat keine Berechtigung");
        }
    }
}
