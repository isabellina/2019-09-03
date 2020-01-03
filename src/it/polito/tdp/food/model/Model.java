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
	private List<String> listaFinale = new LinkedList<String>();
	private int pesoMassimo = -1;
	
	
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
	    List<String> listaVicini = new LinkedList<String>(Graphs.neighborListOf(this.grafo, s)); // neighbor chiedeva i direttamente connessi
	    for(String string: listaVicini) {
	    	MyEdge e = (MyEdge) this.grafo.getEdge(s, string);
	    	res+= string + " - " + e.getWeight() + "\n";
	    }
		return res;
	}
	
	private List<String> run(int n, int livelli, String sourceVertex, List<String> parziale, int pesoParziale) {
		if(livelli >= n) {
			if(this.pesoMassimo <= pesoParziale) {
				System.out.println("parziale alla fine: " + parziale);
				this.listaFinale = new LinkedList<String>(parziale);
				this.pesoMassimo = pesoParziale;
			}
			return this.listaFinale;
		}
		else {			
			for(Object o : Graphs.neighborListOf(this.grafo, sourceVertex)) {
				if(!parziale.contains((String) o)) {
					parziale.add((String) o);
					
					System.out.println("Peso da sommare: " + this.getWeight(sourceVertex, (String) o));
					
					this.run(n, livelli + 1, (String) o, parziale, pesoParziale + this.getWeight(sourceVertex, (String) o));
					
					parziale.remove(o);
				}
			}
		}
		return this.listaFinale;
	}
	

	
	public List<String> recursive(int n, String verticeP){
		List<String> l = new LinkedList<String>();
		l.add(verticeP);
		List<String> ret = this.run(n, 1, verticeP, l, 0);
		System.out.println("Mi arriva: " + ret);
		
		return ret;
	}
	
	public List<String> getListaFinale(){
		return this.listaFinale;
	}
	
	public int getPesoMassimo() {
		int peso = this.pesoMassimo;
		this.pesoMassimo = 0;
		return peso;
	}
	
	
}
