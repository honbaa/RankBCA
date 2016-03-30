package tyut.edu.bca;

import java.util.ArrayList;
import java.util.List;

import tyut.edu.utilities.FileUtils;

public class Main {
	/***********************************
	 * 域，包含了算法的配置参数
	 ***********************************/
	private static  AntigenRepertoire TRAIN;											//静态抗原库
	private static  AntigenRepertoire VALI;												//静态验证集
	private static  AntigenRepertoire TEST;												//静态测试集
	
	private static final int GEN = 50;													//代数
	private static final double CLONEFACTOR = 1;										//克隆因子
	private static final int R = 50;													//种群大小
	

	public static final String TRAIN_TXT = "D:\\我的数据\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\train.txt";
	public static final String VALI_TXT = "D:\\我的数据\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\vali.txt";
	public static final String TEST_TXT = "D:\\我的数据\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\test.txt";
	public static final int GAP = 1;													//选择抗体间距
	
	
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
	public void rankBsa() {
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
										
		AntibodyRepertoire clonePool = null;													     //克隆池
	
	
		int  i = 1;																//当前迭代次数
		do{
			System.out.println("-------------------------第"+(i)+"轮开始-------------------------");
			//System.out.println("计算抗体亲和力....");
		
			for(int i1 =0;i1<abR.getAbCount();i1++){												//计算亲和力
				Antibody ab = abR.getAbR().get(i1);
				ab.computeAffinity(TRAIN);
				clonePool = ab.cloneBCell(CLONEFACTOR,R);						//简单克隆
		
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
				System.out.println("\n再次看最好抗体在验证集上的亲和力："+bestAb.computeAffinity(VALI)+"\n");
				double Map = bestAb.evaluateMap(TEST);
				String a = "\n第"+i+"代后，最优抗体在测试集上的MAP："+Map+"\n";
				System.out.println(a);
				outfile.append(a);		
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
		new Main().rankBsa();
	}
}
