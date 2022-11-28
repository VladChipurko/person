package telran.java2022.person.service;

import java.time.LocalDate;
import java.util.stream.Collectors;


//import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java2022.person.dao.PersonRepository;
import telran.java2022.person.dto.AddressDto;
import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.dto.PersonDto;
import telran.java2022.person.dto.PersonNotFoundException;
import telran.java2022.person.model.Address;
import telran.java2022.person.model.Child;
import telran.java2022.person.model.Employee;
import telran.java2022.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {
	
	final PersonRepository personRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public boolean addPerson(PersonDto personDto) {
		if(personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(modelMapper.map(personDto, getModelClass(personDto)));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		return modelMapper.map(person, getDtoClass(person));
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		personRepository.deleteById(id);
		return modelMapper.map(person, getDtoClass(person));
	}

	@Override
	@Transactional
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setName(name);
		return modelMapper.map(person, getDtoClass(person));
	}

	@Override
	@Transactional
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setAddress(modelMapper.map(addressDto, Address.class));
		return modelMapper.map(person, getDtoClass(person));
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepository.findPersonsByAddressCity(city)
				.map(p->modelMapper.map(p, getDtoClass(p)))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepository.findPersonsByName(name)
				.map(p->modelMapper.map(p, getDtoClass(p)))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
		return personRepository.findPersonsByBirthDateBetween(LocalDate.now().minusYears(maxAge), LocalDate.now().minusYears(minAge))
				.map(p->modelMapper.map(p, getDtoClass(p)))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
//		Map<String, List<Person>> res = StreamSupport.stream(personRepository.findAll().spliterator(), false)
//					.collect(Collectors.groupingBy(p->p.getCity()));	
//		return res.entrySet().stream()
//				.map(c->new CityPopulationDto(c.getKey(), (long) c.getValue().size()))
//				.collect(Collectors.toList());
		return personRepository.getCitiesPopulation();
	}

	@Override
	public void run(String... args) throws Exception {
		Person person = new Person(1000, "John", LocalDate.of(1985, 4, 11), 
				new Address("Tel Aviv", "Ben Gvirol", 87));
		Child child = new Child(2000, "Mosche", LocalDate.of(2018, 7, 5), 
				new Address("Ashkelon", "Bar Kohva", 21), "Shalom");
		Employee employee = new Employee(3000, "Sarah", LocalDate.of(1995, 11, 23), 
				new Address("Rehovot", "Herzl", 7), "Motorola", 20000);
		personRepository.save(person);
		personRepository.save(child);
		personRepository.save(employee);
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends Person> getModelClass(PersonDto personDto) {
		String className = "telran.java2022.person.model." + personDto.getClass().getSimpleName();
		className = className.substring(0, className.length() - 3);
		Class<?> clazz= null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("mistake - " + className);
		}
		return (Class<? extends Person>) clazz;
//		if(personDto instanceof EmployeeDto) {
//			return Employee.class;
//		}
//		if(personDto instanceof ChildDto) {
//			return Child.class;
//		}
//		return Person.class;
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends PersonDto> getDtoClass(Person person) {
		String className = "telran.java2022.person.dto." + person.getClass().getSimpleName() + "Dto";
		Class<?> clazz= null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("mistake - " + className);
		}
		return (Class<? extends PersonDto>) clazz;
//		if(person instanceof Employee) {
//			return EmployeeDto.class;
//		}
//		if(person instanceof Child) {
//			return ChildDto.class;
//		}
//		return PersonDto.class;
	}

	@Override
	public Iterable<PersonDto> FindEmployeeBySalary(int min, int max) {
		return personRepository.FindEmployeesBySalary(min, max).stream()
				.map(p->modelMapper.map(p, getDtoClass(p)))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<PersonDto> getChildren() {
		return personRepository.getChildren().stream()
					.map(p->modelMapper.map(p, getDtoClass(p)))
					.collect(Collectors.toList());
	}

}
