package tyut.edu.bca;
import java.util.Arrays;


public class NDCG {
	public static double[] getNDCG(double g[]) {
		double[] gBest = new double[g.length];
		System.arraycopy(g, 0, gBest, 0, g.length);
		Arrays.sort(gBest);
		//逆序
		for(int i=0,j=g.length-1;i<j;i++,j--)
			{
			double temp  = gBest[i];
			gBest[i] = gBest[j];
			gBest[j] = temp;
			}
	
		// 分别计算DCG
		double[] gBest_dcg = DCG.getDCG(gBest);
		double[] g_dcg = DCG.getDCG(g);
//System.out.println(Arrays.toString(gBest_dcg));
//System.out.println(Arrays.toString(g_dcg));
		// 计算NDCG
		double[] NDCG = new double[g.length];
		for (int i = 0; i < g.length; i++) {
			if(gBest_dcg[i]!=0)
			NDCG[i] = g_dcg[i]/gBest_dcg[i] ;
			else
				NDCG[i] = (double) 0;
		}
		return NDCG;
	}
}
