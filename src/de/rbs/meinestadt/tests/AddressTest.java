package de.rbs.meinestadt.tests;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.rbs.meinestadt.DatabaseManager;
import de.rbs.meinestadt.datatypes.Address;

public class AddressTest {

	@BeforeClass 
	public static void initializeDB() {
		DatabaseManager.init();
    }
	
	@AfterClass 
	public static void closeDB() {
		DatabaseManager.close();
    }
	
	@Test
	public void testAddress1() {
		Address address = new Address("Dreibrüderweg 5", 20);
		assertTrue(address.getStreet().getName().equals("Dreibrüderweg"));
		assertTrue(address.getHnr() == 5);
	}
	
	@Test
	public void testAddress2() {
		Address address = new Address("Dreibrüderweg 5 b", 20);
		assertTrue(address.getStreet().getName().equals("Dreibrüderweg"));
		assertTrue(address.getHnr() == 5);
		assertTrue(address.getHnrChar() == 'b');
	}
	
	@Test
	public void testAddress3() {
		Address address = new Address("Dreibrüderweg 5b", 20);
		assertTrue(address.getStreet().getName().equals("Dreibrüderweg"));
		assertTrue(address.getHnr() == 5);
		assertTrue(address.getHnrChar() == 'b');
	}

	@Test
	public void testAddress4() {
		Address address = new Address("Dreibrüderweg", 20);
		assertTrue(address.getStreet().getName().equals("Dreibrüderweg"));
		assertTrue(address.hasHnr() == false);
	}
	
	@Test
	public void testAddress5() {
		Address address = new Address("Dresdner Straße 105", 20);
		assertTrue(address.getStreet().getName().equals("Dresdner Straße"));
		assertTrue(address.getHnr() == 105);
		assertTrue(address.hasHnr());
		assertTrue(address.hasHnrChar() == false);
	}
	
	@Test
	public void testAddress6() {
		Address address = new Address("Dresdner Straße 105 b", 20);
		assertTrue(address.getStreet().getName().equals("Dresdner Straße"));
		assertTrue(address.getHnr() == 105);
		assertTrue(address.getHnrChar() == 'b');
		assertTrue(address.hasHnr());
		assertTrue(address.hasHnrChar());
	}
	
	@Test
	public void testAddress7() {
		Address address = new Address("Dresdner Straße 105b", 20);
		assertTrue(address.getStreet().getName().equals("Dresdner Straße"));
		assertTrue(address.getHnr() == 105);
		assertTrue(address.getHnrChar() == 'b');
		assertTrue(address.hasHnr());
		assertTrue(address.hasHnrChar());
	}

	@Test
	public void testAddress8() {
		Address address = new Address("Dresdner Straße", 20);
		assertTrue(address.getStreet().getName().equals("Dresdner Straße"));
		assertTrue(address.hasHnr() == false);
		assertTrue(address.hasHnr() == false);
		assertTrue(address.hasHnrChar() == false);
	}
}
