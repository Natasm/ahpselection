package ahpSelection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

import ahp.Alternatives;
import ahp.PriorityWeights;

public class AHPSelection extends PowerVmSelectionPolicy {

	@Override
	public Vm getVmToMigrate(PowerHost host) {
		
		//System.out.print("\n\n------------------------- Nova Seleção ---------------------------------\n\n");
		
		List<PowerVm> migratableVms = getMigratableVms(host);
        
		if (migratableVms.isEmpty()) return null;

		Vm vmChoice = choiceVm(host, migratableVms, getCriterionAndSubCriterionWeights());

		return vmChoice;
	}

	public ArrayList<Double> getCriterionAndSubCriterionWeights() {
		
	    String labelsCriterion[] = { "Desempenho "};
		double compArrayCriterion[] = {  };

		String labelsSubCriterion[][] = { { "MIPS" , "RAM" } };
		double compArraySubCriterion[][] = { { 9.0 } };

		PriorityWeights priorityWeights = new PriorityWeights();
		return priorityWeights.buildPriorityWeights(labelsCriterion, compArrayCriterion, labelsSubCriterion,
				compArraySubCriterion);
	}

	public Vm choiceVm(PowerHost host, List<PowerVm> migratableVms, ArrayList<Double> weightsCriterionAndSub) {

		Alternatives alternatives = new Alternatives();
		String[] labelsVms = new String[migratableVms.size()];
		double[][] compArrayVms = new double[weightsCriterionAndSub.size()][(int) CombinatoricsUtils.binomialCoefficient(migratableVms.size(), 2)];
		int i;

		for (i = 0; i < migratableVms.size(); i++) {
			labelsVms[i] = "VM " + i;
		}
		
		for (i = 0; i < weightsCriterionAndSub.size(); i++) {
			buildCompArrayAlternativeCriterion(host, migratableVms, compArrayVms, i);
		}
		
		ArrayList<Double> weightsFinalAlternative = alternatives.buildAndCompute(labelsVms, compArrayVms, weightsCriterionAndSub);
		
		return migratableVms.get(getIndexMaxPriority(weightsFinalAlternative));
	}

	public void buildCompArrayAlternativeCriterion(PowerHost host, List<PowerVm> migratableVms, double[][] compArrayVms, int i) {
		int x, y;
		int j = 0;

		if (i == 0) {

			// PRINT - updated
			//System.out.println("\n\nCritério de MIPS" + "\n" + "Host: " + host.getTotalMips() + "\n");

			// Printar a quantidade de MIPS de x e y - updated
			/*for (x = 0; x < migratableVms.size(); x++) {
				System.out.println("VM " + x + " : " + migratableVms.get(x).getMips());
			}*/

			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getMips(), 
							                        migratableVms.get(y).getMips(),
							                        host.getTotalMips());
					j++;
				}
		}
		if (i == 1) {

			// PRINT - updated
			//System.out.println("\n\nCritério de Energia" + "\n" + "Host: " + host.getPower() + "\n");

			// Printar a quantidade de MIPS de x e y - updated
			/*for (x = 0; x < migratableVms.size(); x++) {
				System.out.println("VM " + x + " : " + migratableVms.get(x).getCurrentRequestedMaxMips() / host.getPower());
			}*/
			

			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getRam(),
							                        migratableVms.get(y).getRam(),
							                        host.getRam());
					j++;
				}
		}
		if (i == 2) {

			// PRINT - updated
			//System.out.println("\n\nCritério de Energia" + "\n" + "Host: " + host.getPower() + "\n");

			// Printar a quantidade de MIPS de x e y - updated
			/*for (x = 0; x < migratableVms.size(); x++) {
				System.out.println("VM " + x + " : " + migratableVms.get(x).getCurrentRequestedMaxMips() / host.getPower());
			}*/
			

			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getUtilizationMean(),
							                        migratableVms.get(y).getUtilizationMean(),
							                        host.getTotalMips());
					j++;
				}
		}
		if (i == 3) {

			// PRINT - updated
			//System.out.println("\n\nCritério de Energia" + "\n" + "Host: " + host.getPower() + "\n");

			// Printar a quantidade de MIPS de x e y - updated
			/*for (x = 0; x < migratableVms.size(); x++) {
				System.out.println("VM " + x + " : " + migratableVms.get(x).getCurrentRequestedMaxMips() / host.getPower());
			}*/
			

			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getUtilizationVariance(),
							                        migratableVms.get(y).getUtilizationVariance(),
							                        host.getTotalMips());
					j++;
				}
		}
	}

	public double classifier(double x, double y, double maxCapacityHost) {
		double classifier = classifierMin(x, y, maxCapacityHost);
		if (x < y)
			return 1 / classifier;
		else
			return classifier;
	}

	public double classifierMin(double x, double y, double maxCapacityHost) {

		double res = x + y + maxCapacityHost;
		int i;

		if (res > maxCapacityHost) {
			for (i = 1; i <= 9; i++)
				if (res < (maxCapacityHost + (i * maxCapacityHost / 8)))
					return (i + 1);
		} else if (res < maxCapacityHost) {
			for (i = 1; i <= 9; i++)
				if (res < (i * maxCapacityHost / 8))
					return 1 / (10 - i);
		}
		return 1;
	}

	public int getIndexMaxPriority(ArrayList<Double> weightsFinalAlternative) {
		int index = 0;
		for (int i = 1; i < weightsFinalAlternative.size(); i++)
			if (weightsFinalAlternative.get(i) > weightsFinalAlternative.get(index))
				index = i;
		return index;
	}
}
