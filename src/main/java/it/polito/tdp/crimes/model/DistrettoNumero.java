package it.polito.tdp.crimes.model;

public class DistrettoNumero implements Comparable<DistrettoNumero>{

	private District d;
	private Double n;
	
	public DistrettoNumero(District d, double n) {
		super();
		this.d = d;
		this.n = n;
	}

	public District getD() {
		return d;
	}

	public void setD(District d) {
		this.d = d;
	}

	public Double getN() {
		return n;
	}

	public void setN(double n) {
		this.n = n;
	}

	@Override
	public int compareTo(DistrettoNumero o) {
		// TODO Auto-generated method stub
		return this.n.compareTo(o.n);
	}

	@Override
	public String toString() {
		return d + " - " + String.format("%.5g",n);
	}
	
	
}
