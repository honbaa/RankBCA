package tyut.edu.bca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tyut.edu.utilities.FileUtils;
import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.RankList;
import ciir.umass.edu.learning.SparseDataPoint;
/**
 * �����������ǽ�ѵ�����ݣ����ղ�ѯΪ��λ�ָ������طָ���RankList���б�
 * @author dell
 *
 */
public class QueryBasedRankList {
	
	private  ArrayList<RankList> rl ;																				 //�Բ�ѯΪ��λ���б�
	//public static final String TRAIN_PATH="D:\\�ҵ�����\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm\\Fold1\\train.txt";	 //ѵ�����ݵ�λ��
	
	
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
		//*****************************����һ�����ݵ��б�****************************
		for(int i=0;i<s.size();i++)
			{
			SparseDataPoint sp = new SparseDataPoint(s.get(i)) ;	//ÿһ�й���һ��DataPoint		
		//	System.out.println(sp.getID()+" "+Arrays.toString(sp.getfVals()));
			dplist.add(sp);			
			}
		//*****************************�����ǽ����еĵ㰴�ղ�ѯ���飬��ѯ��ͬ��Ϊһ���顣*********************
		int begin=0,end =0;//2�������±�
		for(int i=0 ;i<dplist.size();i++)
		{
			if(i==dplist.size()-1) continue;							//���������һ���±�
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
		ArrayList<DataPoint> dl = new ArrayList<DataPoint>();			//���������һ���±����Ҫ��ʣ�µķ�Ϊһ��
		for(int j = begin;j<=end;j++)
			dl.add(dplist.get(j));
		RankList rl1 = new RankList(dl);
		rl.add(rl1);	
		//*******************************************************************************************************		
	}
	
	
	
}
