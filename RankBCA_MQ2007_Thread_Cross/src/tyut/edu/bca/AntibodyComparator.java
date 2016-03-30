package tyut.edu.bca;

import java.util.Comparator;

public class AntibodyComparator implements Comparator<Antibody> {

	@Override
	public int compare(Antibody o1, Antibody o2) {
		return o1.compareTo(o2);

	}

}
