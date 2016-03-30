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
	 * 抗体库中包含一个抗体列表
	 */
	private ArrayList<Antibody> abR ;
	/**
	 * 抗体库中抗体数量
	 */
	private int abCount;
	
	/**
	 * 随机构造指定个数的抗体库
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
		
	private CountDownLatch countDownLatch;//用来计算所有线程执行的总时间
	
	
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
		return new MyIterator();//返回一个MyIterator实例对象  
		
	}

	/**
	 * 根据亲和力选择前i个抗体
	 * @param i前i个抗体
	 * @param gap每个抗体之间的间隔数量。gap越小，抗体结构越相似
	 * @return
	 */
	public AntibodyRepertoire selectTop(int i,int gap) {
		AntibodyRepertoire topI ;
		//使用优先队列获取topi,使用了MaxPQ类
		MaxPQ<Antibody> topantibody = new MaxPQ<Antibody>(i);
		//加入堆
		for(int j = 0 ;j< abR.size();j++)
			topantibody.insert(abR.get(j));
		//返回top i
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
		//首先对精英抗体库排序，从低到高
	
		//abR.sort(new AntibodyComparator());
	/*	for(Antibody ab : abR)
			System.out.print(ab.getAffinity()+" ");*/
		System.out.println();
		//随机生成d个抗体
		AntibodyRepertoire repabr = new AntibodyRepertoire(d);
		//删除abR中的前d个抗体
		for(int i =0;i<d;i++)
			abR.remove(i);
		
	/*	for(Antibody ab : abR)
			System.out.print(ab.getAffinity()+" ");
		System.out.println();*/
		//加入repabr
		abR.addAll(repabr.getAbR());
	/*	for(Antibody ab : abR)
		System.out.print(ab.getAffinity()+" ");*/
	}
/**
 * 从抗体库中选择表现最好的抗体。策略：
 * 比较抗体的亲和力和验证集上的亲和力的平均值。
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
		for(Antibody ab : abR){												//计算在校验集上的亲和力
			ab.computeAffinity(validation);
		}		
		for(int i = 0;i<aff.length;i++)
		{
			val[i]=abR.get(i).getAffinity();
			avg[i] = (aff[i]+val[i])/2;
		}
		//在avg[i]中寻找最大的那个元素下标，就是那个抗体
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
		ParallelRankBCAClient.outputWindow.append("\n最好抗体在训练集合校验集上的平均亲和力为："+avg[idx]);
		ParallelRankBCAClient.outputWindow.append("\n最好抗体在验证集上的亲和力："+val[idx]);
		ParallelRankBCAClient.outputWindow.append("\n最好抗体在训练集上的亲和力："+aff[idx]);
		ParallelRankBCAClient.outputWindow.append("\n最好抗体表达式为：");
		abR.get(idx).printFunction();
		return abR.get(idx);
	}
	
	/**
	 * 这样能够使用foreach语法遍历抗体库
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
	 * 去重操作。比较抗体函数的字符串表示即可。
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
						abR.get(i).setAffinity(-1);//将重复标记一下。
						n++;
						break;
					}
				}			
		}
		//将标记的删除
		for(int i =0;i<abR.size();i++)
			if(abR.get(i).getAffinity()==-1)
				abR.remove(i);
		//System.out.println("发现"+n+"个重复");
		return n;
	}

	/**
	 * 向抗体库中增加抗体,用随机的抗体替换重复的
	 * @param dupNum增加抗体的数量
	 */
	public void fillComplete(int dupNum) {
		for(int i =0;i<dupNum;i++)
			abR.add(new Antibody());
	}

	/**
	 * 封装方法
	 */
	public void removeDulplicate() {
		int dupNum = duplicateRemoval();														//抗体库去重
		while(dupNum!=0){
				System.out.println("发现"+dupNum+"个重复,替换重复");
				fillComplete(dupNum);															//如果去掉重复，补充
				dupNum = duplicateRemoval();													//万一又引入了重复
		}
		if(dupNum==0)
			System.out.println("去重检验完成");
		}

	@Override
	//完成这个子群的进化
	public void run() {
		int i = 1; // 当前迭代次数
		//计算亲和力只需要计算一次。变异后的抗体仍然会计算一次。如果替换了父代抗体，变异体本身自带亲和力。
		for (int i1 = 0; i1 < abR.size(); i1++) { // 计算亲和力
			Antibody ab = abR.get(i1);
			ab.computeAffinity(Config.TRAIN);
		}
		do {			
			//******************主要循环***************************
			
		//	System.out.println(Thread.currentThread().getName()+"计算完亲和力...");	
			//CloneAndCross.cloneAndCross(ParallelBCA.AR);//种群而非子群,克隆和交叉一起做
			//这里就不能进行克隆和交叉操作了。需要一个单独的线程
			synchronized(Config.lockObj){
					try {						
					//	System.out.println(Thread.currentThread().getName()+"等待...");		
						ParallelBCA.allWait.add(true);
						Config.lockObj.wait();//因为wait会释放锁，所以其他子群上的线程可以进入到这行代码。执行到这行代码的线程全部在该对象上等待。
					//	System.out.println(Thread.currentThread().getName()+"继续变异...");		
					
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}		
			
			for(int i1 =0;i1<abR.size();i1++)
			{
				Antibody ab = abR.get(i1);
				for (Antibody ab2 : ab.pool) {
					ab2.mutate();//连续区域变异
				}
			}
			for(int i1 =0;i1<abR.size();i1++)
			{
				Antibody ab = abR.get(i1);
				for (Antibody ab3 : ab.pool) {//重算亲和力，亲和力比父亲高，则替换
					ab3.computeAffinity(Config.TRAIN);
					if (ab3.getAffinity() > abR.get(i1).getAffinity()) {
						abR.set(i1, ab3);
						//一旦更新了，这个新的抗体并没有克隆池。
					}
				}
			}
		
			ParallelRankBCAClient.outputWindow.append(".");	
			i++;
		} while (i <= Config.Gen);
	
		countDownLatch.countDown();//计数器减1
		}	
	}


