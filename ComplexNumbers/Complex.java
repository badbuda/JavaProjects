package il.co.ilrd.complex;

public class Complex implements Comparable<Complex>{
	private double real;
	private double imaginary;
	
	Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public static Complex createFromReal(double realUser)
	{
		return new Complex(realUser, 0);
	}
	
	public static Complex createFromImaginary(double imaginaryUser)
	{
		return new Complex(0, imaginaryUser);
	}
	public double getReal() {
		return (this.real);	
	}

	public void setReal(double real) {
		this.real = real;	
	}
	public double getImaginary() {
		return (this.imaginary);
	}
	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}
	public boolean isReal()
	{
		return (this.imaginary == 0);
	}
	
	public boolean isImaginary()
	{
		return (this.imaginary != 0);
	}

	@Override
	public int hashCode() {
		int result = 1;
		long realTemp = Double.doubleToLongBits(real);
		long imaginaryTemp = Double.doubleToLongBits(imaginary);
		
		result = 31 * result + (int)realTemp ^ (int)(realTemp >>> 32);
		result = 31 * result + (int)imaginaryTemp ^ (int)(imaginaryTemp >>> 32);	
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return ((real == ((Complex)obj).getReal()) && (imaginary == ((Complex)obj).getImaginary())) ? true : false;
	}

	@Override
	public String toString() {
	    return (real + " + " + imaginary + "i");
	}

	@Override
	public int compareTo(Complex data) {
		if (real > (data.getReal())){
			return 1;
		}

		if ((imaginary == data.getImaginary()) && (real == data.getReal())){
			return 0;
		}

		return -1;			
	}
	
	public Complex add(Complex num1)
	{
		return new Complex(real + num1.real, imaginary + num1.imaginary);	
	}
	
	public Complex subtract(Complex num1)
	{
		return new Complex(real - num1.real, imaginary - num1.imaginary);
	}
	
	public Complex multiply(Complex num1)
	{
		return new Complex(real * num1.real - imaginary * num1.imaginary, 
							real * num1.imaginary + imaginary * num1.real);
	}

	public Complex divide(Complex num1){
		return new Complex(((real * num1.real + 
				imaginary * num1.imaginary)/
				(Math.pow(num1.real,2) + Math.pow(num1.imaginary,2))), 
				(-(real * num1.imaginary - imaginary * num1.real)/
				(Math.pow(num1.real,2) + Math.pow(num1.imaginary,2))));
	}
}