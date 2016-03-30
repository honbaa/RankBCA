package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import ciir.umass.edu.utilities.FileUtils;
/**
 * ��������ʵ���������۽��
 * @author dell
 *
 */
public class AnalysisFinalResult {

	public static String getFinalResult(File mainDir) {
		String fenge = " ";
		File[] expNumfolds = mainDir.listFiles();
		//��ÿ��ʵ���µ�folds_avg.txt�Ļ�ȡ�����ַ�����
		
		int size = expNumfolds.length;//ʵ�����Ŀ¼��������ʵ��������������ļ�����size-1
		
		// ��size��folds_avg.txt�ļ�ȡ����������list
		ArrayList<String> results = new ArrayList<String>();
		
		for (int i =0;i<expNumfolds.length;i++)
		{
			if(expNumfolds[i].isFile()) {size--;continue;}
			results.add(FileUtils.read(expNumfolds[i].getAbsolutePath() + "\\folds_avg.txt", "utf-8"));
		}
			
		
		//����2��double���飬�洢p,ndcg���ۼ�,0��Ԫ����֮���á�1��double�������洢map���ۼ�
		double[] p_sum = new double[11];
		double[] ndcg_sum = new double[11];
		double map_sum = 0;
		
		//����ÿ���ַ�����Ȼ��ָ���һ���ַ������飬ȡ��1��ʼ��10��Ԫ�أ��ۼӵ�p_sum,ndcg_sum��map_sum;
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
		//��һ����precision
				String ps = metricList.get(0);
				String[] strs_p = ps.split(fenge);//���ֿո�����ͨ�ո�ͬ
				for(int k = 1;k<=10;k++)
				{
					double  p = Double.parseDouble(strs_p[k]);
					p_sum[k] = p_sum[k] + p;
				}
		//�ڶ�����Map
				String map_str = metricList.get(1);
				String[] strs_map = map_str.split(fenge);
				double  map = Double.parseDouble(strs_map[1]);
					map_sum = map_sum + map;
		//��������NDCG
					String ndcgs = metricList.get(2);
					String[] strs_ndcg = ndcgs.split(fenge);
					for(int k = 1;k<=10;k++)
					{
						double  ndcg = Double.parseDouble(strs_ndcg[k]);
						ndcg_sum[k] = ndcg_sum[k] + ndcg;
					}		
		}
		
		//��size��folds_avg.txt��ƽ��ֵ��0��Ԫ����֮����
		double[] p_avg = new double[11];
		double map_avg = map_sum/size;
		double[] ndcg_avg = new double[11];
		for(int i = 1;i<=10;i++)
		{
			p_avg[i] = p_sum[i]/size;
			ndcg_avg[i] = ndcg_sum[i]/size;
		}
		
		//��������ַ���
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
