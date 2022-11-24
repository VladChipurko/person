package telran.java2022.person.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.person.dao.PersonRepository;
import telran.java2022.person.dto.AddressDto;
import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.dto.PersonDto;
import telran.java2022.person.dto.PersonExistException;
import telran.java2022.person.dto.PersonNotFoundException;
import telran.java2022.person.model.Address;
import telran.java2022.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
	
	final PersonRepository personRepository;
	final ModelMapper modelMapper;

	@Override
	public boolean addPerson(PersonDto personDto) {
		if(personRepository.existsById(personDto.getId())) {
			throw new PersonExistException();
		}
		personRepository.save(modelMapper.map(personDto, Person.class));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		personRepository.deleteById(id);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setName(name);
		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setAddress(modelMapper.map(addressDto, Address.class));;
		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepository.findPersonsByAddressCity(city)
				.map(p->modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepository.findPersonsByName(name)
				.map(p->modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
		return personRepository.findPersonsByBirthDateBetween(LocalDate.now().minusYears(maxAge), LocalDate.now().minusYears(minAge))
				.map(p->modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		Map<String, List<Person>> res = StreamSupport.stream(personRepository.findAll().spliterator(), false)
					.collect(Collectors.groupingBy(p->p.getCity()));	
		return res.entrySet().stream()
				.map(c->new CityPopulationDto(c.getKey(), (long) c.getValue().size()))
				.collect(Collectors.toList());
	}

}
