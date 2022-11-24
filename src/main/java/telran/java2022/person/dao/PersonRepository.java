package telran.java2022.person.dao;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;
import telran.java2022.person.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {

	Stream<Person> findPersonsByAddressCity(String city);
	
	Stream<Person> findPersonsByName(String name);
	
	Stream<Person> findPersonsByBirthDateBetween(LocalDate minAge, LocalDate maxAge);
}
