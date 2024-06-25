package com.ruwan.BookNetwork.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    public  Integer businessErrorCode;
    public String businessErrorDescription;
    public String error;
    private Set<String> validationErrors;
    private Map<String,String> errors;


}
