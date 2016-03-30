package tyut.edu.bca;

import java.util.ArrayList;
import java.util.Iterator;

import ciir.umass.edu.learning.RankList;

public class AntigenRepertoire implements Iterable<Antigen>{
	private ArrayList<Antigen> agList ;						//һ����ԭ�б�(��ԭ����
	
	/**
	 * ���췽��������QueryBasedRankList��������ԭ����
	 * @param trainTxt 
	 */
	public AntigenRepertoire(String trainTxt){
		agList = new ArrayList<Antigen>();
		QueryBasedRankList qr = new QueryBasedRankList(trainTxt);
		ArrayList<RankList> rl = qr.getRl();
		//�����б�ͬʱ���쿹ԭ�б�
		for(int i=0;i<rl.size();i++)
		{
			RankList r = rl.get(i);
			Antigen ag = new Antigen(Integer.parseInt(r.get(0).getID()),r);	//����һ����ԭ
			agList.add(ag);													//���뵽��ԭ�б���
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
