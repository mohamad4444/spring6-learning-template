package com.github.mohamad4444.springtemplate.model.exception;

public class UsernameExistsException extends RuntimeException
{
    public UsernameExistsException(String message)
    {super(message);
    }
}
