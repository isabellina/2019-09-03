package it.polito.tdp.food.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao foodDao;
	private Graph grafo;
	
	
	public Model  () {
		this.foodDao = new FoodDao();
	}
	
	public List<String> getPorzioni(int c){
		System.out.println(this.foodDao.tipiDiporzioni(c));
		List<String> porzioni = new LinkedList<String>(this.foodDao.tipiDiporzioni(c));
		return porzioni;
	}
	
	
	public int getWeight(String s1, String s2) {
		return foodDao.getPeso(s1, s2);
	}
	
	public void creaGrafo(int c) {
		this.grafo = new SimpleWeightedGraph<String, MyEdge>(MyEdge.class);
		
		for(String s: foodDao.tipiDiporzioni(c)) {
			this.grafo.addVertex(s);
		}
		
		for(Object a: this.grafo.vertexSet()) {
			for(Object b: this.grafo.vertexSet()) {
				if(!a.equals(b)) {
					String aa = (String) a;
					String bb = (String) b;
					
					int peso = this.getWeight(aa, bb);
					if(peso > 0) {
						this.grafo.addEdge(aa,bb);
						this.grafo.setEdgeWeight(aa, bb, peso);
						System.out.println(aa + " to " + bb + "peso:    " + peso);
					}
				}
			}
		}
		System.out.println(this.grafo.toString());
	}
	
	public String getConnessioni(String s) {
		String res = "";
		System.out.println(this.grafo);
	    List<String> listaVicini = new LinkedList<String>(Graphs.neighborListOf(this.grafo, s)); //successor or neighbor???
	    for(String string: listaVicini) {
	    	MyEdge e = (MyEdge) this.grafo.getEdge(s, string);
	    	res+= string + " - " + e.getWeight() + "\n";
	    }
		return res;
	}
	
	private void run(int n, int livelli, String sourceVertex, List<String> parziale) {
		if(livelli ==n) {
			return ;
		}
		else {
			for(Object o:  Graphs.successorListOf(this.grafo, sourceVertex)){
				
				this.run(n, livelli+1, (String) o, parziale);
			}
		}
	}
	
	public List<String> recursive(int n, String verticeP){
		List<String> l = new LinkedList<String>();
		this.run(n, 1, verticeP, l);
		return l;
	}
}
