package com.hyd.acornai.kb;

public class KnowledgeBaseException extends RuntimeException {

    public static KnowledgeBaseException wrap(Throwable cause) {
        if (cause instanceof KnowledgeBaseException k) {
            return k;
        } else {
            return new KnowledgeBaseException(cause);
        }
    }

    public KnowledgeBaseException() {
    }

    public KnowledgeBaseException(String message) {
        super(message);
    }

    public KnowledgeBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public KnowledgeBaseException(Throwable cause) {
        super(cause);
    }
}
