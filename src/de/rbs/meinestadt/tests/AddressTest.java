package de.rbs.meinestadt.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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
		Address address = new Address("Dreibrüderweg 5");
		assertTrue(address.getStreet().getName().equals("Dreibrüderweg"));
		assertTrue(address.getHnr() == 5);
	}
	
	@Test
	public void testAddress2() {
		Address address = new Address("Dreibrüderweg 5 b");
		assertTrue(address.getStreet().getName().equals("Dreibrüderweg"));
		assertTrue(address.getHnr() == 5);
		assertTrue(address.getHnrChar() == 'b');
	}
	
	@Test
	public void testAddress3() {
		Address address = new Address("Dreibrüderweg 5b");
		assertTrue(address.getStreet().getName().equals("Dreibrüderweg"));
		assertTrue(address.getHnr() == 5);
		assertTrue(address.getHnrChar() == 'b');
	}

	@Test
	public void testAddress4() {
		Address address = new Address("Dreibrüderweg");
		assertTrue(address.getStreet().getName().equals("Dreibrüderweg"));
		assertTrue(address.hasHnr() == false);
	}
	
	@Test
	public void testAddress5() {
		Address address = new Address("Dresdner Straße 105");
		assertTrue(address.getStreet().getName().equals("Dresdner Straße"));
		assertTrue(address.getHnr() == 105);
		assertTrue(address.hasHnr());
		assertTrue(address.hasHnrChar() == false);
	}
	
	@Test
	public void testAddress6() {
		Address address = new Address("Dresdner Straße 105 b");
		assertTrue(address.getStreet().getName().equals("Dresdner Straße"));
		assertTrue(address.getHnr() == 105);
		assertTrue(address.getHnrChar() == 'b');
		assertTrue(address.hasHnr());
		assertTrue(address.hasHnrChar());
	}
	
	@Test
	public void testAddress7() {
		Address address = new Address("Dresdner Straße 105b");
		assertTrue(address.getStreet().getName().equals("Dresdner Straße"));
		assertTrue(address.getHnr() == 105);
		assertTrue(address.getHnrChar() == 'b');
		assertTrue(address.hasHnr());
		assertTrue(address.hasHnrChar());
	}

	@Test
	public void testAddress8() {
		Address address = new Address("Dresdner Straße");
		assertTrue(address.getStreet().getName().equals("Dresdner Straße"));
		assertTrue(address.hasHnr() == false);
		assertTrue(address.hasHnr() == false);
		assertTrue(address.hasHnrChar() == false);
	}
}
