package accounting.files.controller.error;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
	@ExceptionHandler(NoSuchElementException.class)
	public Map<String, String> handleNoSuchElementException(NoSuchElementException ex) {
		log.error(Messages.getString("GlobalErrorHandler.0"), ex.getMessage()); //$NON-NLS-1$
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put(Messages.getString("GlobalErrorHandler.1"), ex.toString()); //$NON-NLS-1$
		return errorResponse;
	}
}
