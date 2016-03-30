package tyut.edu.bca;

import java.util.ArrayList;
import java.util.Iterator;

import ciir.umass.edu.learning.RankList;

public class AntigenRepertoire implements Iterable<Antigen>{
	private ArrayList<Antigen> agList ;						//一个抗原列表(抗原集）
	
	/**
	 * 构造方法，依据QueryBasedRankList，建立抗原集合
	 * @param trainTxt 
	 */
	public AntigenRepertoire(String trainTxt){
		agList = new ArrayList<Antigen>();
		QueryBasedRankList qr = new QueryBasedRankList(trainTxt);
		ArrayList<RankList> rl = qr.getRl();
		//遍历列表，同时构造抗原列表
		for(int i=0;i<rl.size();i++)
		{
			RankList r = rl.get(i);
			Antigen ag = new Antigen(Integer.parseInt(r.get(0).getID()),r);	//构造一个抗原
			agList.add(ag);													//加入到抗原列表中
		}
		
	}

	@Override
	public Iterator<Antigen> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Antigen> getAgList() {
		return agList;
	}
	
}
