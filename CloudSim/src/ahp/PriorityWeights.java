package ahp;

public class PriorityWeights {
	
	private TableCriterion tableCriterion;
	private TableCriterion tableSubCriterion[];
	
	public double[] buildPriorityWeights(String labelsCriterion[], double[] compArrayCriterion, String labelsSubCriterion[][], 
			                             double compArraySubCriterion[][]) {
		
		/**
	     * Table Criterion
	     */
		
		this.tableCriterion = new TableCriterion();
		this.tableCriterion.Compute(labelsCriterion, compArrayCriterion);
		
		if (compArrayCriterion == null || labelsSubCriterion == null) return this.tableCriterion.getWeights();
			

		/**
	     * Array of subcriterion
	     */
	         
		this.tableSubCriterion = new TableCriterion[labelsSubCriterion.length];
	
		
		/**
	     * Subcriterions
	     */
		
		int i,j;
		for (i = 0; i < labelsSubCriterion.length; i++) {
			this.tableSubCriterion[i] = new TableCriterion();
			this.tableSubCriterion[i].Compute(labelsSubCriterion[i], compArraySubCriterion[i]);
		}
			
	    
	    /**
	     * Calculate final weights
	     */
		int count = 0;
		for (i = 0; i < labelsSubCriterion.length; i++)
			for (j = 0; j < labelsSubCriterion[i].length; j++) count++;
	    double weightsFinal[] = new double[count];
			
		i = 0;
		j = 0;
		for (double weightsCriterion: tableCriterion.getWeights()) {
			for (double weightsSubCriterion: tableSubCriterion[i].getWeights()) {
				weightsFinal[j] = weightsCriterion * weightsSubCriterion;
				j++;
			}
			i++;
		}
			
		return weightsFinal;
	}
}
