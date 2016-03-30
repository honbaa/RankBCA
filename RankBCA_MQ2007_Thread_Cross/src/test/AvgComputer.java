package test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import ciir.umass.edu.utilities.FileUtils;

public class AvgComputer {

	public static final String DIRECTORY = "D:\\�ҵ�����\\ʵ��\\ousumed\\first\\";
	public static final String[] filter = { "P_File.txt", "NDCG_File.txt",
			"MAP_File.txt" };

	public static void main(String[] args) {
		// ��ȡ����Ŀ¼Fold1,Fold2,Fold3,Fold4,Fold5�µĵ�P_File.txt,NDCG_File.txt,MAP_File.txt

		File f = new File(DIRECTORY);
		// ��ȡFold1-Fold5
		File[] folds = f.listFiles();
		// ����ÿ��Ŀ¼����ȡָ���ļ�����ȡ��������������ݵ�ƽ��ֵ
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
			// ����3��ָ���ļ�������ƽ��ֵ
			for (int i1 = 0; i1 < metrics.length; i1++) {
				String fileType = metrics[i1].getName();// ��ȡָ���ļ���
				computeAvg(curr_fold, fileType);// �����ļ��������ֵ��
			}
		}
	}

	private static void computeAvg(File curr_fold, String fileType) {
		if(fileType.equals(filter[0]))//�����P_File.txt
		{
			String f = DIRECTORY+curr_fold.getName()+"\\"+filter[0];
			List<String> pLists = FileUtils.readLine(f, "utf-8");
			double size = pLists.size();
			double[][] pp = new double[(int) size][11];//���ļ��е�����תΪ��ά����洢
			double[] p_avg = new double[10];
			//�������
			for(int i =0;i<size;i++)
			{
				String p = pLists.get(i);
				//[0.0, 0.45454545454545453, 0.4318181818181818, 0.4848484848484849, 0.4772727272727273, 0.43636363636363645, 0.42424242424242425, 0.4285714285714286, 0.42613636363636365, 0.39898989898989895, 0.3727272727272727]
				//���
				String[] strs = p.split(", ");//���ſո�ָ�
				//��length-1Ԫ�غ����]ȥ��
				//���ָ���Ĵ�0��length-1Ԫ�ش����ά����ĵ�һ��
				//��β����
				pp[i][0] = Double.valueOf(strs[0].substring(1));
				int pos = strs[strs.length-1].indexOf("]");
				pp[i][strs.length-1] =  Double.valueOf(strs[strs.length-1].substring(0, pos));
				//�м��
				for(int j =1;j<strs.length-1;j++)			
					pp[i][j] = Double.valueOf(strs[j]);				
			}
			//����ƽ��ֵ,��1��������0������
			for(int j=1;j<11;j++)
			{
				double sum = 0;			
				for(int i1 =0;i1<(int)size;i1++)
					sum += pp[i1][j];
				p_avg[j-1] = sum/size;	
				
			}
			System.out.println(curr_fold.getName()+" p_avg "+Arrays.toString(p_avg));			
			//���ñ���
			FileUtils.write("D:\\�ҵ�����\\ʵ��\\ousumed\\first\\"+curr_fold.getName()+"\\P_AVG.txt", "utf-8",Arrays.toString(p_avg));
		}else if(fileType.equals(filter[1]))//�����NDCG_File.txt
		{
			String f = DIRECTORY+curr_fold.getName()+"\\"+filter[1];
			List<String> ndcgLists = FileUtils.readLine(f, "utf-8");
			double size = ndcgLists.size();
			double[][] pp = new double[(int) size][10];//���ļ��е�����תΪ��ά����洢
			double[] ndcg_avg = new double[10];
			//�������
			for(int i =0;i<size;i++)
			{
				String p = ndcgLists.get(i);
				//[ 0.45454545454545453, 0.4318181818181818, 0.4848484848484849, 0.4772727272727273, 0.43636363636363645, 0.42424242424242425, 0.4285714285714286, 0.42613636363636365, 0.39898989898989895, 0.3727272727272727]
				//���
				String[] strs = p.split(", ");//���ſո�ָ�
				//��length-1Ԫ�غ����]ȥ��
				//���ָ���Ĵ�0��length-1Ԫ�ش����ά����ĵ�һ��
				//��β����
				pp[i][0] = Double.valueOf(strs[0].substring(1));
				int pos = strs[strs.length-1].indexOf("]");
				pp[i][strs.length-1] =  Double.valueOf(strs[strs.length-1].substring(0, pos));
				//�м��
				for(int j =1;j<strs.length-1;j++)			
					pp[i][j] = Double.valueOf(strs[j]);				
			}
			//����ƽ��ֵ,��1��������0������
			for(int j=0;j<10;j++)
			{
				double sum = 0;			
				for(int i1 =0;i1<(int)size;i1++)
					sum += pp[i1][j];
				ndcg_avg[j] = sum/size;	
				
			}
			System.out.println(curr_fold.getName()+" ndcg_avg "+Arrays.toString(ndcg_avg));			
			//���ñ���
		FileUtils.write("D:\\�ҵ�����\\ʵ��\\ousumed\\first\\"+curr_fold.getName()+"\\NDCG_AVG.txt", "utf-8",Arrays.toString(ndcg_avg));
		}else if(fileType.equals(filter[2]))//�����MAP_File.txt
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
			//���ñ���
			FileUtils.write("D:\\�ҵ�����\\ʵ��\\ousumed\\first\\"+curr_fold.getName()+"\\MAP_AVG.txt", "utf-8",String.valueOf(map_avg));
		}
		
	}
}
