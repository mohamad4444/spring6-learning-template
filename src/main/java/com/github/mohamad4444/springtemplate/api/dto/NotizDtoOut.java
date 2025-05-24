package com.github.mohamad4444.springtemplate.api.dto;


import com.github.mohamad4444.springtemplate.model.Notiz;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class NotizDtoOut
{
    private static final DateTimeFormatter fomatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale( Locale.forLanguageTag("de"));

    private final String inhalt;
    private final String erzeugungsDatum;

    public NotizDtoOut(Notiz notiz)
    {
        this.inhalt = notiz.getInhalt();
        this.erzeugungsDatum = fomatter.format( notiz.getDatum() );
    }

    public String getInhalt()
    {
        return inhalt;
    }

    public String getErzeugungsDatum()
    {
        return erzeugungsDatum;
    }
}
