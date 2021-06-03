package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.District;
import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public void setDistrict(Map<Integer, District> idMap, int anno) {
		String sql = "SELECT district_id, AVG(geo_lon) AS longitudine, AVG(geo_lat) AS latitudine "
				+ "FROM `events` "
				+ "WHERE YEAR(reported_date) = ? "
				+ "GROUP BY district_id";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				District d = new District(res.getInt("district_id"), res.getDouble("latitudine"), res.getDouble("longitudine"));
				if(!idMap.containsKey(d.getId())) {
					idMap.put(d.getId(), d);
				}				
			}
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public List<String> getAnni(){
		String sql = "SELECT distinct YEAR(reported_date) AS anno "
				+ "FROM `events` "
				+ "ORDER BY anno";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				int	s = res.getInt("anno");
				result.add(String.valueOf(s));
			}
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return result;
	}
	
	public List<String> getMesi(){
		String sql = "SELECT distinct Month(reported_date) AS mese "
				+ "FROM `events` "
				+ "ORDER BY mese";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				int	s = res.getInt("mese");
				result.add(String.valueOf(s));
			}
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return result;
	}

	public List<String> getGiorni(int anno, int mese){
		String sql = "SELECT distinct DAY(reported_date) AS giorno "
				+ "FROM `events` "
				+ "WHERE MONTH(reported_date) = ? AND YEAR(reported_date) = ? "
				+ "ORDER BY giorno";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, mese);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				int	s = res.getInt("giorno");
				result.add(String.valueOf(s));
			}
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return result;		
	}
}
