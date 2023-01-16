package com.nordea.transfer_system.processing;

import lombok.NoArgsConstructor;

/**
 * Custom Esception for processing steps
 */
@NoArgsConstructor
public class ProcessingException extends Exception {

    public ProcessingException(String errorMessage) {
        super(errorMessage);
    }
}
