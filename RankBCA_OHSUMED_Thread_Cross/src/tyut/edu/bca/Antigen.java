package tyut.edu.bca;

import ciir.umass.edu.learning.RankList;

/**
 * һ����ԭ����һ����ѯ�����ĵ��б�<q,D,Y>,�������������Լ���ÿ���ĵ���÷֣�Ȼ���γ�һ�����յ������б�
 * ���ʹ��AP�������ۣ���Ϊ��ԭ�뿹����׺���������Ϊ����ָ����query-based�ģ����Կ�ԭͬ����query-based��
 * @author dell
 *
 */
public class Antigen {
	private int qid;				//��ѯ���
	private RankList rl;			//ĳ����ѯ������ĵ��б�
	
	public Antigen(int qid, RankList rl) {
		super();
		this.qid = qid;
		this.rl = rl;
	}

	public Antigen(RankList r) {
		this.rl = r;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public RankList getRl() {
		return rl;
	}

	public void setRl(RankList rl) {
		this.rl = rl;
	}

	
}
