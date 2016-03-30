package tyut.edu.utilities;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import tyut.edu.bca.QueryBasedRankList;
import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.RankList;

//Ϊ��ʹ�ùٷ��Ĳ��Թ��ߣ���ÿ��Fold�е�test���ݼ���label���ȡ���������ļ���
public class ExractLabels {
	static final String foldpath = "D:\\�ҵ�����\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\";
	static String output ="D:\\�ҵ�����\\LETOR\\OHSUMED\\labels\\";
	public static final String filter ="test.txt";
	public static void extract(){
		StringBuilder sb = new StringBuilder();
		File f = new File(foldpath);
		// ��ȡFold1-Fold5
		File[] folds = f.listFiles();
		// ����ÿ��Ŀ¼����ȡָ���ļ�
		for (int i = 0; i < folds.length; i++) {
			final File curr_fold = folds[i];
			File[] metrics = curr_fold.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					dir = curr_fold;
					if (name.equals(filter))
						return true;
					else
						return false;
				}
			});
			// ����test�ļ�����
			String testfile = metrics[0].getAbsolutePath();// ��ȡָ���ļ�·��
		  QueryBasedRankList test = new QueryBasedRankList(testfile);
		  //����testȻ�����label���ļ�
		  ArrayList<RankList> arl = test.getRl();
		  for(int j = 0; j<arl.size(); j++)
		  {
			  RankList rl = arl.get(j);
			  for(int k =0;k<rl.size();k++)
			  {
				  DataPoint dp = rl.get(k);
				  double label = dp.getLabel();
				  sb.append(label+"\n");            //��ÿ�е�labelд������
			  }
		  }
		  //��sbд���ļ�
		  String outname = output+curr_fold.getName()+".txt";
		  FileUtils.write(outname, "utf-8", sb.toString());
		}

		
	}
	
	
	public static void main(String[] args) {
		
		ExractLabels.extract();
	}
}
