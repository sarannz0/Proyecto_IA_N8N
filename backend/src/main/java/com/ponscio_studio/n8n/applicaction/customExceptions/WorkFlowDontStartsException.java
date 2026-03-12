package com.ponscio_studio.n8n.applicaction.customExceptions;

public class WorkFlowDontStartsException extends RuntimeException {
    
    public WorkFlowDontStartsException(String msg) {
        super(msg);
    }
}
