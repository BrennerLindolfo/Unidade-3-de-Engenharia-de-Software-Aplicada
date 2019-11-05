package br.ufpb.dcx.esa.medievalbank.service;

import static org.junit.Assert.assertEquals;
import static br.ufpb.dcx.esa.medievalbank.service.AtendeeServiceTestHelper.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtendeeServiceTest {
	
	private static final int UNKNOWN_ID = 123456;

	public static final String EXAMPLE_NAME = "A Name";
	public static final String OTHER_NAME = "Other Name";


	private static final String EXAMPLE_EMAIL = "a@a.com";

	private static final String EXAMPLE_SSN = "623-76-7120";

	@Autowired
	private AtendeeService service;
	
	@Test
	public void t01_createAtendee() {
		Atendee createdAtendee = createAtendee(service, EXAMPLE_NAME);
				
		validateAtendee(EXAMPLE_NAME, null, createdAtendee);

		Atendee searchedAtendee = service.getOne(createdAtendee.getId());
		assertEquals(createdAtendee, searchedAtendee);
	}

	@Test
	public void t02_createAtendeeWithoutName() {
		Atendee atendee = new Atendee();
		String failMessage = "Test failed because the system accepted to create atendee without name";
		String expectedExceptionMessage = "Name is mandatory";
		tryCreateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
	}

	@Test
	public void t03_atendeeNameDuplicated() {
		createAtendee(service, EXAMPLE_NAME);

		Atendee atendee2 = new Atendee();
		atendee2.setName(EXAMPLE_NAME);  // The same name!
				
		String failMessage = "Test failed because the system accepted to create atendee with duplicated name";
		String expectedExceptionMessage = "Atendee name cannot be duplicated";
		tryCreateAtendeeWithError(service, atendee2, failMessage, expectedExceptionMessage);
	}
	
	@Test
	public void t04_createAtendeeWithAutomaticFields() {
		Atendee atendee = new Atendee();
		atendee.setName(EXAMPLE_NAME); 
		atendee.setId(123);

		String failMessage = "Test failed because the system accepted to create atendee with id already set";
		String expectedExceptionMessage = "Atendee id cannot be set";
		tryCreateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);

		Atendee atendee2 = new Atendee();
		atendee2.setName(EXAMPLE_NAME); 
		atendee2.setCreation(new Date());
		
		failMessage = "Test failed because the system accepted to create atendee with creation already set";
		expectedExceptionMessage = "Atendee creation date cannot be set";
		tryCreateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
	}
	
	@Test
	public void t05_createAtendeeWithInvalidEmail() {
		Atendee atendee = new Atendee();
		atendee.setName(EXAMPLE_NAME); 
		atendee.setEmail("sdsdfa.sds#");

		String failMessage = "Test failed because the system accepted to create atendee with invalid e-mail format";
		String expectedExceptionMessage = "Atendee e-mail format is invalid";
		tryCreateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
		
		
		atendee.setEmail("sdsdfa@@gmail.com");
		tryCreateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
		
		atendee.setEmail("sdsdfa#gmail.com");
		tryCreateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
		
		atendee.setEmail("sdsdfa@gmail");
		tryCreateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
	}
	
	@Test
	public void t06_updateAtendee() {
		Atendee createdAtendee = createAtendee(service, EXAMPLE_NAME, EXAMPLE_EMAIL);
		
		String otherEmail = "other@email.com";

		createdAtendee.setName(OTHER_NAME);
		createdAtendee.setEmail(otherEmail);
		
		Atendee updatedAtendee = service.update(createdAtendee);
		validateAtendee(OTHER_NAME, otherEmail, updatedAtendee);
		assertEquals(createdAtendee.getId(), updatedAtendee.getId());
		assertEquals(createdAtendee.getCreation(), updatedAtendee.getCreation());
		
		Atendee searchedAtendee = service.getOne(updatedAtendee.getId());
		assertEquals(updatedAtendee, searchedAtendee);
	}

	@Test
	public void t07_updateAtendeeWithImmutableFields() {
		Atendee createdAtendee = createAtendee(service, EXAMPLE_NAME, EXAMPLE_EMAIL, EXAMPLE_SSN);
		
		createdAtendee.setSSN("670-03-8924");
		
		String failMessage = "Test failed because the system accepted to update atendee with a new SSN";
		String expectedExceptionMessage = "Atendee SSN is immutable";

		tryUpdateAtendeeWithError(service, createdAtendee, failMessage, expectedExceptionMessage);
	}
	
	@Test
	public void t08_updateAtendeeWithUnknownId() {
		Atendee atendeeWithUnknownId = new Atendee();
		atendeeWithUnknownId.setId(UNKNOWN_ID);
		
		String failMessage = "Test failed because the system accepted to update atendee with an unknown id";
		String expectedExceptionMessage = "Atendee id not found: " + UNKNOWN_ID;

		tryUpdateAtendeeWithError(service, atendeeWithUnknownId, failMessage, expectedExceptionMessage);

	
		Atendee createdAtendee = createAtendee(service, EXAMPLE_NAME, EXAMPLE_EMAIL, EXAMPLE_SSN);
		createdAtendee.setId(UNKNOWN_ID);
		
		tryUpdateAtendeeWithError(service, createdAtendee, failMessage, expectedExceptionMessage);
	}
	
	@Test
	public void t09_updateAtendeeWithoutName() {
		Atendee createdAtendee = createAtendee(service, EXAMPLE_NAME, EXAMPLE_EMAIL, EXAMPLE_SSN);
		createdAtendee.setName(null);
				
		String failMessage = "Test failed because the system accepted to update atendee without name";
		String expectedExceptionMessage = "Name is mandatory";
		tryUpdateAtendeeWithError(service, createdAtendee, failMessage, expectedExceptionMessage);
	}
	
	@Test
	public void t10_updateAtendeeWithDuplicatedName() {
		createAtendee(service, EXAMPLE_NAME);

		Atendee atendee2 = createAtendee(service, OTHER_NAME);
		atendee2.setName(EXAMPLE_NAME); 
				
		String failMessage = "Test failed because the system accepted to update atendee with duplicated name";
		String expectedExceptionMessage = "Atendee name cannot be duplicated";
		tryUpdateAtendeeWithError(service, atendee2, failMessage, expectedExceptionMessage);
	}
	
	@Test
	public void t11_updateAtendeeWithAutomaticField() throws Exception {
		Atendee createdAtendee = createAtendee(service, EXAMPLE_NAME);

		Thread.sleep(10);
		createdAtendee.setCreation(new Date());
		
		String failMessage = "Test failed because the system accepted to update atendee with changed creation";
		String expectedExceptionMessage = "Atendee creation date cannot be changed";
		tryUpdateAtendeeWithError(service, createdAtendee, failMessage, expectedExceptionMessage);
	}
	
	@Test
	public void t12_updateAtendeeWithInvalidEmail() {
		Atendee atendee = createAtendee(service, EXAMPLE_NAME, EXAMPLE_EMAIL);

		String failMessage = "Test failed because the system accepted to update atendee with invalid e-mail format";
		String expectedExceptionMessage = "Atendee e-mail format is invalid";
		
		
		atendee.setEmail("sdsdfa.sds#");
		tryUpdateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
		
		atendee.setEmail("sdsdfa@@gmail.com");
		tryUpdateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
		
		atendee.setEmail("sdsdfa#gmail.com");
		tryUpdateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
		
		atendee.setEmail("sdsdfa@gmail");
		tryUpdateAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
	}
	
	@Test
	public void t13_deleteAtendee() {
		Atendee atendee = createAtendee(service, EXAMPLE_NAME, EXAMPLE_EMAIL);
		
		service.delete(atendee);
		
		String failMessage = "Test failed because the system returned an unknown atendee";
		String expectedExceptionMessage = "Unknown Atendee id: " + atendee.getId();
		
		tryGetOneAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
	}

	@Test
	public void t14_deleteUnknownAtendee() {
		Atendee atendee = new Atendee();
		atendee.setId(UNKNOWN_ID);
		
		service.delete(atendee);
		
		String failMessage = "Test failed because the system accepted to delete atendee with an unknown id";
		String expectedExceptionMessage = "Atendee id not found: " + UNKNOWN_ID;
		
		tryDeleteAtendeeWithError(service, atendee, failMessage, expectedExceptionMessage);
		
		failMessage = "Test failed because the system accepted to delete a null atendee";
		expectedExceptionMessage = "Null atendee";
		tryDeleteAtendeeWithError(service, null, failMessage, expectedExceptionMessage);
	}

	
	

}
