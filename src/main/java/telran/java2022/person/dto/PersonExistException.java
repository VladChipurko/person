package telran.java2022.person.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PersonExistException extends RuntimeException{

	private static final long serialVersionUID = 2924302768637974536L;

}
