package test;

import java.util.ArrayList;

import tyut.edu.bca.Antibody;
import tyut.edu.bca.AntibodyRepertoire;
import tyut.edu.bca.CloneUtils;

public class CloneAndCross {
	
	public  int N=Config.N ; 				// ��Ⱥ��ģ
	public  double beta=Config.clones ; 	// ��¡����
	public  int nc =(int) (N * beta);		//��¡��ģ
	public  int group ;						//�ֳɼ���
	public  AntibodyRepertoire L  ;			// �����
	public  AntibodyRepertoire[] groups ; 	// ����
	
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
				// ����		
				int gap = 0;
				if (N < nc)
					gap = N;
				else
					gap = nc;
				for (int i = 0; i < group; i++) {
					groups[i] = new AntibodyRepertoire(gap);
					groups[i].getAbR().clear();// ����б�����������������������������
				}
			
				if (N <= nc)// 1��
				{
					for (int i = 0; i < L.getAbCount(); i++)
						groups[0].getAbR().add(L.getAbR().get(i));
				} else if (N >= nc && N % nc == 0) {// ����
					for (int j = 0; j < group; j++)
						for (int i = ((j == 0) ? 0 : (gap * j)), k = i; i < k+ gap; i++)//������ֹ�����ע����gap*j
							groups[j].getAbR().add(L.getAbR().get(i));
				} else if (N > nc && N % nc != 0) {// �������nc�ļ���
					for (int j = 0; j < group - 1; j++)
						for (int i = ((j == 0) ? 0 : (gap * j)), k = i; i < k+ gap; i++)
							groups[j].getAbR().add(L.getAbR().get(i));
			
					// ʣ��N%nc��ΪΪ1��,���һ��
					for (int i = gap * (N / nc); i < N; i++)
						groups[group - 1].getAbR().add(L.getAbR().get(i));
				}
		}
			
		
	}
	
	 
	
	/**
	 * ����BCA�Ŀ�¡����¡����Ϊ��Ⱥ��С��
	 * 
	 * @param clonefactor����Ⱥ��С�ĳ�������
	 * @param n����Ⱥ��С
	 */
	private static  AntibodyRepertoire cloneBCell(Antibody a, double clonefactor, int n) {
		ArrayList<Antibody> abl = new ArrayList<Antibody>();
		// ��¡����
		int num = 0;
		num = (int) (clonefactor * n);
		// System.out.println("��������¡������"+num);
		for (int i = 0; i < num; i++) {
			Antibody ab = CloneUtils.clone(a);
			abl.add(ab);
		}
		AntibodyRepertoire abrclone = new AntibodyRepertoire(abl);
		return abrclone;
	}
	
	 


	// ����
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
			// ǰgroup-1����������
			for (int i = 1; i <= group - 1; i++)
				for (int j = 1; j < nc; j++)
					for (int s = 1, k = j; k < nc; k++, s++)
						swap(groups[i - 1].getAbR().get(j - 1).getPool(),
								(k - 1), groups[i - 1].getAbR().get(j + s - 1)
										.getPool(), (j - 1));
			// ���һ��ʣ�µ�
			int i = group;
			for (int j = 1; j < N % nc; j++)
				for (int s = 1, k = j; k < N % nc; k++, s++)
					swap(groups[i - 1].getAbR().get(j - 1).getPool(), (k - 1),
							groups[i - 1].getAbR().get(j + s - 1).getPool(),
							(j - 1));
		}

	}

	// ����
	private static void swap(AntibodyRepertoire ar1, int i, AntibodyRepertoire ar2,	int j) {
		Antibody temp = ar1.getAbR().get(i);
		ar1.getAbR().set(i, ar2.getAbR().get(j));
		ar2.getAbR().set(j, temp);

	}

	/**
	 * ����Ⱥ���п�¡�ͽ���
	 * 
	 * @param population Ŀǰ����Ⱥ
	 */
	public void cloneAndCross() {
		// ��¡
		//System.out.println("kelong ....");
	 
		for (int i = 0; i < L.getAbCount(); i++) {
			//��¡֮ǰһ��Ҫ��pool����Ϊnull����������pool�����п���ȫ������¡���ڴ�й¶�����������
			Antibody an = L.getAbR().get(i);
			an.setPool(null);//��һ���ǳ���Ҫ��
			AntibodyRepertoire pool = cloneBCell(an, beta, N);
			an.setPool(pool);
		}
		
		//System.out.println("jiaocha ....");
		// ����
		if(nc!=1)//��¡1�����Ͳ�������
			cross();
	
	}

	

}
