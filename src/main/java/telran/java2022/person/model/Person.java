package telran.java2022.person.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "persons")
public class Person {

	@Id
	Integer id;
	@Setter
	String name;
	LocalDate birthDate;
	@Setter
//	@Embedded
//	аннотация для включения этого поля в столбцы person
	Address address;
	
	public String getCity() {
		return address.getCity();
	}
}
