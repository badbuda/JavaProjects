package il.co.ilrd.complex;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestComplex {

	@Test
	public void testIsReal() {
		Complex aisReal =   Complex.createFromReal(0);
		aisReal.setImaginary(0);
		aisReal.setReal(3);
		assertTrue(aisReal.isReal());
	}

	@Test
	public void testIsImaginary() {
		Complex aisImaginary =  Complex.createFromImaginary(3);
		aisImaginary.setReal(3);
		assertTrue(aisImaginary.isImaginary());
	}

	@Test
	public void testAdd() {
		Complex aadd =  Complex.createFromImaginary(6.6);
		aadd.setReal(4);

		Complex badd =   Complex.createFromImaginary(6.6);
		badd.setReal(3);
		
		Complex result =   Complex.createFromImaginary(13.2);
		result.setReal(7);

		assertEquals(result, aadd.add(badd));
	}

	@Test
	public void testSubtract() {
		
		Complex asubtract =  Complex.createFromImaginary(6.6);
		asubtract.setReal(4.0);

		Complex bsubtract =   Complex.createFromImaginary(6.6);
		bsubtract.setReal(3);
		
		Complex result =   Complex.createFromImaginary(0.0);
		result.setReal(1);

		assertEquals(result , asubtract.subtract(bsubtract));
	}

	@Test
	public void testMultiply() {
		Complex amultiply =  Complex.createFromImaginary(6.6);
		amultiply.setReal(4.0);

		Complex bmultiply =   Complex.createFromImaginary(6.6);
		bmultiply.setReal(3);
		
		Complex result =   Complex.createFromImaginary(46.199999999999996);
		result.setReal(-31.559999999999995);

		assertEquals (result, amultiply.multiply(bmultiply));
	}


	@Test
	public void testDevide() {
		Complex adivide =  Complex.createFromImaginary(13);
		adivide.setReal(3);

		Complex bdivide =   Complex.createFromImaginary(17);
		bdivide.setReal(7);

		Complex result =   Complex.createFromImaginary(0.11834319526627218);
		result.setReal(0.7159763313609467);
		
		assertEquals(result, adivide.divide(bdivide));
	}
	
	@Test
	public void testCompare() {
		Complex a =  Complex.createFromImaginary(13);
		a.setReal(3);

		Complex b =   Complex.createFromImaginary(17);
		b.setReal(7);
		
		Complex c =   Complex.createFromImaginary(13);
		c.setReal(3);

		assertEquals(-1, a.compareTo(b));
		assertEquals(0, a.compareTo(c));

	}
	
	@Test
	public void testEquals() {
		Complex a =  Complex.createFromImaginary(13);
		a.setReal(3);

		Complex b =   Complex.createFromImaginary(17);
		b.setReal(7);
		
		Complex c =   Complex.createFromImaginary(13);
		c.setReal(3);

		assertFalse(a.equals(b));
		assertTrue(a.equals(c));
	}
	
	@Test
	public void testToString() {
		Complex a =  Complex.createFromImaginary(13);
		a.setReal(3);

		assertEquals("3.0 + 13.0i",a.toString());
	}
	
	@Test
	public void testHashCode() {
		Complex a =  Complex.createFromImaginary(13);
		a.setReal(3);

		assertEquals(-2133720127,a.hashCode());
	}
}
