package tyut.edu.bca;

public class DCG {
	// �����������ӵĵ�
	public static final int B = 2;
	public static double dcg[];
	public static double[] g;
	public static double[] getDCG(double[] G) {
		//2^i-1��gain
		g = new double[G.length];
		
		for(int i = 0;i<G.length;i++)
		{
			g[i] = Math.pow(2, G[i])-1;
		}
				
		dcg = new double[G.length];
		// ����ԭʼ����
		for (int i = 1; i <= G.length; i++) {
			// �ݹ���㼴��
			if (i == 1)
				dcg[i-1] = g[0];
			else
				dcg[i-1] = dcg[i - 2] + g[i-1] / (Math.log(i+1) / Math.log(B));//i+1�Ķ���

		}
		return dcg;
	}
}
