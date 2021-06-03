package it.polito.tdp.crimes.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private Graph<District,DefaultWeightedEdge> grafo;
	private Map<Integer,District> idMap;
	private EventsDao dao;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public void creaGrafo(int anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		dao.setDistrict(idMap, anno);
		
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(District d1: grafo.vertexSet()) {
			for(District d2: grafo.vertexSet()) {
				DefaultWeightedEdge e = grafo.getEdge(d2, d1);
				if (e == null && !d1.equals(d2)) {
					double peso = LatLngTool.distance(d1.getCentro(), d2.getCentro(), LengthUnit.KILOMETER);
					Graphs.addEdge(grafo, d1, d2, peso);
					d1.setNuovoAdiacente(new DistrettoNumero(d2,peso));
					d2.setNuovoAdiacente(new DistrettoNumero(d1,peso));					
				}
			}
		}
		
	}
	
	public Map<District, List<DistrettoNumero>> getAdiacenze(){
		Map<District, List<DistrettoNumero>> result = new TreeMap<>();
		for(District d : grafo.vertexSet()) {
			result.put(d, d.getAdiacenti());
		}
		return result;
	}
	
	public List<String> getAnni(){
		return dao.getAnni();
	}
	
	public List<String> getMesi(){
		return dao.getMesi();
	}
	
	public List<String> getGiorni(int mese, int anno){
		return dao.getGiorni(anno, mese);
	}
	
}
