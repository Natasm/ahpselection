package SelectionPolicyTeste;

import java.util.List;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

public class SelectionPolicyTeste extends PowerVmSelectionPolicy{

	@Override
	public Vm getVmToMigrate(PowerHost host) {
		List<PowerVm> migratableVms = getMigratableVms(host);
		
		if (migratableVms.isEmpty()) return null;
		
		System.out.println("\nSelectionPolicyTeste -> VM escolhida: VM " + migratableVms.get(0).getId());
		
		return migratableVms.get(0);
	}

}
