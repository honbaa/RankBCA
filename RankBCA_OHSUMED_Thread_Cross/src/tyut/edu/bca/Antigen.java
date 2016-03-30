package tyut.edu.bca;

import ciir.umass.edu.learning.RankList;

/**
 * 一个抗原就是一个查询及其文档列表。<q,D,Y>,这样排序函数可以计算每个文档额得分，然后形成一个最终的排序列表。
 * 最后使用AP进行评价，作为抗原与抗体的亲和力。正因为评价指标是query-based的，所以抗原同样是query-based。
 * @author dell
 *
 */
public class Antigen {
	private int qid;				//查询编号
	private RankList rl;			//某个查询的相关文档列表
	
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
