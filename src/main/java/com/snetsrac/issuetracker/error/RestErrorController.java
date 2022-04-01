package com.snetsrac.issuetracker.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Problem> handle(HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf((Integer) request.getAttribute("javax.servlet.error.status_code"));
        String message = (String) request.getAttribute("javax.servlet.error.message");

        if (status == HttpStatus.NOT_FOUND) {
            message = "No resource found at " + (String) request.getAttribute("javax.servlet.error.request_uri");
        }

        Problem problem = new Problem(status, message);

        return ResponseEntity.status(status).body(problem);
    }
}
