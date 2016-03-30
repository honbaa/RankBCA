package tyut.edu.bca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import test.ParallelRankBCAClient;
import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.RankList;
import ciir.umass.edu.metric.APScorer;
import ciir.umass.edu.metric.PrecisionScorer;
import ciir.umass.edu.utilities.Sorter;

public class Antibody implements Comparable<Antibody>,Cloneable,Serializable{
	private static final long serialVersionUID = 2631590509760908280L; 
	private double affinity = 0;																					//亲和力
	public static final double ADD = 100,MIN=200,MUL=300,DIV=400;													//表示运算符类型	
	public static double[] op = {ADD,MIN,MUL,DIV};																	//运算符数组（全局）
	private static double[] constant={0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10};	//常数数组（全局）															//常数数组
	private double[] features ;																						//特征值数组
	private static final int FEATRUE_COUNT=45;//ohsumed中的特征
	private static int HEIGHT = 7;																					//树的高度
	//*****注意对象类型的克隆*****
	private Node root;																								//树的根
	private Node[] leaves;																							//树的叶子
	//后面的克隆池
	AntibodyRepertoire pool;
	
	/**
	 * @return the pool
	 */
	public AntibodyRepertoire getPool() {
		return pool;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(AntibodyRepertoire pool) {
		this.pool = pool;
	}

	//**********
	private int innerTotal;																							//树的内部节点总数
	private int temp;//辅助用的，作为数组下标用，无其他实际意义。
	private int temp2;
	
	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public int getTemp2() {
		return temp2;
	}

	public void setTemp2(int temp2) {
		this.temp2 = temp2;
	}

	//******这个数组和index为fillNodsArray()方法服务,同样初始化后填充*****
	private Node[] allNodes; //allNods数组只是保存另一份树中节点的引用
	//index是当前Node[]的下标
	private int index;
	
	public Node[] getAllNodes() {
		return allNodes;
	}

	public void setAllNodes(Node[] allNodes) {
		this.allNodes = allNodes;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double[] getFeatures() {
		return features;
	}

	public Node[] getLeaves() {
		return leaves;
	}

	public void setLeaves(Node[] leaves) {
		this.leaves = leaves;
	}

	public void setFeatures(double[] features) {
		this.features = features;
		//要将这些特征值填充进相应的特征节点value中去。才能计算出分数，否则value的值是0！	
		
		//遍历allNodes数组，获取特征节点，判断其特征编号，将对应的特征编号加入
		for(int i=0;i<allNodes.length;i++)
		{
			if(allNodes[i].isFeature()== true)
			{
				int fid = allNodes[i].getFeatureId();//获取特征节点id
				allNodes[i].setValue(features[fid]);//给特征节点赋予对应特征值
			}
		}
		
		
		
		
		
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public int getInnerTotal() {
		return innerTotal;
	}

	public void setInnerTotal(int innerTotal) {
		this.innerTotal = innerTotal;
	}

	/**
	 * 树的节点类型
	 * @author dell
	 *
	 */
	public  class Node implements Serializable{
		private static final long serialVersionUID = 1267293988171991494L; 
		double value;																						//节点的值，内部节点使用ADD等常量
		Node left;
		Node right;
		int num;																							//节点编号
		boolean isFeature = false;//用来表示节点是否为特征节点
		int featureId;			 //用来表示节点的特征编号,使用前，必须使用isFeatrue()方法进行判断后方可使用
		
		
		
		public boolean isFeature() {
			return isFeature;
		}
		public void setFeature(boolean isFeature) {
			this.isFeature = isFeature;
		}
		public int getFeatureId() {
			return featureId;
		}
		public void setFeatureId(int featureId) {
			this.featureId = featureId;
		}
		public Node(int num,double v){
			this.value = v;
			this.num = num;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public double getValue() {
			return value;
		}
		public void setValue(double value) {
			this.value = value;
		}
		/**
		 * 克隆节点。经测试，失败。使用序列化
		 */
		/*@Override  
	    public Object clone() throws CloneNotSupportedException {  
			Node newn = (Node) super.clone();
			if(left!=null)
			newn.left = (Node) this.left.clone();
			if(right!=null)
			newn.right = (Node) this.right.clone();
	       return newn;  
	    }  */
		public Node getLeft() {
			return left;
		}
		public void setLeft(Node left) {
			this.left = left;
		}
		public Node getRight() {
			return right;
		}
		public void setRight(Node right) {
			this.right = right;
		}
	 
		
	}
	//构造方法构造一个树结构，里面初始化了随机的运算符和常数，但是特征节点的value值全都是默认的0！
	//在计算一个文档得分的时候，需要将特征值加入到特征节点的value中！调用setFeatrues方法即可。
	public Antibody(){
		features = new double[FEATRUE_COUNT];							
		this.innerTotal = (int) (Math.pow(2,HEIGHT-1)-1);				//所有内部节点个数
		initializeLeaves();												//初始化叶子
		this.root = recurCreateFullBiTree(1);							//创建内部节点树		
		createLeaves(this.root);										//创建叶子（挂载）
		this.allNodes=new Node[innerTotal+leaves.length];				//直到fillNodesArray调用，不会填充
		fillNodesArray(root);											//中序遍历树节点的引用，保存到这个数组
	};
	/**
	 * 构造方法
	 * @param features特征向量值，亲和力默认为0，操作符和常数默认
	 */
	public Antibody(double[] features) {
		super();
		this.features = features;
		this.innerTotal = (int) (Math.pow(2,HEIGHT-1)-1);				//所有内部节点个数
		initializeLeaves();												//初始化叶子
		this.root = recurCreateFullBiTree(1);							//创建内部节点树		
		createLeaves(this.root);										//创建叶子（挂载）
	}
	

	/**
	 * 初始化随机的，包含所有且不重复特征的叶子列表
	 *  采取的策略为：
	 * 首先，生成所有的叶子节点列表
	 * 其次，将所有特征不重复按顺序加入到叶子列表中
	 * 最后，将所有的未填写的叶子从常数数组中随机填写 
	 * 这样形成的一个初始叶子列表，不重复随机抽取节点形成新列表并赋值给leaves。
	 */
	private void initializeLeaves() {
		leaves = new Node[(int) Math.pow(2,HEIGHT-1)];
		Node[] temp = new Node[(int) Math.pow(2,HEIGHT-1)];				//临时叶子数组
		for(int i =0;i<features.length;i++){							//特征节点
			leaves[i] = new Node(0,features[i]);						//将Featrues加入，编号还没定，要等挂载到树才能定
			leaves[i].setFeature(true);	//这个很重要，它表示了该节点是特征节点
			leaves[i].setFeatureId(i);	//这个也很重要，它表示了该特征的id
		}
		for(int i = features.length;i<(int) Math.pow(2,HEIGHT-1);i++)	//常数节点
		{
			int index = (int)(Math.random() * constant.length);			//随机产生下标
			leaves[i] = new Node(0,constant[index]);					//加入后面的节点
		}		
		//要保证不重复的选，选择随机产生的下标元素，然后将该元素替换为数组最后一个元素，并将数组长度减1.
		int length = leaves.length;
	    for (int i = 0; i < leaves.length; i++)
	    {
	      int index = (int)(Math.random() * length);//产生下标
	      temp[i] = leaves[index];					//第i个随机节点
	//System.out.print(" " + leaves[index].getValue());
	      leaves[index] = leaves[length - 1];
	      length--;
	    }   
	    leaves = temp;			//leaves最后是一个随机的列表了，并且包含所有不重复featrue，常数是随机的。
	}

	/**
	 * 创建一个内部节点的满二叉树
	 * 1.先建立内部节点。节点的value随机选中运算符数组中的一个。
	 * 2.再在末尾加入叶子节点。
	 * 
	 * @return 内部节点树的根
	 */
	private Node recurCreateFullBiTree(int num) {
			int index = (int)(Math.random() * op.length);//随机产生下标
		    Node rootNode = new Node(num,op[index]);	//根节点  
	       //--------先建立内部节点，节点数量为2^(HEIGHT-1)-1个--------
		    //如果有左子树则创建左子树  
	        if ( num * 2 < innerTotal) {  
	            rootNode.left = recurCreateFullBiTree(num * 2);  
	            //如果还可以创建右子树，则创建  
	            if (num * 2 + 1 <= innerTotal) {  
	                rootNode.right = recurCreateFullBiTree(num * 2 + 1);  
	            }  
	        }  
	      return  rootNode;  		
	}
	/**
	 * 给定内部节点的根，建立其所有的叶子节点
	 * @param rootNode 内部节点树的根	
	 */
	private void createLeaves(Node root) {
		
		if(root.left ==null && root.right ==null)
		{
			
			root.left = getARandomLeaf(root.num*2,temp++);//temp作为一个临时的，表示下标
			root.right = getARandomLeaf(root.num*2+1,temp++);
		}else
		{
			createLeaves(root.left);
			createLeaves(root.right);
		}
		
	}	
	/**
	 * 先序遍历树，填充到一个数组中
	 * @param root树根
	 */
	private void fillNodesArray(Node root) {
			if (root != null) {
				allNodes[index++] = root;		//allNods数组只是保存另一份树中节点的引用
				fillNodesArray(root.left);
				fillNodesArray(root.right);
		}
	}

	/**
	 * 从已经随机的叶子列表中顺序选择节点，并赋予树中编号
	 * @param j 给予节点的编号
	 * @param temp2 下标
	 * @return 返回节点
	 */	
	private Node getARandomLeaf(int j, int temp2) {
		Node n = leaves[temp2];
		n.setNum(j);
		return n;
	}

	public double getAffinity() {
		return affinity;
	}

	public void setAffinity(double affinity) {
		this.affinity = affinity;
	}

	/**
	 * 计算抗体与抗原集合的平均亲和力,使用指标MAP。
	 * @param aGR 抗原集
	 */
	public double computeAffinity(AntigenRepertoire agR) {
		ArrayList<Antigen> aglist = agR.getAgList();
		double map = 0;
		int numQuery = aglist.size();				//查询个数
		double sum = 0;
		for(int i = 0;i<numQuery;i++)
		{
			double ap = computeAffinity(aglist.get(i));
			/*System.out.println();
			System.out.println("query id为"+i+"的ap:"+ap);*/
			sum += ap;
		}
		map = sum/numQuery;
		/*System.out.println("sum :"+sum);
		System.out.println("numQuery :"+numQuery);
		System.out.println("MAP为：");
		System.out.println(map);*/
		affinity = map;									//给抗体的affinity属性赋值
		return map;
	}

	/**
	 * 计算抗体与某个抗原的亲和力.即，对排序函数预测的一个查询的文档列表进行评价，使用指标AP。
	 * @param ag 某个抗原
	 * @return 返回亲和力
	 */
	public double computeAffinity(Antigen ag) {
		Antigen orderedag = orderAntigen(ag);
		//对排序函数预测的RankList进行评价
		APScorer s = new APScorer();	
		return s.score(orderedag.getRl());
	}
	/**
	 * 对抗原里面的每个文档，使用排序函数计算它的得分
	 * @param ag
	 * @return
	 */
	public double[] computeScores(Antigen ag) {
		RankList rl  = ag.getRl();
		double[] scores = new double[ag.getRl().size()] ;						//保存该查询的所有文档的分数
		for(int i=0;i<rl.size();i++)
		{
			DataPoint dp = rl.get(i);
			float[] fvals = dp.getfVals();
		//System.out.println("id="+dp.getID()+" features:"+Arrays.toString(fvals));
			setFeatures(convertFloatsToDoubles(fvals));
			//updateLeaves(root);													//将叶子对应编号的特征的值，更新为当前features对应特征编号的值
			//trav(root);
			scores[i] = ranker(root);											//将该位置的文档的得分放在相应的位置
		}
		return scores;
	}

	

	public double[] computeScores(RankList rl) {
		double[] scores = new double[rl.size()] ;						//保存该查询的所有文档的分数
		for(int i=0;i<rl.size();i++)
		{
			DataPoint dp = rl.get(i);
			float[] fvals = dp.getfVals();
		//System.out.println("id="+dp.getID()+" features:"+Arrays.toString(fvals));
			setFeatures(convertFloatsToDoubles(fvals));
			//updateLeaves(root);														//将叶子对应编号的特征的值，更新为当前features对应特征编号的值
			//trav(root);
			scores[i] = ranker(root);											//将该位置的文档的得分放在相应的位置
		}
		return scores;
		
	}

	/**
	 * 更新特征节点的值
	 */
	public void updateLeaves(Node root) {
		if(root!=null){
			if(root.isFeature==true)
			{
				int id = root.getFeatureId();
				root.setValue(features[id]);//更新节点的值
			}else
			{
				updateLeaves(root.left);
				updateLeaves(root.right);
			}
		}
	}

	/**
	 * 候选排序函数（抗体）。能够计算某个文档对的相关性得分。得分可以是负值。
	 * @param root
	 * @return
	 */
	public double ranker(Node root){
		
			if(root.value == ADD)							//加法
				return  ranker(root.left)+ranker(root.right);
			if(root.value == MIN)							//减法
				return  ranker(root.left)-ranker(root.right);
			if(root.value == MUL)							//乘法
				return  ranker(root.left)*ranker(root.right);
			if(root.value == DIV)							//除法
				return  ranker(root.left)/(ranker(root.right)+0.001);
			else
				return root.value;
	}
	public void trav(Node root) {
		if (root != null) {
			
			trav(root.left);
			System.out.println(root.num + " " + root.value+"isFearture:                            "+root.isFeature());
			trav(root.right);
		}
	}
	/**
	 * 克隆抗体
	 * @param clonefactor克隆因子
	 * @param n选择克隆的抗体数量
	 * @return 克隆后的抗体群
	 */
	public AntibodyRepertoire onClone(double clonefactor,int n) {
		ArrayList<Antibody> abl = new ArrayList<Antibody>();
		//克隆数量
		int num = 0;
		num = (int)(clonefactor*affinity*n);
	//	System.out.println("这个抗体克隆个数："+num);
		for(int i =0;i<num;i++)
		{
			Antibody ab = CloneUtils.clone(this);
			//克隆后，将辅助的下标temp,temp2都设置为0
			ab.setTemp(0);
			ab.setTemp2(0);
			abl.add(ab);
		}
		AntibodyRepertoire abrclone = new AntibodyRepertoire(abl);
		return abrclone;
		
	}
	/**
	 * 用于BCA的克隆。克隆个数为种群大小。
	 * @param clonefactor是种群大小的乘数因子
	 * @param n是种群大小
	 */
	public AntibodyRepertoire cloneBCell(double clonefactor,int n) {
		ArrayList<Antibody> abl = new ArrayList<Antibody>();
		//克隆数量
		int num = 0;
		num = (int)(clonefactor*n);
	//	System.out.println("这个抗体克隆个数："+num);
		for(int i =0;i<num;i++)
		{
			Antibody ab = CloneUtils.clone(this);
			//克隆后，将辅助的下标temp,temp2都设置为0
			ab.setTemp(0);
			ab.setTemp2(0);
			abl.add(ab);
		}
		AntibodyRepertoire abrclone = new AntibodyRepertoire(abl);
		return abrclone;
		
	}
	
	
	
	/**
	 * 抗体超变异
	 * @param mutationactor变异因子
	 */
	public void hypermutete(double mutationactor) {
		//下面的这种实现将变异因子理解为固定的变异百分比
		//首先从将抗体库中的抗体按照亲和力排序
		
		
	}

	/**
	 * 单变异。变异策略如下：
	 * 1.如果是叶子：
	 * 		如果是特征，那么随机选择另一个叶子特征，呼唤特征编号。
	 * 		如果是常数，那么随机选择另一个常数，替换值
	 * 2.如果是内部节点，那么随机选择另一个运算符，替换值。
	 */
	public void singleMutate(){
		Node n = randomNode();
		//System.out.println(n.getNum()+":"+n.isFeature+":"+n.getValue());
		//如果是叶子
		if(n.left == null)
		{
			//System.out.println("变异的是叶子");
			if(n.isFeature()==true)
			{
				//System.out.println("变异的是特征");
				Node newNode = randomFeatrueNode();
				int newid = newNode.getFeatureId();
				int id = n.getFeatureId();
				newNode.setFeatureId(id);
				n.setFeatureId(newid);
			}else
			{
				//System.out.println("变异的是常数");
				double newValue = randomConstant();
				if(n.getValue() == newValue)
					newValue = randomConstant();
				else
					n.setValue(newValue);
			}
		}else//运算符
		{
			//System.out.println("变异的是运算符");
			double op = randomOperator();
			if(op == n.getValue())
				op = randomOperator();
			else
				n.setValue(op);
		}
	}
	/**
	 * 多变异，以子树的方式替换。
	 * 需要保证2点：1.替换后的节点编号要与符合树整个编号顺序。2.替换子树的特征与被替换子树的特征完全相同且不重复，新子树特征节点
	 * 排列是随机的。 
	 * 策略：
	 * 1.随机选取抗体的一个内部节点n。获取高度h
	 * 2.获取该节点为根的特征子节点f[]。这样做是为了将保留这些特征节点，将其随机挂载进新的子树上，这样整个抗体的特征仍然不重复且完全覆盖所有特征。
	 * 3.创建h-1高度的子树t
	 * 4.创建包含f[]的叶子节点l[]（个数为高度为h的叶子节点数目，即第h-1层的下一层）.
	 * 5.随机选取l[]中的叶子，挂载到t上面。
	 * 6.用t替换n。不能是仅仅的n=t.这样不会更新原来的子树，只是更新了变量n而已。
	 */
	public void multiMutate(int layer){
		Node subtree = randomInnerNode(layer);//获取某一层的内部节点
		ArrayList<Node> featrues = getFeatrues(subtree);//获取内部节点为根的子树的所有特征叶子节点。
		int height = getHeight(subtree);//获取子树的高度
		//System.out.println("变异树的高度："+height+"\n");
		int num = subtree.getNum();//获取根的编号
		Node t = createAsubTree(height-1,num);//创建h-1高度子树
		Node[] leaves =	createAllLeaves(featrues,height);//一个包含给定特征，填充叶子
		createLeaves(t,leaves);//将叶子按顺序挂上
		//以上，一颗子树就建好了。
		replaceSubTree(subtree,t);
	}
	
	public void multiMutateNOlimit(int layer){		
		Node subtree = randomInnerNode(layer);//获取某一层的内部节点
		int height = getHeight(subtree);//获取子树的高度
		int num = subtree.getNum();//获取根的编号
		Node t = createAsubTree(height-1,num);//创建h-1高度子树
		Node[] leaves =	createAllLeavesNoLimit(height);//叶子上的节点类型不受限制。
		createLeaves(t,leaves);//将叶子按顺序挂上
		replaceSubTree(subtree,t);
	}
		
	
	
	 private Node[] createAllLeavesNoLimit(int height) {
		 Node[] ls = new Node[(int) Math.pow(2,height-1)];				//临时叶子数组
		 double[] allone = new double[constant.length+FEATRUE_COUNT]; //合并一下特征和常数，从这里随机选择
		 //第一部分是常数，下标范围是【0-constant.length-1]
		 for(int i = 0;i<constant.length;i++)
			 allone[i] = constant[i];
		 //第二部分是特征下标
		 for(int i = constant.length;i<constant.length+FEATRUE_COUNT;i++)
			 allone[i] = i-constant.length;									
		for(int i = 0;i<ls.length;i++)//所有元素全是随机，不管是否重复
		{
			 int index = (int)(Math.random() * allone.length);			//随机产生下标
			 if(index>=0 && index<=constant.length-1)//选了一个常数节点
				 ls[i] = new Node(0,allone[index]);
			 else
			 {
				ls[i] = new Node(0,allone[index]);//特征下标做值，特征编号是随机的啊，不受限制的
				ls[i].setFeature(true);
				ls[i].setFeatureId((int) allone[index]);
			 }
		}
		return ls;
				
	}

	/**
	  * 使用右边的子树替换左边的子树。策略：
	  * 1.从父节点进行判断，根据节点的编号进行。
	  * 2.让这个原指针指向t
	  * 3.这个过程需要遍历root
	  * @param subtree被替换子树
	  * @param t替换子树
	  */
		public  void replaceSubTree(Node subtree, Node t) {
		int num = subtree.getNum();//把编号取出来用于对比
		if(this.root.getNum() == num)
			this.root = t;
		else 
			traverseReplace(this.root,subtree,t);
		}

/**
 * 遍历树，寻找成功后替换
 * @param r子树根
 * @param subtree待替换子树
 * @param t替换树
 * @return 是否成功
 */
		private boolean traverseReplace(Node r, Node subtree, Node t) {
			if(r.left!=null)
			{
				if(replace(r,subtree,t)== true)
					return true;//替换后终止
				else
				{
					if(traverseReplace(r.left,subtree,t)==true)
						return true;
					if(traverseReplace(r.right,subtree,t)==true)
						return true;
				}				
			}else
				return false;
			return false;
	}


/**
 * 在3个节点的小树中判断，符合则替换
 * @param r
 * @param subtree
 * @param t
 */
private boolean replace(Node r, Node subtree, Node t) {
		if(r.left.getNum() == subtree.getNum())
		{	r.setLeft(t);return true;}
		else
			if(r.right.getNum()==subtree.getNum())
				{r.setRight(t);return true;}
			else
				return false;
		
	}

/**
 * 将叶子数组挂载到树
 * @param t树根
 * @param ls叶子数组
 * @param idx叶子数组下标
 */
public  void createLeaves(Node t, Node[] ls) {
	if(t.left ==null && t.right ==null)
	{
		Node l  = ls[temp2++];
		l.setNum(2*t.num);
		t.setLeft(l);;
		Node r = ls[temp2++];
		r.setNum(2*t.num+1);
		t.setRight(r);//右子树居然忘记设置了！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
	}else
	{
		createLeaves(t.left,ls);
		createLeaves(t.right,ls);
	}
		
	}

/**
 * 返回一个随机排列的叶子。随机排列值得是制定特征的排列以及随机常数。与之前那个初始化方法相似。照着那个进行相应改变。
 * @param fs特征列表
 * @param height树高度
 * @param ls要填充的叶子数组
 */
	public Node[] createAllLeaves(ArrayList<Node> fs,int height) {	
		Node[] ls = new Node[(int) Math.pow(2,height-1)];				//临时叶子数组
		Node[] temp = new Node[(int) Math.pow(2,height-1)];				//临时叶子数组
		for(int i =0;i<fs.size();i++){									//特征节点
			ls[i] = fs.get(i);											//将特征节点加入
	    }
		for(int i = fs.size();i<(int) Math.pow(2,height-1);i++)	//常数节点
		{
			int index = (int)(Math.random() * constant.length);			//随机产生下标
			ls[i] = new Node(0,constant[index]);					//加入后面的节点
		}		
		//要保证不重复的选，选择随机产生的下标元素，然后将该元素替换为数组最后一个元素，并将数组长度减1.
		int length = ls.length;
	    for (int i = 0; i < ls.length; i++)
	    {
	      int index = (int)(Math.random() * length);//产生下标
	      temp[i] = ls[index];					    //第i个随机节点
	//System.out.print(" " + leaves[index].getValue());
	      ls[index] = ls[length - 1];
	      length--;
	    }   
	    //ls =temp;			//leaves最后是一个随机的列表了，并且包含所有不重复featrue，常数是随机的。
	  /*  for(Node l:temp)
			System.out.print(" ["+l.getNum()+":"+l.getValue()+"]"+"feature："+l.isFeature());*/
	    return temp;
	}

	/**
	 * 创建一个子树
	 * @param height子树的高度
	 * @param num根节点的起始编号
	 * @return
	 */
	public Node createAsubTree(int height,int num) {
		int index = (int)(Math.random() * op.length);//随机产生下标		
		Node rootNode = new Node(num,op[index]);	//根节点
		//如果高度大于1，创建左右子树
		if(height>1)
		{
		int nextheight =height-1;
	    int leftnum = 2*num;
	    int rightnum = leftnum+1;
	    rootNode.left = createAsubTree(nextheight, leftnum);
	    rootNode.right = createAsubTree(nextheight, rightnum);
		}         
      return  rootNode;  		
	}
	/**
	 * 获取子树的特征叶子
	 * @param subtree子树根
	 * @return 特征叶子数组
	 */
	public ArrayList<Node> getFeatrues(Node subtree) {
		ArrayList<Node> fs = new ArrayList<Node>();
		fillfs(subtree,fs);		
		return fs;
	}

	/**
	 * 从一颗子树中获取特征叶子，加入到一个列表中
	 * 
	 * @param subtree子树根
	 * @param fs保存特征叶子的列表
	 */
	private void fillfs(Node subtree, ArrayList<Node> fs) {
		if (subtree.left == null)// 判定是叶子
		{
			if (subtree.isFeature() == true)// 是特征节点则加入
				fs.add(subtree);
		} else {
			fillfs(subtree.left, fs);
			fillfs(subtree.right, fs);
		}

	}
	/**
	 * 获取某个子树的高度
	 * @param subtree子树的根
	 * @return
	 */
	public int getHeight(Node subtree) {
		if(subtree ==null)		
			return  0;
		else
			return getHeight(subtree.left)+1;	
	}


	/**
	 * 随机选择某个层上的一个内部节点。策略为：
	 * 从allNodes[]中选择所有的内部节点，组成一个临时的innerNodes[]数组。然后从该临时数组中选择该层的所有节点，组成
	 * layerNode[]，再随机选择。
	 * @param j 节点的高度，层，深度
	 * @return
	 */
	public Node randomInnerNode(int j) {
		Node[]	innerNodes = new Node[innerTotal];
		int idx=0;
		for(int i = 0;i<allNodes.length;i++)
		{
			if(allNodes[i].left != null)//说明是内部节点
				innerNodes[idx++] = allNodes[i];
		}
		int left = (int)Math.pow(2, j-1);
		int right = ((int)Math.pow(2, j))-1;
		Node[] layerNode =  new Node[right-left+1];
		int temp = 0;
		//如果节点编号n在【2^(h-1)，2^h-1]之间，那么放入layerNode[]中
		for(int k = 0;k<innerNodes.length;k++)
		{
			int num= innerNodes[k].getNum();
			if(num>=left && num<=right)
				layerNode[temp++] = innerNodes[k];				
		}
		int index = (int)(Math.random() * layerNode.length);//随机产生下标
		return layerNode[index];
	}

	/**
	 * 随机选择运算符，从op数组中
	 */
	private double randomOperator() {
		int index = (int)(Math.random() * op.length);//随机产生下标
		return op[index];
	}

	/**
	 * 随机产生常数，从constant数组中
	 * @return
	 */
	private double randomConstant() {
		int index = (int)(Math.random() * constant.length);//随机产生下标
		return constant[index];		
	}

	/**
	 * 随机从一个特征节点中选择一个特征节点，策略为：
	 * 从叶子节点leaves[]中选择出所有的特征节点，存入临时featrues节点中，然后从featrues中随机选择。
	 * @return
	 */
	public  Node randomFeatrueNode() {
		Node[]	fNodes = new Node[features.length];
		int idx=0;
		for(int i= 0;i<leaves.length;i++)
		{
			if(leaves[i].isFeature()==true)
				fNodes[idx++] = leaves[i];
		}
		int index = (int)(Math.random() * fNodes.length);//随机产生下标
		return fNodes[index];
	}

	/**
	 * 随机从树中选择一个节点，采用的策略：
	 * 中序遍历树，将节点保存存在数组中。然后随机从数组中随机选择一个节点。
	 * @return
	 */
	public Node randomNode() {
		int index = (int)(Math.random() * allNodes.length);//随机产生下标
		return allNodes[index];			
	}
	
	/**
	 * 将训练所得的模型写入文件中
	 * @param modelpath文件路径
	 */
	public void writeToModelFile(String modelpath) {
		// TODO Auto-generated method stub
		
	}
	
	public static double[] convertFloatsToDoubles(float[] input)
	{
	    if (input == null)
	    {
	        return null; // Or throw an exception - your choice
	    }
	    double[] output = new double[input.length];
	    for (int i = 0; i < input.length; i++)
	    {
	        output[i] = input[i];
	    }
	    return output;
	}

	public void printFunction() {
		print(root);
		
	}
	 
	private void print(Node n) {
		
		if(n!=null)
		{
			ParallelRankBCAClient.outputWindow.append("(");
			print(n.left);
			if(n.isFeature()==true)//判定是特征，还是运算符，否则就是常数
			ParallelRankBCAClient.outputWindow.append("f["+n.featureId+"]");
			else
			if(n.value == ADD)
				ParallelRankBCAClient.outputWindow.append("+");
			else
				if(n.value == MIN)
					ParallelRankBCAClient.outputWindow.append("-");
				else
					if(n.value == MUL)
						ParallelRankBCAClient.outputWindow.append("*");
					else
						if(n.value == DIV)
							ParallelRankBCAClient.outputWindow.append("/");
						else
								ParallelRankBCAClient.outputWindow.append(String.valueOf(n.value));
			print(n.right);
			ParallelRankBCAClient.outputWindow.append(")");
		}
	}

	public void printExpression() {
		printExpression(root);
		
	}

	private void printExpression(Node n) {
		if(n!=null)
		{
			System.out.print("(");
			printExpression(n.left);
			//判定运算符，否则就是常数
		if(n.value == ADD)
				System.out.print("+");
			else
				if(n.value == MIN)
					System.out.print("-");
				else
					if(n.value == MUL)
						System.out.print("*");
					else
						if(n.value == DIV)
							System.out.print("/");
						else
								System.out.print(n.value);
		printExpression(n.right);
			System.out.print(")");
		}
		
	}

	/**
	 * 将抗原中的文档列表按照得分降序排列，返回有序文档列表的抗原
	 * @param ag
	 * @return
	 */
	public Antigen orderAntigen(Antigen ag) {
		double[] d = computeScores(ag.getRl());
		int[] idx = Sorter.sort(d, false); 
		RankList newlist = new RankList(ag.getRl(),idx);
		Antigen orderag = new Antigen(newlist);
		return orderag;
	}

	@Override
	public int compareTo(Antibody o) {
		if(this.affinity<o.affinity)
			return -1;
		else if(this.affinity == o.affinity)
			return 0;
		else
			return 1;
	}
	/**
	 * 克隆对象，经测试，失败；使用序列化
	 */
	/*@Override  
    public Object clone() throws CloneNotSupportedException {  
		Antibody newab = (Antibody) super.clone();  
		//克隆树
		recursivelyClone(newab.root,this.root);
		//克隆featrues
		newab.features = new double[FEATRUE_COUNT];
		System.arraycopy(features,0,newab.features,0,FEATRUE_COUNT);
	    return newab;  
    }

	private void recursivelyClone(Node newroot,Node oldroot) throws CloneNotSupportedException {
		if(newroot !=null && oldroot!=null)
		{
			newroot = (Node) oldroot.clone();
			recursivelyClone( newroot.left,oldroot.left);
			recursivelyClone(newroot.right,oldroot.left);
		}
		
	}  */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		printString(root,sb);
		return sb.toString();
	}

	private void printString(Node n,StringBuilder sb) {
		if(n!=null)
		{
			sb.append("(");
			//System.out.print("(");
			printString(n.left,sb);
			if(n.isFeature()==true)//判定是特征，还是运算符，否则就是常数
			//System.out.print("f["+n.featureId+"]");
				sb.append("f["+n.featureId+"]");
			else
			if(n.value == ADD)
				//System.out.print("+");
				sb.append("+");
			else
				if(n.value == MIN)
					//System.out.print("-");
						sb.append("-");
				else
					if(n.value == MUL)
					//	System.out.print("*");
							sb.append("*");
					else
						if(n.value == DIV)
							//System.out.print("/");
							sb.append("/");
						else
								//System.out.print(n.value);
							sb.append(n.value);
			printString(n.right,sb);
			sb.append(")");
			//System.out.print(")");
		}		
	}

	public double evaluateMap(AntigenRepertoire eVA) {
		double map = computeAffinity(eVA);
		return map;
	}

	/**
	 * 用于BCA的连续区域变异
	 */
	public void mutate() {
		int H = HEIGHT;
		int[] direction = { -1, 1 };
		int p1 = (int) (Math.random() * (int) (Math.pow(2, H) - 1)) + 1;// hotspot,从1开始
		int d = direction[(int) (Math.random() * 2)];// 随机方向，-1表示左，1表示右。
		// System.out.println("hostsopt:"+p1);
		//System.out.println("编码长度："+H);
	//	 System.out.println("变异方向："+d);
		if (d == -1)// 如果向左变异
		{
			int l = (int) (Math.random() *p1) + 1;// 变异长度，从1开始，包含hotspot
			//System.out.println("变异长度："+l);
			for(int i = p1-1;i>p1-l-1;i--){
				Node n = allNodes[i];
				doMutation(n);
			}
		} else// 向右变异
		{
			int l = (int) (Math.random() * (int) (Math.pow(2, H) - p1)) + 1;// 变异长度，从1开始，包含hotspot
		//	System.out.println("变异长度："+l);
			for (int i = p1 - 1; i < p1 - 1 + l; i++)// 连续区域
			{
				Node n = allNodes[i];
				doMutation(n);
			}
		}

	}

	private void doMutation(Node n) {
		if (n.getLeft() == null)// 是叶子，就从常数或者特征中选择
		{
			double[] allone = new double[constant.length
					+ FEATRUE_COUNT]; // 合并一下特征和常数，从这里随机选择
			// 第一部分是常数，下标范围是【0-constant.length-1]
			for (int i1 = 0; i1 < constant.length; i1++)
				allone[i1] = constant[i1];
			// 第二部分是特征下标
			for (int i1 = constant.length; i1 < constant.length
					+ FEATRUE_COUNT; i1++)
				allone[i1] = i1 - constant.length;

			int index = (int) (Math.random() * allone.length); // 随机产生下标
			if (index >= 0 && index <= constant.length - 1)// 选了一个常数节点
				n.setValue(allone[index]);// 改变抗体树该节点的值
			else// 如果是特征，那么就设置抗体树该节点为特征节点，并设置特征id
			{
				n.setFeature(true);
				n.setFeatureId((int) allone[index]);
			}
		} else// 设置一个随机的运算符
		{
			int index = (int) (Math.random() * op.length); // 随机产生下标
			n.setValue(op[index]);
		}
		
	}

	/**
	 * 在测试集上使用P@1-10评价最优抗体
	 * @param tEST
	 * @return
	 */
	public double[] evaluateP1To10(AntigenRepertoire tEST) {
		PrecisionScorer ps = new PrecisionScorer();
		double[] p = new double[11];
		//对测试集中的抗原，按照查询进行评分
		//对所有查询排序。
		ArrayList<Antigen> agl = tEST.getAgList();
		List<RankList> rl  = new ArrayList<RankList>();//存放排好序的
		for(int i = 0;i<agl.size();i++)
		{
			Antigen ag =agl.get(i);
			Antigen orderag = this.orderAntigen(ag);//排序
			rl.add(orderag.getRl());				
		}
		for(int k = 1;k<11;k++)
		{
			ps.setK(k);
			p[k] = ps.score(rl);
		}		
		return p;
	}
	/**
	 * 在测试集上使用NDCG@1-10评价抗体
	 * @param tEST
	 * @return
	 */
	public double[] evaluateNDCG1To10(AntigenRepertoire tEST) {
		
		//使用自己编写的NDCG，休息需要得到排序函数排好文档的label列表，是一个double数组
		
		double[] ndcg = new double[10];
		//对测试集中的抗原，按照查询进行评分
		//对所有查询排序。
		ArrayList<Antigen> agl = tEST.getAgList();
		List<RankList> rl  = new ArrayList<RankList>();//存放排好序的
		for(int i = 0;i<agl.size();i++)
		{
			Antigen ag =agl.get(i);
			Antigen orderag = this.orderAntigen(ag);//排序
			rl.add(orderag.getRl());			
		}
	 
		//获取label数组
		for(int k = 0;k<rl.size();k++)
		{
			RankList r = rl.get(k);//获取一个查询
			double[] labels = new double[r.size()];
			for(int i = 0;i<r.size();i++)//填充labels
				labels[i] =  r.get(i).getLabel();
			double[] n = NDCG.getNDCG(labels);
			for(int j = 0;j<10;j++)					//对NDCG的前10个数累加
				ndcg[j] = ndcg[j] + n[j];
			
		}
		for(int i =0;i<10;i++)						//算平均值
			ndcg[i] = ndcg[i]/rl.size();
		
		return ndcg;
	}
/**
 * 使用抗体，计算测试集每个文档对的得分
 * @param tEST测试集
 * @return
 */
	public String predictionScore(AntigenRepertoire tEST) {
		StringBuilder sb =new StringBuilder();
		ArrayList<Antigen> tlist = tEST.getAgList();
		int numQuery = tlist.size();				//查询个数
		for(int i = 0;i<numQuery;i++)
		{
			Antigen ag = tlist.get(i);
			RankList rl = ag.getRl();
			for(int j=0;j<rl.size();j++)
			{
				DataPoint dp = rl.get(j);
				float[] fvals = dp.getfVals();
				setFeatures(convertFloatsToDoubles(fvals));
				double s = ranker(root);											//将得分写入流
				sb.append(s+"\n");
			}			
		}
		return sb.toString();
	}
}
