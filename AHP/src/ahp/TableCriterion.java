package ahp;

public class TableCriterion {
	
	/**
     * AHP criterion
     */
	private AHP ahpCriterion;
	
	public AHP Compute(int nrVx, String[] labelsCriterion, double compArray[]) {
		this.ahpCriterion = new AHP(nrVx);
		
		double compArrayCriterion[] = this.ahpCriterion.getPairwiseComparisonArray();
		
		int i = 0;
		for(double number: compArray) {
			compArrayCriterion[i] = number;
			i++;
		}
		
		this.ahpCriterion.setPairwiseComparisonArray(compArrayCriterion);

		this.ahpCriterion = this.ahpCriterion.Calculate(this.ahpCriterion, nrVx, labelsCriterion);
		
		return ahpCriterion;
	}
	
	public double[] getWeights() {
		if (ahpCriterion == null) return null;
		return ahpCriterion.getWeights();
	}
}
