package ahp;

public class PriorityWeights {
	
	public double[] buildPriorityWeights() {
		
		/**
	     * Table Criterion
	     */
		
		TableCriterion tableCriterion = new TableCriterion();
			
		String labelsCriterion[] = { "Comprometimento ", "Financeiro      ", "Estratégicos    ", "Outros critérios" };
		double compArrayCriterion[] = {1.0 / 5.0, 1.0 / 9.0 ,1 ,1 ,5 ,5};
			
		tableCriterion.Compute(4, labelsCriterion, compArrayCriterion);
			

		/**
	     * Array of subcriterion
	     */
	         
		TableCriterion tableSubCriterion[] = new TableCriterion[4];
	
		
		/**
	     * Subcriterion 0
	     */
	   
		tableSubCriterion[0] = new TableCriterion();

		String labelsSubCriterion1[] = { "Comprometimento do Time ", "Comprometimento da Organização",
					"Comprometimento do Gerente do Projeto" };
	    double compArray1[] = {3.0 , 1.0 / 5.0, 1.0 / 9.0};
			
	    tableSubCriterion[0].Compute(3, labelsSubCriterion1, compArray1);
	    
	    /**
	     * Subcriterion 1
	     */

	    tableSubCriterion[1] = new TableCriterion();

		String labelsSubCriterion2[] = { "Retorno de Investimento", "Lucro", "Valor Presente Líquido" };
		double compArray2[] = {1.0 / 5.0, 1.0 / 5.0, 1.0};
			
		tableSubCriterion[1].Compute(3, labelsSubCriterion2, compArray2);

		/**
	     * Subcriterion 2
	     */

	    tableSubCriterion[2] = new TableCriterion();
			
		String labelsSubCriterion3[] = { "Competir em Mercados Internacionais", "Processos Internos", "Reputação" };
		double compArray3[] = {7.0, 3.0, 1.0 / 5.0};

		tableSubCriterion[2].Compute(3, labelsSubCriterion3, compArray3);

		/**
	     * Subcriterion 3
	     */

	    tableSubCriterion[3] = new TableCriterion();

	    String labelsSubCriterion4[] = { "Reduz o Risco para a Organização", "Urgência", "Conhecimento Técnico Interno"};
	    double compArray4[] = {5.0, 1.0 / 3.0, 1.0 / 7.0};
			
	    tableSubCriterion[3].Compute(3, labelsSubCriterion4, compArray4);
			
	    
	    /**
	     * Calculate final weights
	     */
			
	    double weightsFinal[] = new double[12];
			
		int i = 0, j = 0;
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
