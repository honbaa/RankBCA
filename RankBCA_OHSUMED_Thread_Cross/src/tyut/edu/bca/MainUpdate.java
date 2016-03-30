package tyut.edu.bca;

import java.util.ArrayList;
import java.util.List;

import tyut.edu.utilities.FileUtils;

public class MainUpdate {
	/***********************************
	 * �򣬰������㷨�����ò���
	 ***********************************/
	private static  AntigenRepertoire TRAIN;											//��̬��ԭ��
	private static  AntigenRepertoire VALI;												//��̬��֤��
	private static  AntigenRepertoire TEST;												//��̬���Լ�
	//private static final String TRAIN_PATH = "";										//ѵ������·��
	private static final int GEN = 200;								//����
	private static final int N = 50;													//ѡ���TOP��������
	private static final double CLONEFACTOR = 10;										//��¡����
	private static final double MUTATIONACTOR = 0.5;									//��������
	private static final int R = 500;													//��Ⱥ��С
	private static final int D = 100;													//�滻����
	private static final String MODELPAHT = "";
	public static final String TRAIN_TXT = "D:\\�ҵ�����\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\train.txt";
	public static final String VALI_TXT = "D:\\�ҵ�����\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\vali.txt";
	public static final String TEST_TXT = "D:\\�ҵ�����\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\test.txt";
	public static final int GAP = 9;													//ѡ������
	
	
	static {
		System.out.println("��ʼ����ԭ��......");
		TRAIN=buildAntigenRepertoire(TRAIN_TXT);	
		System.out.println("��ʼ����֤��......");
		VALI = buildAntigenRepertoire(VALI_TXT);
		System.out.println("��ʼ�����Լ�......");
		TEST = buildAntigenRepertoire(TEST_TXT);
	}
	/***********************************
	 * �㷨
	 ***********************************/
	public void rankCsa() {
		StringBuilder outfile = new StringBuilder();
		long startTime=System.currentTimeMillis();   //��ȡ��ʼʱ��  
		System.out.println("��ʼ��"+R+"������......");
		AntibodyRepertoire abR = initializeAntibodyRepertoire(R);									//���������
		System.out.println("׼��ȥ��......");
		int dupNum = abR.duplicateRemoval();														//�����ȥ��
		if(dupNum!=0){
			System.out.println("�����ظ�,�滻�ظ�");
			abR.fillComplete(dupNum);																//���ȥ���ظ�������
		}else{
			System.out.println("û�з����ظ�");
		}
		AntibodyRepertoire topNabR =null;															//top N���弯��
		AntibodyRepertoire cloned = null;															//��ʱ��¡����	
		AntibodyRepertoire highNotInTopN = null;
		AntibodyRepertoire elites = null;
		int  i = 1;																//��ǰ��������
		do{
			System.out.println("-------------------------��"+(i)+"�ֿ�ʼ-------------------------");
			System.out.println("���㿹���׺���....");
		
			for(Antibody ab : abR){												//�����׺���
				ab.computeAffinity(TRAIN);
			}
			System.out.println("ѡ��"+N+"���׺�����ߵĿ���");
			topNabR = abR.selectTop(N,GAP);											//ѡ��top n�Ŀ���	
		
			System.out.println("��¡"+N+"���׺�����ߵĿ���");
			AntibodyRepertoire cloneFinishedAbR = new AntibodyRepertoire(new ArrayList<Antibody>());	//��¡�ܼ���
			for (Antibody ab : topNabR){										//��¡top n�е����п���
				cloned = ab.onClone(CLONEFACTOR,N);
				cloneFinishedAbR.merge(cloned);
			}
			System.out.println("��¡���Ϲ�����ɣ�һ����"+cloneFinishedAbR.getAbCount()+"������");
			System.out.println("����"+cloneFinishedAbR.getAbCount()+"������...");
		//	cloneFinishedAbR.getAbR().sort(new AntibodyComparator());
			
			int middle = (int)(cloneFinishedAbR.getAbR().size()*0.8);
			//���׺�����������ǰ80%����죬��20%������
			List<Antibody> many = cloneFinishedAbR.getAbR().subList(0, middle);
			List<Antibody> single = cloneFinishedAbR.getAbR().subList(middle,cloneFinishedAbR.getAbR().size());
			System.out.println("�����"+middle+"������...");
			for(Antibody ab: many)
				ab.multiMutate(2);
			System.out.println("������"+(cloneFinishedAbR.getAbR().size()-middle)+"������...");
			for(Antibody ab: single)
				ab.singleMutate();			
			
			
			System.out.println("��ʼ�����׺���.....");
			for (Antibody ab : cloneFinishedAbR){								 //���¼���������׺���
				ab.computeAffinity(TRAIN);
			}
			System.out.println("�����׺�����ɣ�");
			System.out.println("�ϲ�");
			
			//�����Ŀ�����top n�ϲ�
			cloneFinishedAbR.merge(topNabR);
			System.out.println("ѡ��");
			//ѡ����׺����Ŀ���Ⱥ
			elites = cloneFinishedAbR.selectTop(R, 0);
			System.out.println("ȥ��");
			//ȥ��
			elites.removeDulplicate();
			
			
			
			//highNotInTopN = cloneFinishedAbR.selectTopFromMataion(R-N);			//�ӳ���Ŀ�����ѡ��R-N�����׺���,��Top N �еĲ�ͬ
			//highNotInTopN = cloneFinishedAbR.selectTop(R-N,GAP);//&&&&&&��ʱ���б��Ƿ���Top N �ظ�
			//System.out.println("�ӱ�����弯��ѡ���׺�����ߵ�"+(R-N)+"��������ɣ�");
			//highNotInTopN.merge(topNabR);								//���ε�����R����Ӣ����
			
		//	elites.removeDulplicate();//ȥ��
			System.out.println(elites.getAbCount());
					
			
			if(i!=GEN)//���һ�ֲ��滻��
			{	elites.replaceLowest(D);											//����滻��Ӣ������е�D������׺�������
				System.out.println("�滻��Ӣ�е���͵�"+D+"��������ɣ�");
			}else//���һ������
		//		elites.getAbR().sort(new AntibodyComparator());
			abR = elites;
			
			//if(i%1==0)//ÿ10����һ������
			{
				
				Antibody bestAb = abR.selectBest(VALI);
				double Map = bestAb.evaluateMap(TEST);
				String a = "\n��"+i+"�������ſ����ڲ��Լ��ϵ�MAP��"+Map+"\n";
				System.out.println(a);
				outfile.append(a);
			}
			System.out.println("-------------------------��"+(i++)+"�����-------------------------");
			
		}while(i<=GEN);					
		/*System.out.println("�����ַ�����");
		for(Antibody ab : abR)
			System.out.println(ab.toString());	
		
		
		System.out.println("ѵ�����ϣ����տ�����п�������ձ��֣�");
		for(Antibody ab : abR)
			System.out.println(ab.getAffinity()+" ");	
		System.out.println("ѵ�����ϣ���������׺���Ϊ��"+abR.getAbR().get(abR.getAbCount()-1).getAffinity());
		System.out.println("�������տ��������֤���ϵ��׺���....");
		for(Antibody ab : abR){												//�����׺���
			ab.computeAffinity(VAL);
		}		
		System.out.println("��֤���ϣ�������п�������ձ��֣�");
		for(Antibody ab : abR)
			System.out.println(ab.getAffinity()+" ");	*/
		//Antibody bestAb = abR.selectBest(VAL);										//ѡ����ÿ���
		
		//*******************����bestAb*********************************************************
		long endTime=System.currentTimeMillis(); //��ȡ����ʱ��  
		System.out.println("ִ��"+GEN+"��ʹ����"+(endTime-startTime)/1000/60+"����");
		System.out.println("������۲�ֵд���ļ�....");
		FileUtils.write("D:\\�ҵ�����\\ʵ��\\observe.txt", "utf-8", outfile.toString());
	//	double Map = bestAb.evaluateMap(EVA);
	//	double[] precision = bestAb.evaluatePrecision(EVA);
		//double[] ndcg = bestAb.evaluateNDCG(EVA);
		
		
		//bestAb.writeToModelFile(MODELPAHT);										//��ģ��д���ļ�

	}


	/***********************************
	 * ��ʼ�ķ������������ָ���������忹��
	 ***********************************/
	private AntibodyRepertoire initializeAntibodyRepertoire(int i) {
		return new AntibodyRepertoire(i);		
	}


/**
 * ��ʼ����ԭ��
 * @param trainTxt 
 * @param path
 * @return
 */
	private static AntigenRepertoire buildAntigenRepertoire(String trainTxt) {
		AntigenRepertoire agr = new AntigenRepertoire(trainTxt);
		return agr;
		// TODO Auto-generated method stub
		
	}
	/***********************************
	 * main����
	 ***********************************/
	public static void main(String[] args) {
		new MainUpdate().rankCsa();
	}
}
