package tyut.edu.bca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import test.CloneAndCross;
import test.Config;
import test.ParallelBCA;
import test.ParallelRankBCAClient;

public class AntibodyRepertoire implements Iterable<Antibody>,Runnable,Serializable{
	public static Object lockObj= new Object();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ������а���һ�������б�
	 */
	private ArrayList<Antibody> abR ;
	/**
	 * ������п�������
	 */
	private int abCount;
	
	/**
	 * �������ָ�������Ŀ����
	 */
	public AntibodyRepertoire(int abCount) {
		super();
		abR = new ArrayList<Antibody>();
		for (int i = 0; i < abCount; i++) 
		{
		Antibody ab = new Antibody();	
		abR.add(ab);
		}		
		this.abCount = abCount;
	}
	
	public int getAbCount() {
		return abCount;
	}
	public void setAbCount(int abCount) {
		this.abCount = abCount;
	}
	public AntibodyRepertoire(ArrayList<Antibody> abR) {
		super();
		this.abR = abR;
		this.abCount = abR.size();
	}
	
	
	public ArrayList<Antibody> getAbR() {
		return abR;
	}

	public void setAbR(ArrayList<Antibody> abR) {
		this.abR = abR;
	}
		
	private CountDownLatch countDownLatch;//�������������߳�ִ�е���ʱ��
	
	
	/**
	 * @return the countDownLatch
	 */
	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	/**
	 * @param countDownLatch the countDownLatch to set
	 */
	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
	@Override
	public Iterator<Antibody> iterator() {
		return new MyIterator();//����һ��MyIteratorʵ������  
		
	}

	/**
	 * �����׺���ѡ��ǰi������
	 * @param iǰi������
	 * @param gapÿ������֮��ļ��������gapԽС������ṹԽ����
	 * @return
	 */
	public AntibodyRepertoire selectTop(int i,int gap) {
		AntibodyRepertoire topI ;
		//ʹ�����ȶ��л�ȡtopi,ʹ����MaxPQ��
		MaxPQ<Antibody> topantibody = new MaxPQ<Antibody>(i);
		//�����
		for(int j = 0 ;j< abR.size();j++)
			topantibody.insert(abR.get(j));
		//����top i
		ArrayList<Antibody> abl = new ArrayList<Antibody>();
		for(int n = 0;n<i;n++)
		{
			abl.add(topantibody.delMax());
			for(int k = 0;k<gap;k++)
				topantibody.delMax();
		}
		
			
		topI = new AntibodyRepertoire(abl);
		return topI;
	}

	public void merge(AntibodyRepertoire cloned) {
		this.abR.addAll(cloned.getAbR());
		this.abCount+=cloned.getAbCount();	
		
	}

	public AntibodyRepertoire selectTopFromMataion(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public void replaceLowest(int d) {
		//���ȶԾ�Ӣ��������򣬴ӵ͵���
	
		//abR.sort(new AntibodyComparator());
	/*	for(Antibody ab : abR)
			System.out.print(ab.getAffinity()+" ");*/
		System.out.println();
		//�������d������
		AntibodyRepertoire repabr = new AntibodyRepertoire(d);
		//ɾ��abR�е�ǰd������
		for(int i =0;i<d;i++)
			abR.remove(i);
		
	/*	for(Antibody ab : abR)
			System.out.print(ab.getAffinity()+" ");
		System.out.println();*/
		//����repabr
		abR.addAll(repabr.getAbR());
	/*	for(Antibody ab : abR)
		System.out.print(ab.getAffinity()+" ");*/
	}
/**
 * �ӿ������ѡ�������õĿ��塣���ԣ�
 * �ȽϿ�����׺�������֤���ϵ��׺�����ƽ��ֵ��
 * @return
 */
	public Antibody selectBest(AntigenRepertoire validation) {
		double[] aff = new double[abR.size()];
		double[] val = new double[abR.size()];
		double[] avg = new double[abR.size()];
		for(int i = 0;i<aff.length;i++)
		{
			aff[i]=abR.get(i).getAffinity();
		}
		for(Antibody ab : abR){												//������У�鼯�ϵ��׺���
			ab.computeAffinity(validation);
		}		
		for(int i = 0;i<aff.length;i++)
		{
			val[i]=abR.get(i).getAffinity();
			avg[i] = (aff[i]+val[i])/2;
		}
		//��avg[i]��Ѱ�������Ǹ�Ԫ���±꣬�����Ǹ�����
		double max=avg[0];
		int idx=0;
		for(int j=1;j<aff.length;j++)
		{
			if(avg[j]>max)
				{
					max = avg[j];
					idx = j;
				}
		}
		ParallelRankBCAClient.outputWindow.append("\n��ÿ�����ѵ������У�鼯�ϵ�ƽ���׺���Ϊ��"+avg[idx]);
		ParallelRankBCAClient.outputWindow.append("\n��ÿ�������֤���ϵ��׺�����"+val[idx]);
		ParallelRankBCAClient.outputWindow.append("\n��ÿ�����ѵ�����ϵ��׺�����"+aff[idx]);
		ParallelRankBCAClient.outputWindow.append("\n��ÿ�����ʽΪ��");
		abR.get(idx).printFunction();
		return abR.get(idx);
	}
	
	/**
	 * �����ܹ�ʹ��foreach�﷨���������
	 * @author dell
	 *
	 */
	class MyIterator implements Iterator<Antibody> {
		private int index =0;
		@Override
		public boolean hasNext() {
			if(index < abR.size())
				return true;
			else
			return false;
		}

		@Override
		public Antibody next() {
			return abR.get(index++);
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}

	}
	/**
	 * ȥ�ز������ȽϿ��庯�����ַ�����ʾ���ɡ�
	 * @return 
	 */
	public int duplicateRemoval() {
		int n =0;
		for(int i = 0;i<abR.size();i++)
		{
				for(int j=i+1;j<abR.size();j++)
				{
					if(abR.get(i).toString().equals(abR.get(j).toString()))
					{	
						abR.get(i).setAffinity(-1);//���ظ����һ�¡�
						n++;
						break;
					}
				}			
		}
		//����ǵ�ɾ��
		for(int i =0;i<abR.size();i++)
			if(abR.get(i).getAffinity()==-1)
				abR.remove(i);
		//System.out.println("����"+n+"���ظ�");
		return n;
	}

	/**
	 * ����������ӿ���,������Ŀ����滻�ظ���
	 * @param dupNum���ӿ��������
	 */
	public void fillComplete(int dupNum) {
		for(int i =0;i<dupNum;i++)
			abR.add(new Antibody());
	}

	/**
	 * ��װ����
	 */
	public void removeDulplicate() {
		int dupNum = duplicateRemoval();														//�����ȥ��
		while(dupNum!=0){
				System.out.println("����"+dupNum+"���ظ�,�滻�ظ�");
				fillComplete(dupNum);															//���ȥ���ظ�������
				dupNum = duplicateRemoval();													//��һ���������ظ�
		}
		if(dupNum==0)
			System.out.println("ȥ�ؼ������");
		}

	@Override
	//��������Ⱥ�Ľ���
	public void run() {
		int i = 1; // ��ǰ��������
		//�����׺���ֻ��Ҫ����һ�Ρ������Ŀ�����Ȼ�����һ�Ρ�����滻�˸������壬�����屾���Դ��׺�����
		for (int i1 = 0; i1 < abR.size(); i1++) { // �����׺���
			Antibody ab = abR.get(i1);
			ab.computeAffinity(Config.TRAIN);
		}
		do {			
			//******************��Ҫѭ��***************************
			
		//	System.out.println(Thread.currentThread().getName()+"�������׺���...");	
			//CloneAndCross.cloneAndCross(ParallelBCA.AR);//��Ⱥ������Ⱥ,��¡�ͽ���һ����
			//����Ͳ��ܽ��п�¡�ͽ�������ˡ���Ҫһ���������߳�
			synchronized(Config.lockObj){
					try {						
					//	System.out.println(Thread.currentThread().getName()+"�ȴ�...");		
						ParallelBCA.allWait.add(true);
						Config.lockObj.wait();//��Ϊwait���ͷ���������������Ⱥ�ϵ��߳̿��Խ��뵽���д��롣ִ�е����д�����߳�ȫ���ڸö����ϵȴ���
					//	System.out.println(Thread.currentThread().getName()+"��������...");		
					
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}		
			
			for(int i1 =0;i1<abR.size();i1++)
			{
				Antibody ab = abR.get(i1);
				for (Antibody ab2 : ab.pool) {
					ab2.mutate();//�����������
				}
			}
			for(int i1 =0;i1<abR.size();i1++)
			{
				Antibody ab = abR.get(i1);
				for (Antibody ab3 : ab.pool) {//�����׺������׺����ȸ��׸ߣ����滻
					ab3.computeAffinity(Config.TRAIN);
					if (ab3.getAffinity() > abR.get(i1).getAffinity()) {
						abR.set(i1, ab3);
						//һ�������ˣ�����µĿ��岢û�п�¡�ء�
					}
				}
			}
		
			ParallelRankBCAClient.outputWindow.append(".");	
			i++;
		} while (i <= Config.Gen);
	
		countDownLatch.countDown();//��������1
		}	
	}


