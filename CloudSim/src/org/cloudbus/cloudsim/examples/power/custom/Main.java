package org.cloudbus.cloudsim.examples.power.custom;

import java.io.IOException;

/**
 * A simulation of a heterogeneous power aware data center that only applied DVFS, but no dynamic
 * optimization of the VM allocation. The adjustment of the hosts' power consumption according to
 * their CPU utilization is happening in the PowerDatacenter class.
 * 
 * This example uses a real PlanetLab workload: 20110303.
 * 
 * The remaining configuration parameters are in the Constants and PlanetLabConstants classes.
 * 
 * If you are using any algorithms, policies or workload included in the power package please cite
 * the following paper:
 * 
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 * 
 * @author Anton Beloglazov
 * @since Jan 5, 2012
 */
public class Main {

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		boolean enableOutput = true;
		boolean outputToFile = false;
		String inputFile = "testconfig\\all";
		String outputFolder = "output";
		String workload = "googlecluster.txt"; // PlanetLab workload
		String vmAllocationPolicy = "guazzone"; // DVFS policy without VM migrations
		String vmSelectionPolicy = "ahpselection";
		String parameter = "0.8";

		new CustomRunner(
				enableOutput,
				outputToFile,
				inputFile,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
		 
	}

}
