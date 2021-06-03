package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

public class District implements Comparable<District>{

	private Integer id;
	private LatLng centro;
	private List<DistrettoNumero> adiacenti;
	
	public District(int id, double lat, double longi) {
		this.id = id;
		this.centro= new LatLng(lat, longi);
		adiacenti = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LatLng getCentro() {
		return centro;
	}

	public void setCentro(LatLng centro) {
		this.centro = centro;
	}
	
	public void setNuovoAdiacente(DistrettoNumero nuovo) {
		adiacenti.add(nuovo);
		Collections.sort(adiacenti);
	}

	public List<DistrettoNumero> getAdiacenti() {
		return adiacenti;
	}

	@Override
	public String toString() {
		return "" + id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		District other = (District) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int compareTo(District o) {
		// TODO Auto-generated method stub
		return this.id.compareTo(o.id);
	}
	
}
