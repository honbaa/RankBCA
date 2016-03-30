package tyut.edu.bca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tyut.edu.utilities.FileUtils;
import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.RankList;
import ciir.umass.edu.learning.SparseDataPoint;
/**
 * 这个类的作用是将训练数据，按照查询为单位分隔，返回分割后的RankList的列表
 * @author dell
 *
 */
public class QueryBasedRankList {
	
	private  ArrayList<RankList> rl ;																				 //以查询为单位的列表
	//public static final String TRAIN_PATH="D:\\我的数据\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\train.txt";	 //训练数据的位置
	
	
	public ArrayList<RankList> getRl() {
		return rl;
	}
	public void setRl(ArrayList<RankList> rl) {
		this.rl = rl;
	}
	
	public QueryBasedRankList(String trainTxt){
		List<String> s = FileUtils.readLine(trainTxt, "utf-8");
		rl = new ArrayList<RankList>();
		ArrayList<DataPoint> dplist = new ArrayList<DataPoint>();
		//*****************************构建一个数据点列表****************************
		for(int i=0;i<s.size();i++)
			{
			SparseDataPoint sp = new SparseDataPoint(s.get(i)) ;	//每一行构建一个DataPoint		
		//	System.out.println(sp.getID()+" "+Arrays.toString(sp.getfVals()));
			dplist.add(sp);			
			}
		//*****************************下面是将所有的点按照查询分组，查询相同的为一个组。*********************
		int begin=0,end =0;//2个辅助下标
		for(int i=0 ;i<dplist.size();i++)
		{
			if(i==dplist.size()-1) continue;							//到达了最后一个下标
			int former = Integer.parseInt(dplist.get(i).getID());
			int latter = Integer.parseInt(dplist.get(i+1).getID());
			if(former == latter)
			{
				end++;
				continue;
			}
			else
			{
				ArrayList<DataPoint> dl = new ArrayList<DataPoint>();
				for(int j = begin;j<=end;j++)
					dl.add(dplist.get(j));
				RankList rl1 = new RankList(dl);
				rl.add(rl1);
				begin= end+1;
				end=begin;
			}			

		}
		ArrayList<DataPoint> dl = new ArrayList<DataPoint>();			//到达了最后一个下标后，需要将剩下的分为一组
		for(int j = begin;j<=end;j++)
			dl.add(dplist.get(j));
		RankList rl1 = new RankList(dl);
		rl.add(rl1);	
		//*******************************************************************************************************		
	}
	
	
	
}
