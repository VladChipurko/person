package telran.java2022.person.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Entity(name = "Person")
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Person implements Serializable{

	private static final long serialVersionUID = 5956815699217122501L;
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
