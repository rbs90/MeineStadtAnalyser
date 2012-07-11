package de.rbs.meinestadt.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.rbs.meinestadt.DatabaseManager;
import de.rbs.meinestadt.datatypes.Address;

public class AddressTest {

	@Before 
	public void initialize() {
		DatabaseManager.init();
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
	}
	
	@Test
	public void testAddress3() {
		Address address = new Address("Dreibrüderweg 5b");
		assertTrue(address.getStreet().getName().equals("Dreibrüderweg"));
		assertTrue(address.getHnr() == 5);
	}

}
