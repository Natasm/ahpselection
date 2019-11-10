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
		
		List<PowerVm> migratableVms = getMigratableVms(host);
        
		if (migratableVms.isEmpty() || migratableVms.size() <= 1) return null;

		if (!isServerOverLoaded(host)) return null;
		
		Vm vmChoice = choiceVm(host, migratableVms, getCriterionAndSubCriterionWeights());

		return vmChoice;
	}
	
	private boolean isServerOverLoaded(PowerHost host) {
		boolean is_overloaded = false;
		
		if ((host.getUtilizationOfCpu()) >= 0.8) is_overloaded = true;
		
		return is_overloaded;
	}

	public ArrayList<Double> getCriterionAndSubCriterionWeights() {
		
	    /*String labelsCriterion[] = { "Desempenho" , "Utilização" };
		double compArrayCriterion[] = { 1.0 / 4.0 };

		String labelsSubCriterion[][] = { { "MIPS ", "RAM" } , { "Utilização da CPU", "Utilização Máxima da CPU" } };
		double compArraySubCriterion[][] = { { 2.0 } , { 1.0 / 3.0 } };*/

		String labelsCriterion[] = { "MIPS" ,"Utilização da Cpu" };
		double compArrayCriterion[] = { 1.0 / 4.0 };
		
		PriorityWeights priorityWeights = new PriorityWeights();
		return priorityWeights.buildPriorityWeights(labelsCriterion, compArrayCriterion, null,
				null);
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
			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifierMin(migratableVms.get(x).getMips(), 
							                        migratableVms.get(y).getMips(),
							                        host.getMaxAvailableMips());
					j++;
				}
		}
		if (i == 1) 
		{
			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getCurrentRequestedMaxMips(),
							                        migratableVms.get(y).getCurrentRequestedMaxMips(),
							                        host.getMaxAvailableMips());
					j++;
				}
		}
	}
	
	public double mean(List<Double> listDouble) {
		if (listDouble.isEmpty()) return 0;
		System.out.println("Mean => " + listDouble.size());
		double sum = 0;
		for (double d: listDouble) sum += d;
		return sum / listDouble.size();
	}

	public double classifier(double x, double y, double maxCapacityHost) {
		return 1.0 / classifierMin(x, y, maxCapacityHost);
	}

	public double classifierMin(double x, double y, double maxCapacityHost) {

		double res = x - y + maxCapacityHost;
		int i;

		if (res > maxCapacityHost) {
			for (i = 1; i <= 8; i++)
				if (res < (maxCapacityHost + (i * maxCapacityHost / 8.0 )))
					return (i + 1);
		} else if (res < maxCapacityHost) {
			for (i = 1; i <= 8; i++)
				if (res < (i * maxCapacityHost / 8.0 ))
					return 1.0 / (10.0 - i);
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
