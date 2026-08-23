package com.github.mohamad4444.springtemplate.security;

import com.github.mohamad4444.springtemplate.model.Benutzer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccessManager
{
    private Map<AccessToken, Benutzer> accessList = new ConcurrentHashMap<>();


    public AccessToken createBenutzerToken(Benutzer benutzer)
    {
        String uuid = UUID.randomUUID().toString();
        AccessToken accessToken = new AccessToken(uuid);

        accessList.put(accessToken, benutzer);

        return accessToken;
    }

    public boolean removeBenutzerToken(AccessToken accessToken)
    {
        return accessList.remove(accessToken) != null;
    }

    public boolean hasAccess(AccessToken accessToken)
    {
        return accessList.containsKey(accessToken);
    }

    public Benutzer getBenutzer(AccessToken accessToken)
    {
        return accessList.get(accessToken);
    }
}
