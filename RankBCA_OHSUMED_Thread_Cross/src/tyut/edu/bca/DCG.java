package tyut.edu.bca;

public class DCG {
	// 对数这算因子的底
	public static final int B = 2;
	public static double dcg[];
	public static double[] g;
	public static double[] getDCG(double[] G) {
		//2^i-1是gain
		g = new double[G.length];
		
		for(int i = 0;i<G.length;i++)
		{
			g[i] = Math.pow(2, G[i])-1;
		}
				
		dcg = new double[G.length];
		// 遍历原始向量
		for (int i = 1; i <= G.length; i++) {
			// 递归计算即可
			if (i == 1)
				dcg[i-1] = g[0];
			else
				dcg[i-1] = dcg[i - 2] + g[i-1] / (Math.log(i+1) / Math.log(B));//i+1的对数

		}
		return dcg;
	}
}
