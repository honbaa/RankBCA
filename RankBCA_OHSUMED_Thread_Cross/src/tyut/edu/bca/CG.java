package tyut.edu.bca;

public class CG {
	public static double cg[];

	public static double[] getCG(double[] G) {
			cg = new double[G.length];
		// ����ԭʼ����
		for (int i = 1; i <= G.length; i++) {
			// �ݹ���㼴��
			if (i == 1)
				cg[i-1] = G[0];
			else
				cg[i-1] = cg[i - 2] + G[i-1];

		}
		return cg;
	}	
}
