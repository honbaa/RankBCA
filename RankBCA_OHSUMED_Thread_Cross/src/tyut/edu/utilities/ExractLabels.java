package tyut.edu.utilities;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import tyut.edu.bca.QueryBasedRankList;
import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.RankList;

//为了使用官方的测试工具，将每个Fold中的test数据集的label提出取来，存入文件中
public class ExractLabels {
	static final String foldpath = "D:\\我的数据\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\";
	static String output ="D:\\我的数据\\LETOR\\OHSUMED\\labels\\";
	public static final String filter ="test.txt";
	public static void extract(){
		StringBuilder sb = new StringBuilder();
		File f = new File(foldpath);
		// 获取Fold1-Fold5
		File[] folds = f.listFiles();
		// 遍历每个目录，获取指标文件
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
			// 将该test文件读入
			String testfile = metrics[0].getAbsolutePath();// 获取指标文件路径
		  QueryBasedRankList test = new QueryBasedRankList(testfile);
		  //遍历test然后输出label到文件
		  ArrayList<RankList> arl = test.getRl();
		  for(int j = 0; j<arl.size(); j++)
		  {
			  RankList rl = arl.get(j);
			  for(int k =0;k<rl.size();k++)
			  {
				  DataPoint dp = rl.get(k);
				  double label = dp.getLabel();
				  sb.append(label+"\n");            //将每行的label写入流中
			  }
		  }
		  //将sb写入文件
		  String outname = output+curr_fold.getName()+".txt";
		  FileUtils.write(outname, "utf-8", sb.toString());
		}

		
	}
	
	
	public static void main(String[] args) {
		
		ExractLabels.extract();
	}
}
