package tyut.edu.bca;

import java.util.ArrayList;
import java.util.List;

import tyut.edu.utilities.FileUtils;

public class Main {
	/***********************************
	 * �򣬰������㷨�����ò���
	 ***********************************/
	private static  AntigenRepertoire TRAIN;											//��̬��ԭ��
	private static  AntigenRepertoire VALI;												//��̬��֤��
	private static  AntigenRepertoire TEST;												//��̬���Լ�
	
	private static final int GEN = 50;													//����
	private static final double CLONEFACTOR = 1;										//��¡����
	private static final int R = 50;													//��Ⱥ��С
	

	public static final String TRAIN_TXT = "D:\\�ҵ�����\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\train.txt";
	public static final String VALI_TXT = "D:\\�ҵ�����\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\vali.txt";
	public static final String TEST_TXT = "D:\\�ҵ�����\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\test.txt";
	public static final int GAP = 1;													//ѡ������
	
	
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
	public void rankBsa() {
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
										
		AntibodyRepertoire clonePool = null;													     //��¡��
	
	
		int  i = 1;																//��ǰ��������
		do{
			System.out.println("-------------------------��"+(i)+"�ֿ�ʼ-------------------------");
			//System.out.println("���㿹���׺���....");
		
			for(int i1 =0;i1<abR.getAbCount();i1++){												//�����׺���
				Antibody ab = abR.getAbR().get(i1);
				ab.computeAffinity(TRAIN);
				clonePool = ab.cloneBCell(CLONEFACTOR,R);						//�򵥿�¡
		
				for(Antibody ab2 : clonePool)
					ab2.mutate();
				for(Antibody ab3 : clonePool)
				{
					ab3.computeAffinity(TRAIN);
					if(ab3.getAffinity()>ab.getAffinity())
					{	abR.getAbR().remove(i1);
						abR.getAbR().add(ab3);
					}
				}
			}
							
				Antibody bestAb = abR.selectBest(VALI);
				System.out.println("\n�ٴο���ÿ�������֤���ϵ��׺�����"+bestAb.computeAffinity(VALI)+"\n");
				double Map = bestAb.evaluateMap(TEST);
				String a = "\n��"+i+"�������ſ����ڲ��Լ��ϵ�MAP��"+Map+"\n";
				System.out.println(a);
				outfile.append(a);		
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
		new Main().rankBsa();
	}
}
