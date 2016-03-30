package test;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import tyut.edu.bca.Antibody;
import tyut.edu.bca.AntibodyRepertoire;

public class ParallelBCA {

	// ��Щ�������Ƕ����
	public  int N = Config.N; // ��Ⱥ��ģ
	public  int M = Config.M; // ����,MӦ�ÿ��Ա�N����
	public AntibodyRepertoire AR; // ����⡣�����������ܵġ�//��2����Ҫstatic
	public AntibodyRepertoire[] subGroupArrays; // ��ȺM��
	CountDownLatch countDownLatch;
	// ����ͬ��,��־�߳��Ƿ��ڵȴ�
	public static Vector<Boolean> allWait = new Vector<Boolean>();
	public static boolean isLearning = true;
	
	public  boolean isAllWait() {
		if (allWait.size() != M)
			return false;
		else
			return true;

	}
	public ParallelBCA(){}
	// ��ʼ��
	public ParallelBCA(AntibodyRepertoire ar, CountDownLatch countDownLatch) {
		AR = ar;
		// ����
		int gap = N / M;
		if ((N % M) != 0)
			try {
				throw new Exception("N���ܱ�M������");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		subGroupArrays = new AntibodyRepertoire[M];
		for (int i = 0; i < M; i++) {
			subGroupArrays[i] = new AntibodyRepertoire(gap);
			subGroupArrays[i].getAbR().clear();// ����б�����������������������������������������������
			subGroupArrays[i].setCountDownLatch(countDownLatch);
		}

		for (int j = 0; j < M; j++)
			for (int i = ((j == 0) ? 0 : (gap - 1) * j + 1), k = i; i < k + gap; i++)
				subGroupArrays[j].getAbR().add(AR.getAbR().get(i));
		
		this.countDownLatch = countDownLatch;
	}

	public String learn() {
		isLearning = true;
		long startTime = System.currentTimeMillis();// ��ʼʱ��
		// M���߳�
		Thread[] threadarray = new Thread[Config.M];
		for (int i = 0; i < Config.M; i++)
			threadarray[i] = new Thread(subGroupArrays[i]);// ÿ����Ⱥ����ÿ���������߳�
		// M���߳�����
		for (int i = 0; i < Config.M; i++)
			threadarray[i].start();

		// ��̨�߳��ж��Ƿ�Ҫ���п�¡�ͽ���
		new Thread(new Runnable() {
			@Override
			public void run() {
				// ֻҪ����ѧϰ�ͼ��
				while (isLearning) {
					if (isAllWait()) {
						//System.out.println("��ʼ��¡�뽻��..");
						// ����һ����Ⱥ
						updateAR();
					//	System.gc();
						CloneAndCross c = new CloneAndCross(AR);
						c.cloneAndCross();;// ��ʼ��¡�ͽ���
						c = null;
						//System.out.println("�������..");
						// �������еȴ��̼߳�������
						synchronized (Config.lockObj) {
						//	System.out.println("���������߳�..");
							ParallelBCA.allWait.clear();// �����־
							Config.lockObj.notifyAll();
						}
					}
				}
			}

		}).start();

		// ���̵߳ȴ�����ѧϰ����
		try {
			countDownLatch.await();
			//ѧϰ������һ�±�����
			isLearning = false;
			allWait.clear();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ��abr��ѡ�����ſ���
		// ********************ѡ����ÿ��壬Ԥ�����*********************
		updateAR();
		Antibody bestAb = AR.selectBest(Config.VALI);// ѡ����ѿ���
		String s = bestAb.predictionScore(Config.TEST);

		ParallelRankBCAClient.outputWindow.append(("\n�����������ѧϰ��ʱ��:"
				+ (System.currentTimeMillis() - startTime) + " ms"));
		//��������
		AR = null;
		subGroupArrays = null;
		countDownLatch=null;
		return s;
	}

	private void updateAR() {
		AR.getAbR().clear();// ���
		// subGroupArrays��Ԫ��ȫ�������AR��
		for (int i = 0; i < M; i++) {
			AntibodyRepertoire ar = subGroupArrays[i];// �õ�һ����Ⱥ
			for (int j = 0; j < ar.getAbR().size(); j++)
				AR.getAbR().add(ar.getAbR().get(j));
		}
	}
}
