package test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import ciir.umass.edu.utilities.FileUtils;

public class AvgComputer {

	public static final String DIRECTORY = "D:\\我的数据\\实验\\ousumed\\first\\";
	public static final String[] filter = { "P_File.txt", "NDCG_File.txt",
			"MAP_File.txt" };

	public static void main(String[] args) {
		// 读取给定目录Fold1,Fold2,Fold3,Fold4,Fold5下的的P_File.txt,NDCG_File.txt,MAP_File.txt

		File f = new File(DIRECTORY);
		// 获取Fold1-Fold5
		File[] folds = f.listFiles();
		// 遍历每个目录，获取指标文件，读取，计算里面的数据的平均值
		for (int i = 0; i < folds.length; i++) {
			final File curr_fold = folds[i];
			File[] metrics = curr_fold.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					dir = curr_fold;
					if (name.equals(filter[0]) || name.equals(filter[1])
							|| name.equals(filter[2]))
						return true;
					else
						return false;
				}
			});
			// 遍历3个指标文件，计算平均值
			for (int i1 = 0; i1 < metrics.length; i1++) {
				String fileType = metrics[i1].getName();// 获取指标文件名
				computeAvg(curr_fold, fileType);// 根据文件名计算均值。
			}
		}
	}

	private static void computeAvg(File curr_fold, String fileType) {
		if(fileType.equals(filter[0]))//如果是P_File.txt
		{
			String f = DIRECTORY+curr_fold.getName()+"\\"+filter[0];
			List<String> pLists = FileUtils.readLine(f, "utf-8");
			double size = pLists.size();
			double[][] pp = new double[(int) size][11];//将文件中的内容转为二维数组存储
			double[] p_avg = new double[10];
			//填充数组
			for(int i =0;i<size;i++)
			{
				String p = pLists.get(i);
				//[0.0, 0.45454545454545453, 0.4318181818181818, 0.4848484848484849, 0.4772727272727273, 0.43636363636363645, 0.42424242424242425, 0.4285714285714286, 0.42613636363636365, 0.39898989898989895, 0.3727272727272727]
				//拆分
				String[] strs = p.split(", ");//逗号空格分隔
				//将length-1元素后面的]去掉
				//将分隔后的从0到length-1元素存入二维数组的第一行
				//收尾处理
				pp[i][0] = Double.valueOf(strs[0].substring(1));
				int pos = strs[strs.length-1].indexOf("]");
				pp[i][strs.length-1] =  Double.valueOf(strs[strs.length-1].substring(0, pos));
				//中间的
				for(int j =1;j<strs.length-1;j++)			
					pp[i][j] = Double.valueOf(strs[j]);				
			}
			//计算平均值,第1列数据是0，忽略
			for(int j=1;j<11;j++)
			{
				double sum = 0;			
				for(int i1 =0;i1<(int)size;i1++)
					sum += pp[i1][j];
				p_avg[j-1] = sum/size;	
				
			}
			System.out.println(curr_fold.getName()+" p_avg "+Arrays.toString(p_avg));			
			//永久保存
			FileUtils.write("D:\\我的数据\\实验\\ousumed\\first\\"+curr_fold.getName()+"\\P_AVG.txt", "utf-8",Arrays.toString(p_avg));
		}else if(fileType.equals(filter[1]))//如果是NDCG_File.txt
		{
			String f = DIRECTORY+curr_fold.getName()+"\\"+filter[1];
			List<String> ndcgLists = FileUtils.readLine(f, "utf-8");
			double size = ndcgLists.size();
			double[][] pp = new double[(int) size][10];//将文件中的内容转为二维数组存储
			double[] ndcg_avg = new double[10];
			//填充数组
			for(int i =0;i<size;i++)
			{
				String p = ndcgLists.get(i);
				//[ 0.45454545454545453, 0.4318181818181818, 0.4848484848484849, 0.4772727272727273, 0.43636363636363645, 0.42424242424242425, 0.4285714285714286, 0.42613636363636365, 0.39898989898989895, 0.3727272727272727]
				//拆分
				String[] strs = p.split(", ");//逗号空格分隔
				//将length-1元素后面的]去掉
				//将分隔后的从0到length-1元素存入二维数组的第一行
				//收尾处理
				pp[i][0] = Double.valueOf(strs[0].substring(1));
				int pos = strs[strs.length-1].indexOf("]");
				pp[i][strs.length-1] =  Double.valueOf(strs[strs.length-1].substring(0, pos));
				//中间的
				for(int j =1;j<strs.length-1;j++)			
					pp[i][j] = Double.valueOf(strs[j]);				
			}
			//计算平均值,第1列数据是0，忽略
			for(int j=0;j<10;j++)
			{
				double sum = 0;			
				for(int i1 =0;i1<(int)size;i1++)
					sum += pp[i1][j];
				ndcg_avg[j] = sum/size;	
				
			}
			System.out.println(curr_fold.getName()+" ndcg_avg "+Arrays.toString(ndcg_avg));			
			//永久保存
		FileUtils.write("D:\\我的数据\\实验\\ousumed\\first\\"+curr_fold.getName()+"\\NDCG_AVG.txt", "utf-8",Arrays.toString(ndcg_avg));
		}else if(fileType.equals(filter[2]))//如果是MAP_File.txt
		{
			String f = DIRECTORY+curr_fold.getName()+"\\"+filter[2];
			List<String> maps = FileUtils.readLine(f, "utf-8");
			double map_avg = 0;
			double size = maps.size();
			double sum = 0;
			for(String map :maps)
			{
				double d = Double.parseDouble(map);
				sum = sum + d;
			}
			map_avg = sum/size;
			System.out.println(curr_fold.getName()+" map_avg "+String.valueOf(map_avg));
			//永久保存
			FileUtils.write("D:\\我的数据\\实验\\ousumed\\first\\"+curr_fold.getName()+"\\MAP_AVG.txt", "utf-8",String.valueOf(map_avg));
		}
		
	}
}
