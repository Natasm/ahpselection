package ahp;

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
    protected TableCriterion tableCriterion[];
    
    /**
     * Array of criteria tables
     */
    protected double[] weightsFinal;
    
    public Alternatives(int nrAlternatives, int nrCriterion, double[] weightsFinal){
    	this.nrAlternatives = nrAlternatives;
    	this.nrCriterion = nrCriterion;
    	this.tableCriterion = new TableCriterion[nrCriterion];
    	this.weightsFinal = weightsFinal;
    }
    
    public void buildAndCompute(String[] labelsAlternatives, double[][] compArray) {
    	int i,j;
    	
    	for (i = 0; i < this.nrCriterion; i++) {
    		this.tableCriterion[i] = new TableCriterion();
    		System.out.println("------------------------------------------------");
    		this.tableCriterion[i].Compute(this.nrAlternatives, labelsAlternatives, compArray[i]);
    	}
    	
    	double[] priorityWeightsFinal = new double[this.nrAlternatives];
    	
    	System.out.println("\nFinal priority");
    	
    	for (i = 0; i < this.nrAlternatives; i++) {
    		double soma = 0;
    		for (j = 0; j < this.nrCriterion; j++) {
    			soma = soma + (this.tableCriterion[j].getWeights()[i] * this.weightsFinal[j]);
    		}
    		priorityWeightsFinal[i] = soma;

    		System.out.println(labelsAlternatives[i] + " : " + priorityWeightsFinal[i] * 100);
    	}
    }    
}
