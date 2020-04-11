package ch.bader.budget.server.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NoAccountException.class)
	protected ResponseEntity<Object> handleMyException(NoAccountException ex, WebRequest req) {
		Object resBody = "No Account found with the id: " + ex.id;
		return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
	}
}
