package telran.java2022.person.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonDto {

	Integer id;
	String name;
	LocalDate birthDate;
	AddressDto address;
}
