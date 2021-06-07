package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{

	enum EventType{
		GESTIONE,
		LIBERO
	}
	
	private LocalDateTime T;
	private District d;
	private EventType tipo;
	private Event e;
	
	public Evento(LocalDateTime t, District d, EventType tipo, Event e) {
		super();
		T = t;
		this.d = d;
		this.tipo = tipo;
		this.e = e;
	}

	
	public Event getE() {
		return e;
	}


	public void setE(Event e) {
		this.e = e;
	}


	public LocalDateTime getT() {
		return T;
	}

	public void setT(LocalDateTime t) {
		T = t;
	}

	public District getD() {
		return d;
	}

	public void setD(District d) {
		this.d = d;
	}

	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Evento o) {
		
		return this.T.compareTo(o.T);
	}


	@Override
	public String toString() {
		return "Evento [T=" + T + ", d=" + d + ", tipo=" + tipo + ", e=" + e + "]";
	}
	
}
