package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		m.creaGrafo(2015);
		m.init(6, 8, 2016, 30);
		m.run();
		System.out.println(m.getNMalGestiti());
	}

}
