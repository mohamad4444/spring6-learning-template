package com.github.mohamad4444.springtemplate.api;

import com.github.mohamad4444.springtemplate.api.dto.LoginDtoIn;
import com.github.mohamad4444.springtemplate.api.dto.LoginDtoOut;
import com.github.mohamad4444.springtemplate.api.security.AccessManager;
import com.github.mohamad4444.springtemplate.api.security.AccessToken;
import com.github.mohamad4444.springtemplate.model.Benutzer;
import com.github.mohamad4444.springtemplate.model.dbaccess.DBAccessService;
import com.github.mohamad4444.springtemplate.util.PasswordTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("access")
public class AccessController
{
    private final AccessManager accessManager;
    private final DBAccessService dbAccessService;

    @Autowired
    public AccessController(AccessManager accessManager, DBAccessService dbAccessService)
    {
        this.accessManager = accessManager;
        this.dbAccessService = dbAccessService;
    }

    @PostMapping
    public ResponseEntity<LoginDtoOut> login(@RequestBody LoginDtoIn loginDtoIn)
    {
        String benutzerName = loginDtoIn.getBenutzername();
        String password = loginDtoIn.getPassword();

        Benutzer benutzer = dbAccessService.getBenutzerByBenutzername(benutzerName);

        if ( PasswordTools.checkPassword(password, benutzer.getPasswordSalt(), benutzer.getPasswordHash()) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AccessToken accessToken = accessManager.createBenutzerToken(benutzer);
        LoginDtoOut returnData = new LoginDtoOut( benutzer.getId(), benutzer.getBenutzername(), accessToken);

        return ResponseEntity.ok(returnData);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> logout(@RequestHeader AccessToken accessToken)
    {
        return ResponseEntity.ok(accessManager.removeBenutzerToken( accessToken ));
    }
}
