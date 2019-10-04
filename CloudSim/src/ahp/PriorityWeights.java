package ahp;

import java.util.ArrayList;

public class PriorityWeights {
	
	private TableCriterion tableCriterion;
	private ArrayList<TableCriterion> tableSubCriterion;
	
	public ArrayList<Double> buildPriorityWeights(String labelsCriterion[], double[] compArrayCriterion, String labelsSubCriterion[][], 
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
	         
		this.tableSubCriterion = new ArrayList<TableCriterion>();
	
		
		/**
	     * Subcriterions
	     */
		
		int i;
		for (i = 0; i < labelsSubCriterion.length; i++) {
			TableCriterion tableCriterionObj = new TableCriterion();
			tableCriterionObj.Compute(labelsSubCriterion[i], compArraySubCriterion[i]);
			tableSubCriterion.add(tableCriterionObj);
		}
			
	    
	    /**
	     * Calculate final weights
	     */
		ArrayList<Double> weightsFinal = new ArrayList<Double>();
			
		i = 0;
		for (double weightsCriterion: tableCriterion.getWeights()) {
			
			if (i < tableSubCriterion.size()) {
			
				for (double weightsSubCriterion: tableSubCriterion.get(i).getWeights()) {
				weightsFinal.add(weightsCriterion * weightsSubCriterion);
			    }
			}
			else weightsFinal.add(weightsCriterion);
			i++;
		}
			
		return weightsFinal;
	}
}
