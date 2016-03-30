package tyut.edu.bca;

import java.util.ArrayList;
import java.util.List;

import tyut.edu.utilities.FileUtils;

public class MainUpdate {
	/***********************************
	 * 域，包含了算法的配置参数
	 ***********************************/
	private static  AntigenRepertoire TRAIN;											//静态抗原库
	private static  AntigenRepertoire VALI;												//静态验证集
	private static  AntigenRepertoire TEST;												//静态测试集
	//private static final String TRAIN_PATH = "";										//训练样本路径
	private static final int GEN = 200;								//代数
	private static final int N = 50;													//选择的TOP抗体数量
	private static final double CLONEFACTOR = 10;										//克隆因子
	private static final double MUTATIONACTOR = 0.5;									//变异因子
	private static final int R = 500;													//种群大小
	private static final int D = 100;													//替换数量
	private static final String MODELPAHT = "";
	public static final String TRAIN_TXT = "D:\\我的数据\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\train.txt";
	public static final String VALI_TXT = "D:\\我的数据\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\vali.txt";
	public static final String TEST_TXT = "D:\\我的数据\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\test.txt";
	public static final int GAP = 9;													//选择抗体间距
	
	
	static {
		System.out.println("初始化抗原集......");
		TRAIN=buildAntigenRepertoire(TRAIN_TXT);	
		System.out.println("初始化验证集......");
		VALI = buildAntigenRepertoire(VALI_TXT);
		System.out.println("初始化测试集......");
		TEST = buildAntigenRepertoire(TEST_TXT);
	}
	/***********************************
	 * 算法
	 ***********************************/
	public void rankCsa() {
		StringBuilder outfile = new StringBuilder();
		long startTime=System.currentTimeMillis();   //获取开始时间  
		System.out.println("初始化"+R+"个抗体......");
		AntibodyRepertoire abR = initializeAntibodyRepertoire(R);									//进化抗体库
		System.out.println("准备去重......");
		int dupNum = abR.duplicateRemoval();														//抗体库去重
		if(dupNum!=0){
			System.out.println("发现重复,替换重复");
			abR.fillComplete(dupNum);																//如果去掉重复，补充
		}else{
			System.out.println("没有发现重复");
		}
		AntibodyRepertoire topNabR =null;															//top N抗体集合
		AntibodyRepertoire cloned = null;															//临时克隆集合	
		AntibodyRepertoire highNotInTopN = null;
		AntibodyRepertoire elites = null;
		int  i = 1;																//当前迭代次数
		do{
			System.out.println("-------------------------第"+(i)+"轮开始-------------------------");
			System.out.println("计算抗体亲和力....");
		
			for(Antibody ab : abR){												//计算亲和力
				ab.computeAffinity(TRAIN);
			}
			System.out.println("选择"+N+"个亲和力最高的抗体");
			topNabR = abR.selectTop(N,GAP);											//选择top n的抗体	
		
			System.out.println("克隆"+N+"个亲和力最高的抗体");
			AntibodyRepertoire cloneFinishedAbR = new AntibodyRepertoire(new ArrayList<Antibody>());	//克隆总集合
			for (Antibody ab : topNabR){										//克隆top n中的所有抗体
				cloned = ab.onClone(CLONEFACTOR,N);
				cloneFinishedAbR.merge(cloned);
			}
			System.out.println("克隆集合构建完成！一共有"+cloneFinishedAbR.getAbCount()+"个抗体");
			System.out.println("变异"+cloneFinishedAbR.getAbCount()+"个抗体...");
		//	cloneFinishedAbR.getAbR().sort(new AntibodyComparator());
			
			int middle = (int)(cloneFinishedAbR.getAbR().size()*0.8);
			//按亲和力升序排序，前80%多变异，后20%单变异
			List<Antibody> many = cloneFinishedAbR.getAbR().subList(0, middle);
			List<Antibody> single = cloneFinishedAbR.getAbR().subList(middle,cloneFinishedAbR.getAbR().size());
			System.out.println("多变异"+middle+"个抗体...");
			for(Antibody ab: many)
				ab.multiMutate(2);
			System.out.println("单变异"+(cloneFinishedAbR.getAbR().size()-middle)+"个抗体...");
			for(Antibody ab: single)
				ab.singleMutate();			
			
			
			System.out.println("开始重算亲和力.....");
			for (Antibody ab : cloneFinishedAbR){								 //重新计算成熟后的亲和力
				ab.computeAffinity(TRAIN);
			}
			System.out.println("重算亲和力完成！");
			System.out.println("合并");
			
			//变异后的抗体与top n合并
			cloneFinishedAbR.merge(topNabR);
			System.out.println("选择");
			//选择高亲和力的抗体群
			elites = cloneFinishedAbR.selectTop(R, 0);
			System.out.println("去重");
			//去重
			elites.removeDulplicate();
			
			
			
			//highNotInTopN = cloneFinishedAbR.selectTopFromMataion(R-N);			//从成熟的抗体中选出R-N个高亲和力,于Top N 中的不同
			//highNotInTopN = cloneFinishedAbR.selectTop(R-N,GAP);//&&&&&&暂时不判别是否与Top N 重复
			//System.out.println("从变异后抗体集中选出亲和力最高的"+(R-N)+"个抗体完成！");
			//highNotInTopN.merge(topNabR);								//本次迭代的R个精英抗体
			
		//	elites.removeDulplicate();//去重
			System.out.println(elites.getAbCount());
					
			
			if(i!=GEN)//最后一轮不替换了
			{	elites.replaceLowest(D);											//随机替换精英抗体库中的D个最低亲和力抗体
				System.out.println("替换精英中的最低的"+D+"个抗体完成！");
			}else//最后一轮排序
		//		elites.getAbR().sort(new AntibodyComparator());
			abR = elites;
			
			//if(i%1==0)//每10代做一次评价
			{
				
				Antibody bestAb = abR.selectBest(VALI);
				double Map = bestAb.evaluateMap(TEST);
				String a = "\n第"+i+"代后，最优抗体在测试集上的MAP："+Map+"\n";
				System.out.println(a);
				outfile.append(a);
			}
			System.out.println("-------------------------第"+(i++)+"轮完成-------------------------");
			
		}while(i<=GEN);					
		/*System.out.println("抗体字符串：");
		for(Antibody ab : abR)
			System.out.println(ab.toString());	
		
		
		System.out.println("训练集上，最终抗体库中抗体的最终表现：");
		for(Antibody ab : abR)
			System.out.println(ab.getAffinity()+" ");	
		System.out.println("训练集上，最终最佳亲和力为："+abR.getAbR().get(abR.getAbCount()-1).getAffinity());
		System.out.println("计算最终抗体库在验证集上的亲和力....");
		for(Antibody ab : abR){												//计算亲和力
			ab.computeAffinity(VAL);
		}		
		System.out.println("验证集上，抗体库中抗体的最终表现：");
		for(Antibody ab : abR)
			System.out.println(ab.getAffinity()+" ");	*/
		//Antibody bestAb = abR.selectBest(VAL);										//选择最好抗体
		
		//*******************评价bestAb*********************************************************
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("执行"+GEN+"代使用了"+(endTime-startTime)/1000/60+"分钟");
		System.out.println("将抗体观测值写入文件....");
		FileUtils.write("D:\\我的数据\\实验\\observe.txt", "utf-8", outfile.toString());
	//	double Map = bestAb.evaluateMap(EVA);
	//	double[] precision = bestAb.evaluatePrecision(EVA);
		//double[] ndcg = bestAb.evaluateNDCG(EVA);
		
		
		//bestAb.writeToModelFile(MODELPAHT);										//将模型写入文件

	}


	/***********************************
	 * 初始的方法，随机产生指定个数抗体抗体
	 ***********************************/
	private AntibodyRepertoire initializeAntibodyRepertoire(int i) {
		return new AntibodyRepertoire(i);		
	}


/**
 * 初始化抗原库
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
	 * main方法
	 ***********************************/
	public static void main(String[] args) {
		new MainUpdate().rankCsa();
	}
}
