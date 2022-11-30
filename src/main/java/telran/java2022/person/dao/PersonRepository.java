package telran.java2022.person.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {

	//@Query("select p from Person p where p.name=?1")
	Stream<Person> findPersonsByAddressCity(String city);
	
	//@Query("select p from Person p where p.address.city=:city")
	Stream<Person> findPersonsByName(@Param("city") String name);
	
	Stream<Person> findPersonsByBirthDateBetween(LocalDate minAge, LocalDate maxAge);
	
	@Query("select new telran.java2022.person.dto.CityPopulationDto(p.address.city, count(p)) from Person p group by p.address.city order by count(p) desc")
	List<CityPopulationDto> getCitiesPopulation();
	
	@Query("select p from Person p where p.salary between :min and :max")
	List<Person> FindEmployeesBySalary(@Param("min") int min,@Param("max") int max);
//	List<Employee> findSalaryBetween(int min, int max);  --- correct query
	
	@Query("select p from Person p where p.kindergarten != 'null'")
	List<Person> getChildren();
//	List<Child> findChildrenBy();  --- correct query
	
//	это для join стратегии
//	@Query("select c from Child c")
//	List<Person> getChildren();
	
}
