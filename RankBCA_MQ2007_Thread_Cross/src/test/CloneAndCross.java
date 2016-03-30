package test;

import java.util.ArrayList;

import tyut.edu.bca.Antibody;
import tyut.edu.bca.AntibodyRepertoire;
import tyut.edu.bca.CloneUtils;

public class CloneAndCross {
	
	public  int N=Config.N ; 				// 种群规模
	public  double beta=Config.clones ; 	// 克隆因子
	public  int nc =(int) (N * beta);		//克隆规模
	public  int group ;						//分成几组
	public  AntibodyRepertoire L  ;			// 抗体库
	public  AntibodyRepertoire[] groups ; 	// 分组
	
	public CloneAndCross(AntibodyRepertoire aR) {
		L = aR;
		init();
	}



	public void init(){
		if(nc!=1){
			if(N<=nc)
				group=1;
			else if(N>nc && N%nc==0)
				group=N / nc ;
			else if(N>nc && N%nc!=0)
				group= N/nc+1;		
			
			 groups = new AntibodyRepertoire[group];
				// 分组		
				int gap = 0;
				if (N < nc)
					gap = N;
				else
					gap = nc;
				for (int i = 0; i < group; i++) {
					groups[i] = new AntibodyRepertoire(gap);
					groups[i].getAbR().clear();// 清空列表！！！！！！！！！！！！！！！
				}
			
				if (N <= nc)// 1组
				{
					for (int i = 0; i < L.getAbCount(); i++)
						groups[0].getAbR().add(L.getAbR().get(i));
				} else if (N >= nc && N % nc == 0) {// 正好
					for (int j = 0; j < group; j++)
						for (int i = ((j == 0) ? 0 : (gap * j)), k = i; i < k+ gap; i++)//这里出现过错误。注意是gap*j
							groups[j].getAbR().add(L.getAbR().get(i));
				} else if (N > nc && N % nc != 0) {// 多出不满nc的几个
					for (int j = 0; j < group - 1; j++)
						for (int i = ((j == 0) ? 0 : (gap * j)), k = i; i < k+ gap; i++)
							groups[j].getAbR().add(L.getAbR().get(i));
			
					// 剩下N%nc个为为1组,最后一组
					for (int i = gap * (N / nc); i < N; i++)
						groups[group - 1].getAbR().add(L.getAbR().get(i));
				}
		}
			
		
	}
	
	 
	
	/**
	 * 用于BCA的克隆。克隆个数为种群大小。
	 * 
	 * @param clonefactor是种群大小的乘数因子
	 * @param n是种群大小
	 */
	private static  AntibodyRepertoire cloneBCell(Antibody a, double clonefactor, int n) {
		ArrayList<Antibody> abl = new ArrayList<Antibody>();
		// 克隆数量
		int num = 0;
		num = (int) (clonefactor * n);
		// System.out.println("这个抗体克隆个数："+num);
		for (int i = 0; i < num; i++) {
			Antibody ab = CloneUtils.clone(a);
			abl.add(ab);
		}
		AntibodyRepertoire abrclone = new AntibodyRepertoire(abl);
		return abrclone;
	}
	
	 


	// 交叉
	private  void cross() {

		if (N < nc) {// group=1
			int i = 1;
			for (int j = 1; j < N; j++)
				for (int s = 1, k = j; k < N; k++, s++) {
					swap(groups[i - 1].getAbR().get(j - 1).getPool(), (k - 1),
							groups[i - 1].getAbR().get(j + s - 1).getPool(),
							(j - 1));

				}

		} else if (N > nc && N % nc == 0) {
			for (int i = 1; i <= group; i++)
				for (int j = 1; j < nc; j++)
					for (int s = 1, k = j; k < nc; k++, s++)
						swap(groups[i - 1].getAbR().get(j - 1).getPool(),
								(k - 1), groups[i - 1].getAbR().get(j + s - 1)
										.getPool(), (j - 1));
		} else if (N > nc && N % nc != 0) {
			// 前group-1组是整除的
			for (int i = 1; i <= group - 1; i++)
				for (int j = 1; j < nc; j++)
					for (int s = 1, k = j; k < nc; k++, s++)
						swap(groups[i - 1].getAbR().get(j - 1).getPool(),
								(k - 1), groups[i - 1].getAbR().get(j + s - 1)
										.getPool(), (j - 1));
			// 最后一组剩下的
			int i = group;
			for (int j = 1; j < N % nc; j++)
				for (int s = 1, k = j; k < N % nc; k++, s++)
					swap(groups[i - 1].getAbR().get(j - 1).getPool(), (k - 1),
							groups[i - 1].getAbR().get(j + s - 1).getPool(),
							(j - 1));
		}

	}

	// 交换
	private static void swap(AntibodyRepertoire ar1, int i, AntibodyRepertoire ar2,	int j) {
		Antibody temp = ar1.getAbR().get(i);
		ar1.getAbR().set(i, ar2.getAbR().get(j));
		ar2.getAbR().set(j, temp);

	}

	/**
	 * 对种群进行克隆和交叉
	 * 
	 * @param population 目前的种群
	 */
	public void cloneAndCross() {
		// 克隆
		//System.out.println("kelong ....");
	 
		for (int i = 0; i < L.getAbCount(); i++) {
			//克隆之前一定要把pool设置为null。否则连带pool的所有抗体全部都克隆。内存泄露出现在这里！！
			Antibody an = L.getAbR().get(i);
			an.setPool(null);//这一步非常重要！
			AntibodyRepertoire pool = cloneBCell(an, beta, N);
			an.setPool(pool);
		}
		
		//System.out.println("jiaocha ....");
		// 交叉
		if(nc!=1)//克隆1个化就不交叉了
			cross();
	
	}

	

}
