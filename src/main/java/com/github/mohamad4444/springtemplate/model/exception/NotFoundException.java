package com.github.mohamad4444.springtemplate.model.exception;

public class NotFoundException extends RuntimeException
{
    public NotFoundException(String message)
    {
        super(message);
    }
}
