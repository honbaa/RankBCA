package test;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import tyut.edu.bca.Antibody;
import tyut.edu.bca.AntibodyRepertoire;

public class ParallelBCA {

	// 这些变量都是对象的
	public  int N = Config.N; // 种群规模
	public  int M = Config.M; // 分组,M应该可以被N整除
	public AntibodyRepertoire AR; // 抗体库。这个抗体库是总的。//这2个不要static
	public AntibodyRepertoire[] subGroupArrays; // 子群M个
	CountDownLatch countDownLatch;
	// 用于同步,标志线程是否在等待
	public static Vector<Boolean> allWait = new Vector<Boolean>();
	public static boolean isLearning = true;
	
	public  boolean isAllWait() {
		if (allWait.size() != M)
			return false;
		else
			return true;

	}
	public ParallelBCA(){}
	// 初始化
	public ParallelBCA(AntibodyRepertoire ar, CountDownLatch countDownLatch) {
		AR = ar;
		// 分组
		int gap = N / M;
		if ((N % M) != 0)
			try {
				throw new Exception("N不能被M整除！");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		subGroupArrays = new AntibodyRepertoire[M];
		for (int i = 0; i < M; i++) {
			subGroupArrays[i] = new AntibodyRepertoire(gap);
			subGroupArrays[i].getAbR().clear();// 清空列表！！！！！！！！！！！！！！！！！！！！！！！！
			subGroupArrays[i].setCountDownLatch(countDownLatch);
		}

		for (int j = 0; j < M; j++)
			for (int i = ((j == 0) ? 0 : (gap - 1) * j + 1), k = i; i < k + gap; i++)
				subGroupArrays[j].getAbR().add(AR.getAbR().get(i));
		
		this.countDownLatch = countDownLatch;
	}

	public String learn() {
		isLearning = true;
		long startTime = System.currentTimeMillis();// 开始时间
		// M个线程
		Thread[] threadarray = new Thread[Config.M];
		for (int i = 0; i < Config.M; i++)
			threadarray[i] = new Thread(subGroupArrays[i]);// 每个子群属于每个独立的线程
		// M个线程启动
		for (int i = 0; i < Config.M; i++)
			threadarray[i].start();

		// 后台线程判定是否要进行克隆和交叉
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 只要还在学习就检查
				while (isLearning) {
					if (isAllWait()) {
						//System.out.println("开始克隆与交叉..");
						// 更新一下主群
						updateAR();
					//	System.gc();
						CloneAndCross c = new CloneAndCross(AR);
						c.cloneAndCross();;// 开始克隆和交叉
						c = null;
						//System.out.println("交叉完成..");
						// 唤醒所有等待线程继续工作
						synchronized (Config.lockObj) {
						//	System.out.println("唤醒所有线程..");
							ParallelBCA.allWait.clear();// 情况标志
							Config.lockObj.notifyAll();
						}
					}
				}
			}

		}).start();

		// 主线程等待所有学习结束
		try {
			countDownLatch.await();
			//学习完成清空一下变量。
			isLearning = false;
			allWait.clear();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 从abr中选出最优抗体
		// ********************选择最好抗体，预测分数*********************
		updateAR();
		Antibody bestAb = AR.selectBest(Config.VALI);// 选择最佳抗体
		String s = bestAb.predictionScore(Config.TEST);

		ParallelRankBCAClient.outputWindow.append(("\n在这个数据上学习总时间:"
				+ (System.currentTimeMillis() - startTime) + " ms"));
		//继续清理
		AR = null;
		subGroupArrays = null;
		countDownLatch=null;
		return s;
	}

	private void updateAR() {
		AR.getAbR().clear();// 清空
		// subGroupArrays的元素全部加入的AR中
		for (int i = 0; i < M; i++) {
			AntibodyRepertoire ar = subGroupArrays[i];// 拿到一个子群
			for (int j = 0; j < ar.getAbR().size(); j++)
				AR.getAbR().add(ar.getAbR().get(j));
		}
	}
}
