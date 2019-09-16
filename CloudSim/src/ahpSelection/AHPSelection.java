package ahpSelection;

import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

import ahp.Alternatives;
import ahp.TableCriterion;

public class AHPSelection extends PowerVmSelectionPolicy {

	@Override
	public Vm getVmToMigrate(PowerHost host) {

		List<PowerVm> migratableVms = getMigratableVms(host);

		if (migratableVms.isEmpty()) return null;
		
		System.out.println("Passei");
		
		return choiceVm(host, migratableVms, getCriterionAndSubCriterionWeights());
	}

	public double[] getCriterionAndSubCriterionWeights() {

		String labelsCriterion[] = { "MIPS", "Ram", "Tamanho" };
		double compArrayCriterion[] = { 7, 8, 1.0 / 5.0 };

		TableCriterion tableCriterion = new TableCriterion();
		return tableCriterion.Compute(labelsCriterion, compArrayCriterion).getWeights();
	}
	
	public Vm choiceVm(PowerHost host, List<PowerVm> migratableVms, double[] weightsCriterionAndSub) {

		Alternatives alternatives = new Alternatives();
		String[] labelsVms = new String[migratableVms.size()];
		double[][] compArrayVms = new double[weightsCriterionAndSub.length][(int) CombinatoricsUtils.binomialCoefficient(migratableVms.size(), 2)];
		int i;

		for (i = 0; i < migratableVms.size(); i++) {
			labelsVms[i] = "VM " + i;
		}

		for (i = 0; i < weightsCriterionAndSub.length; i++) {
			buildCompArrayAlternativeCriterion(host, migratableVms, compArrayVms, i);
		}

		double[] weightsFinalAlternative = alternatives.buildAndCompute(labelsVms, compArrayVms, weightsCriterionAndSub);
		
		return migratableVms.get(getIndexMaxPriority(weightsFinalAlternative));
	}

	public void buildCompArrayAlternativeCriterion(PowerHost host, List<PowerVm> migratableVms, double[][] compArrayVms, int i) {
		int x, y;
		int j = 0;
		
		if (i == 0) {
			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getMips(), migratableVms.get(y).getMips(), host.getTotalMips());
					j++;
				}
		}
		if (i == 1) {
			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getRam(), migratableVms.get(y).getRam(), host.getRam());
					j++;
				}
		}
		if (i == 2){
			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getSize(), migratableVms.get(y).getSize(), host.getStorage());
					j++;
				}
		}
	}
	
	public int classifier(double x, double y, double maxCapacityHost) {

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
	
	public int getIndexMaxPriority(double[] weightsFinalAlternative) {
		int index = 0;
		for (int i = 1; i < weightsFinalAlternative.length; i++)
			if (weightsFinalAlternative[i] > weightsFinalAlternative[index]) index = i;
		return index;
	}
}
