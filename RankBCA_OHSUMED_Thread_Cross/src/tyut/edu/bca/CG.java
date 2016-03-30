package tyut.edu.bca;

public class CG {
	public static double cg[];

	public static double[] getCG(double[] G) {
			cg = new double[G.length];
		// 遍历原始向量
		for (int i = 1; i <= G.length; i++) {
			// 递归计算即可
			if (i == 1)
				cg[i-1] = G[0];
			else
				cg[i-1] = cg[i - 2] + G[i-1];

		}
		return cg;
	}	
}
