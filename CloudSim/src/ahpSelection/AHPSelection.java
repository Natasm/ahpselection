package ahpSelection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.util.MathUtil;

import ahp.Alternatives;
import ahp.PriorityWeights;

public class AHPSelection extends PowerVmSelectionPolicy {
	
	private Map<Integer, Integer> servers = new HashMap<>();

	@Override
	public Vm getVmToMigrate(PowerHost host) {
		
		List<PowerVm> migratableVms = getMigratableVms(host);
        
		if (migratableVms.isEmpty() || migratableVms.size() <= 1) return null;
		
		if (!isServerOverloaded(host)) return null;
		
		Vm vmChoice = choiceVm(host, migratableVms, getCriterionAndSubCriterionWeights());

		return vmChoice;
	}
	
	private boolean isServerOverloaded(PowerHost host) {
		if (servers.containsKey(host.getId())) {
			if (servers.get(host.getId()) == 3) {
				servers.put(host.getId(), 1);
				return true;
			}
			else if (host.getPreviousUtilizationOfCpu() >= 0.8) 
				servers.put(host.getId(), servers.get(host.getId()) + 1);  
		} else if (host.getPreviousUtilizationOfCpu() >= 0.8) servers.put(host.getId(), 1) ;
		return false;
	}

	public ArrayList<Double> getCriterionAndSubCriterionWeights() {
		
		//Antes 1.0
	    String labelsCriterion[] = { "Utilizacaoo", "Custo de Migracaoo"};
		double compArrayCriterion[] = { 7.0 };

		String labelsSubCriterion[][] = { { "Utilizacao CPU", "Utilizacaoo Media" } , { "RAM" } };
		double compArraySubCriterion[][] = { { 5.0 } , { } };

		PriorityWeights priorityWeights = new PriorityWeights();
		return priorityWeights.buildPriorityWeights(labelsCriterion, compArrayCriterion, labelsSubCriterion,
				compArraySubCriterion);
	}

	public Vm choiceVm(PowerHost host, List<PowerVm> vmsCandidates, ArrayList<Double> weightsCriterionAndSub) {
		
		Alternatives alternatives = new Alternatives();
		String[] labelsVms = new String[vmsCandidates.size()];
		double[][] compArrayVms = new double[weightsCriterionAndSub.size()][(int) CombinatoricsUtils.binomialCoefficient(vmsCandidates.size(), 2)];
		int i;

		for (i = 0; i < vmsCandidates.size(); i++) {
			labelsVms[i] = "VM " + vmsCandidates.get(i);
		}
		
		for (i = 0; i < weightsCriterionAndSub.size(); i++) {
			buildCompArrayAlternativeCriterion(host, vmsCandidates, compArrayVms, i);
		}
		
		ArrayList<Double> weightsFinalAlternative = alternatives.buildAndCompute(labelsVms, compArrayVms, weightsCriterionAndSub);
		
		return vmsCandidates.get(getIndexMaxPriority(weightsFinalAlternative));
	}

	public void buildCompArrayAlternativeCriterion(PowerHost host, List<PowerVm> migratableVms, double[][] compArrayVms, int i) {
		int x = 0, y = 0;
		int j = 0;

		if (i == 1) {
			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getUtilizationMean(), 
							                        migratableVms.get(y).getUtilizationMean(),
							                        host.getTotalMips());
					j++;
				}
		}
		if (i == 2) {
			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					compArrayVms[i][j] = classifier(migratableVms.get(x).getRam(), 
							                           migratableVms.get(y).getRam(),
							                           host.getRam());
					j++;
				}
		}
		if (i == 0) {
			for (x = 0; x < migratableVms.size(); x++)
				for (y = x + 1; y < migratableVms.size(); y++) {
					double timeCurrent = CloudSim.clock();
					
					double vmX = migratableVms.get(x).getTotalUtilizationOfCpu(timeCurrent);
					double vmY = migratableVms.get(y).getTotalUtilizationOfCpu(timeCurrent);
					
					compArrayVms[i][j] = classifier(vmX, vmY, 1);
					j++;
				}
		}
	}
	
	public double classifier(double x, double y, double maxCapacityHost) {
		return classifierMin(x, y, maxCapacityHost);
	}

	public double classifierMin(double x, double y, double maxCapacityHost) {

		double res = x - y + maxCapacityHost;
		int i;

		if (res > maxCapacityHost) {
			for (i = 1; i <= 8; i++)
				if (res < (maxCapacityHost + (i * maxCapacityHost / 8)))
					return (i + 1);
		} else if (res < maxCapacityHost) {
			for (i = 1; i <= 8; i++)
				if (res < (i * maxCapacityHost / 8))
					return 1.0 / (10 - i);
		}
		return 1.0;
	}

	public int getIndexMaxPriority(ArrayList<Double> weightsFinalAlternative) {
		int index = 0;
		for (int i = 1; i < weightsFinalAlternative.size(); i++)
			if (weightsFinalAlternative.get(i) > weightsFinalAlternative.get(index))
				index = i;
		return index;
	}
}