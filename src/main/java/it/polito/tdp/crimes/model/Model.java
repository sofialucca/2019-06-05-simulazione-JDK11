package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import it.polito.tdp.crimes.db.EventsDao;
import it.polito.tdp.crimes.model.Evento.EventType;

public class Model {
	
	private Graph<District,DefaultWeightedEdge> grafo;
	private Map<Integer,District> idMap;
	private EventsDao dao;
	private PriorityQueue<Event> eventiGiorno;
	private PriorityQueue<Evento> queue;
	private int NMALGESTITI;
	private Map<District, Integer> mappaPoliziotti;
	
	
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
	

	public void init(int mese, int giorno, int anno, int n) {
		NMALGESTITI = 0;
		this.eventiGiorno = new PriorityQueue<>(dao.getEventiGiorno(giorno, mese, anno));
		this.queue = new PriorityQueue<>();
		District d = dao.getMiglioreDistretto(idMap, anno);
		mappaPoliziotti = new HashMap<>();
		for(int i = 0; i<n; i++) {
			queue.add(new Evento(LocalDateTime.of(anno, mese, giorno, 0, 0, 0), d, EventType.LIBERO, null));
		}
		mappaPoliziotti.put(d,n);
	}
	
	public void run() {
		Evento e;
		while((e = queue.poll()) !=  null && !eventiGiorno.isEmpty()) {
			LocalDateTime T = e.getT();
			Event event = e.getE();
			District d = e.getD();
			System.out.println(e);
			switch(e.getTipo()) {
				case LIBERO:
					boolean check = false;
					List<Event> eventiScartati = new ArrayList<>();					
					do {
						event = this.eventiGiorno.poll();
						//System.out.println(event);
						District nuovoDistrict = idMap.get(event.getDistrict_id());
						if(event.getReported_date().isAfter(T)) {
							T = event.getReported_date();
						}
						if(nuovoDistrict.equals(d)) {
							queue.add(new Evento(T, nuovoDistrict, EventType.GESTIONE, event));
							mappaPoliziotti.put(d, mappaPoliziotti.get(d) - 1);
							check = true;
							break;							
						}
						for(DistrettoNumero dn: nuovoDistrict.getAdiacenti()) {
							Integer nPol = mappaPoliziotti.get(dn.getD());
							double distanza = dn.getN();
							if(!dn.getD().equals(d) && nPol != null && nPol > 0  ){
								eventiScartati.add(event);
								check = false;
								break;
							}else if(dn.getD().equals(d)) {
								long tempoPercorso = (long) (distanza *60/ 60);
								T.plusMinutes(tempoPercorso);
								queue.add(new Evento(T, nuovoDistrict, EventType.GESTIONE, event));
								mappaPoliziotti.put(d, mappaPoliziotti.get(d) - 1);
								check = true;
								break;
							}
						}						
					}while(!check && !this.eventiGiorno.isEmpty());

					this.eventiGiorno.addAll(eventiScartati);
					break;
				case GESTIONE:
					if(T.isAfter(event.getReported_date().plusMinutes(15))) {
						this.NMALGESTITI++;
					}
					String categoria = event.getOffense_category_id();
					int tempo = 2 ;
					if(categoria.equals("all-other-crimes")) {
						double prob = Math.random();
						if(prob <= 0.5) {
							tempo = 1;
						}
					}
					
					T = T.plusHours(tempo);
					queue.add(new Evento(T, d, EventType.LIBERO, null));
					Integer nPol = mappaPoliziotti.get(d);
					if(nPol == null) {
						nPol = 0;
					}
					mappaPoliziotti.put(d, nPol + 1);
					break;
			}
		}
	}
	
	public int getNMalGestiti() {
		return this.NMALGESTITI;
	}
}
