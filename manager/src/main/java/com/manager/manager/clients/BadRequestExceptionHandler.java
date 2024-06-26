package com.manager.manager.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import com.manager.manager.exceptions.BadRequestException;

@Component
public class BadRequestExceptionHandler {

    public BadRequestException getException(ProblemDetail problemDetail) {
        if (Objects.equals(problemDetail.getProperties()
                .get("errors").getClass(), ArrayList.class))  {
                return new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
            } else {
                return new BadRequestException(List.of((String) problemDetail.getProperties().get("errors")));
            } 
    }

}
