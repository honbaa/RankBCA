package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import ciir.umass.edu.utilities.FileUtils;
/**
 * 分析所有实验的最后评价结果
 * @author dell
 *
 */
public class AnalysisFinalResult {

	public static String getFinalResult(File mainDir) {
		String fenge = " ";
		File[] expNumfolds = mainDir.listFiles();
		//将每个实验下的folds_avg.txt的获取放入字符串中
		
		int size = expNumfolds.length;//实验次数目录个数就是实验次数，当存在文件，将size-1
		
		// 将size个folds_avg.txt文件取出来，放入list
		ArrayList<String> results = new ArrayList<String>();
		
		for (int i =0;i<expNumfolds.length;i++)
		{
			if(expNumfolds[i].isFile()) {size--;continue;}
			results.add(FileUtils.read(expNumfolds[i].getAbsolutePath() + "\\folds_avg.txt", "utf-8"));
		}
			
		
		//定义2个double数组，存储p,ndcg的累加,0号元素弃之不用。1个double变量，存储map的累加
		double[] p_sum = new double[11];
		double[] ndcg_sum = new double[11];
		double map_sum = 0;
		
		//遍历每个字符串，然后分隔成一个字符串数组，取从1开始到10的元素，累加到p_sum,ndcg_sum，map_sum;
		for(int i = 0; i<size;i++)
		{
			String result = results.get(i);
			ArrayList<String> metricList= new ArrayList<String>();
			BufferedReader br = new BufferedReader(new StringReader(result));
			String line;
			try {
				while((line = br.readLine())!=null)
					metricList.add(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//第一行是precision
				String ps = metricList.get(0);
				String[] strs_p = ps.split(fenge);//这种空格与普通空格不同
				for(int k = 1;k<=10;k++)
				{
					double  p = Double.parseDouble(strs_p[k]);
					p_sum[k] = p_sum[k] + p;
				}
		//第二行是Map
				String map_str = metricList.get(1);
				String[] strs_map = map_str.split(fenge);
				double  map = Double.parseDouble(strs_map[1]);
					map_sum = map_sum + map;
		//第三行是NDCG
					String ndcgs = metricList.get(2);
					String[] strs_ndcg = ndcgs.split(fenge);
					for(int k = 1;k<=10;k++)
					{
						double  ndcg = Double.parseDouble(strs_ndcg[k]);
						ndcg_sum[k] = ndcg_sum[k] + ndcg;
					}		
		}
		
		//求size个folds_avg.txt的平均值，0号元素弃之不用
		double[] p_avg = new double[11];
		double map_avg = map_sum/size;
		double[] ndcg_avg = new double[11];
		for(int i = 1;i<=10;i++)
		{
			p_avg[i] = p_sum[i]/size;
			ndcg_avg[i] = ndcg_sum[i]/size;
		}
		
		//构造输出字符串
		StringBuilder out = new StringBuilder();
		out.append("Precision: ");
		for(int i = 1;i<=10;i++)
		{
			if(i!=10)
			out.append(p_avg[i]+" ");
			else
			out.append(p_avg[i]);
		}
		out.append("\n");
		out.append("MAP: "+map_avg);
		out.append("\n");
		out.append("NDCG: ");
		for(int i = 1;i<=10;i++)
		{
			if(i!=10)
			out.append(ndcg_avg[i]+" ");
			else
			out.append(ndcg_avg[i]);
		}
		
		
		return out.toString();
	}

}
