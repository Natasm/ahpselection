package ahp;

import java.util.ArrayList;

public class TableCriterion {
	
	/**
     * AHP criterion
     */
	private AHP ahpCriterion;
	
	public AHP Compute(String[] labelsCriterion, double compArray[]) {
		this.ahpCriterion = new AHP(labelsCriterion);
		
		double compArrayCriterion[] = this.ahpCriterion.getPairwiseComparisonArray();
		
		int i = 0;
		for(double number: compArray) {
			compArrayCriterion[i] = number;
			i++;
		}
		
		this.ahpCriterion.setPairwiseComparisonArray(compArrayCriterion);

		this.ahpCriterion = this.ahpCriterion.Calculate(this.ahpCriterion, labelsCriterion);
		
		return ahpCriterion;
	}
	
	public ArrayList<Double> getWeights() {
		return this.ahpCriterion.getWeights();
	}
}
