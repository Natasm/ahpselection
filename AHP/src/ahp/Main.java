package ahp;

public class Main {

	public static void main(String argv[]) {
		
		PriorityWeights pw = new PriorityWeights();
		
		Alternatives alternatives = new Alternatives(6, 12, pw.buildPriorityWeights());
		
		String[] labelsAlternatives = { "Novo escritorio", "Sistema ERP", "Escritorio na China",
				                        "Produto Internacional", "Terceirizacao TI", 
				                        "Marketing Local"};
		
		double[][] compArray = {
				                {5, 3, 1.0 / 3.0, 9, 7, 1.0 / 5.0, 1.0/ 7.0,1, 1.0/3.0, 1.0 / 3.0, 7, 3, 5, 5, 1.0 / 3.0},
				                
				                {3, 1.0 / 9.0, 1.0 / 5.0, 5, 3, 1.0/9.0, 1.0/1.7,1,1.0/3.0, 3,7,7,9,7,1.0/3.0},
				                
				                {7, 1.0/ 3.0, 1.0/ 3.0, 5, 3, 1.0/9.0, 1.0/7.0,3, 1.0/3.0, 1,7,7,7,9,1.0/5.0},
				                
				                {1.0/3.0, 1.0/7.0, 1.0/9.0, 1.0/3.0, 1.0/3.0, 1.0/9.0, 1.0/9.0, 1.0/3.0, 1.0/3.0, 1.0/3.0, 7, 5, 7, 5, 1.0/3.0},
				                
				                {1, 1.0/7.0, 1.0/9.0, 1.0/5.0, 1.0/3.0, 1.0/7.0, 1.0/9.0, 1.0/3.0, 1.0/5.0, 1.0/3.0, 7,5, 9, 5, 1.0/3.0},
				                
				                {1.0/3.0, 1.0/5.0, 1.0/7.0, 1.0/3.0, 1.0/3.0, 1.0/5.0, 1.0/7.0 ,1,1.0/3.0 ,1.0/3.0,5,3,5,7,1.0/3.0},
				                
				                {3, 1.0/9.0, 1.0/7.0, 5,5, 1.0/9.0, 1.0/9, 1.0/3.0 ,3,1,9,9,9,9,3},
				                
				                {1.0/5.0, 3,5,1,7,7,7,1,7,1, 1.0/7.0,1,1.0/7.0,1.0/3.0,7},
				                
				                {1.0/3.0, 1.0/7.0, 1.0/5.0, 3, 1.0/7.0, 1.0/9.0, 1.0/5.0, 5, 1.0/7.0, 3, 7 ,1,7,1.0/3.0,1.0/9.0},
				                
				                {5,7,3,5,1,5,3,3, 1.0/7.0, 1.0/3.0 , 1.0/3.0, 1.0/9.0 ,5, 1.0/7.0 ,1.0/9.0},
				                
				                {1.0/3.0, 1.0/5.0, 1.0/7.0,3,1, 1.0/7.0, 1.0/9.0,3,3, 1.0/3.0 ,5,7,7,7, 1.0/3.0},
				                
				                {9,9,9,9,3, 1.0/3.0, 1.0/3.0, 1.0/5.0, 1.0/9.0, 3, 1 , 1.0/9.0, 1.0/3.0, 1.0/9.0, 1.0/9.0}
				                				 
				               };
		
		alternatives.buildAndCompute(labelsAlternatives, compArray);
	}
}
