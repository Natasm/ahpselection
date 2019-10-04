package ahp;

import java.util.ArrayList;

public class Alternatives {
	
	/**
     * Number of alternatives
     */
    protected int nrAlternatives;
    
    /**
     * Number of criterion
     */
    protected int nrCriterion;
    
    /**
     * Array of criteria tables
     */
    protected ArrayList<TableCriterion> tableCriterion;
    
    /**
     * Array of criteria tables
     */
    protected ArrayList<Double> weightsFinal;
    
    public ArrayList<Double> buildAndCompute(String[] labelsAlternatives, double[][] compArray, ArrayList<Double> weightsFinal) {
    	
    	this.nrAlternatives = labelsAlternatives.length;
    	this.nrCriterion = weightsFinal.size();
    	this.tableCriterion = new ArrayList<TableCriterion>();
    	this.weightsFinal = weightsFinal;
    	
    	int i,j;
    	
    	for (i = 0; i < this.nrCriterion; i++) {
    		System.out.println("------------------------------------------------");
    		TableCriterion tableCriterionObj = new TableCriterion();
    		tableCriterionObj.Compute(labelsAlternatives, compArray[i]);
    		tableCriterion.add(tableCriterionObj);
    	}
    	
    	ArrayList<Double> priorityWeightsFinal = new ArrayList<Double>();
    	
    	System.out.println("\nFinal priority");
    	
    	for (i = 0; i < this.nrAlternatives; i++) {
    		double soma = 0;
    		for (j = 0; j < this.nrCriterion; j++) {
    			soma = soma + (tableCriterion.get(j).getWeights().get(i) * this.weightsFinal.get(j));
    		}
    		priorityWeightsFinal.add(soma);

    		System.out.println(labelsAlternatives[i] + " : " + priorityWeightsFinal.get(i) * 100);
    	}
    	
    	return priorityWeightsFinal;
    }    
}
