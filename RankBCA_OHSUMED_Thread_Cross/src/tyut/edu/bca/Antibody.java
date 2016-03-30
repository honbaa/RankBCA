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
	private double affinity = 0;																					//�׺���
	public static final double ADD = 100,MIN=200,MUL=300,DIV=400;													//��ʾ���������	
	public static double[] op = {ADD,MIN,MUL,DIV};																	//��������飨ȫ�֣�
	private static double[] constant={0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10};	//�������飨ȫ�֣�															//��������
	private double[] features ;																						//����ֵ����
	private static final int FEATRUE_COUNT=45;//ohsumed�е�����
	private static int HEIGHT = 7;																					//���ĸ߶�
	//*****ע��������͵Ŀ�¡*****
	private Node root;																								//���ĸ�
	private Node[] leaves;																							//����Ҷ��
	//����Ŀ�¡��
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
	private int innerTotal;																							//�����ڲ��ڵ�����
	private int temp;//�����õģ���Ϊ�����±��ã�������ʵ�����塣
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

	//******��������indexΪfillNodsArray()��������,ͬ����ʼ�������*****
	private Node[] allNodes; //allNods����ֻ�Ǳ�����һ�����нڵ������
	//index�ǵ�ǰNode[]���±�
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
		//Ҫ����Щ����ֵ������Ӧ�������ڵ�value��ȥ�����ܼ��������������value��ֵ��0��	
		
		//����allNodes���飬��ȡ�����ڵ㣬�ж���������ţ�����Ӧ��������ż���
		for(int i=0;i<allNodes.length;i++)
		{
			if(allNodes[i].isFeature()== true)
			{
				int fid = allNodes[i].getFeatureId();//��ȡ�����ڵ�id
				allNodes[i].setValue(features[fid]);//�������ڵ㸳���Ӧ����ֵ
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
	 * ���Ľڵ�����
	 * @author dell
	 *
	 */
	public  class Node implements Serializable{
		private static final long serialVersionUID = 1267293988171991494L; 
		double value;																						//�ڵ��ֵ���ڲ��ڵ�ʹ��ADD�ȳ���
		Node left;
		Node right;
		int num;																							//�ڵ���
		boolean isFeature = false;//������ʾ�ڵ��Ƿ�Ϊ�����ڵ�
		int featureId;			 //������ʾ�ڵ���������,ʹ��ǰ������ʹ��isFeatrue()���������жϺ󷽿�ʹ��
		
		
		
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
		 * ��¡�ڵ㡣�����ԣ�ʧ�ܡ�ʹ�����л�
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
	//���췽������һ�����ṹ�������ʼ���������������ͳ��������������ڵ��valueֵȫ����Ĭ�ϵ�0��
	//�ڼ���һ���ĵ��÷ֵ�ʱ����Ҫ������ֵ���뵽�����ڵ��value�У�����setFeatrues�������ɡ�
	public Antibody(){
		features = new double[FEATRUE_COUNT];							
		this.innerTotal = (int) (Math.pow(2,HEIGHT-1)-1);				//�����ڲ��ڵ����
		initializeLeaves();												//��ʼ��Ҷ��
		this.root = recurCreateFullBiTree(1);							//�����ڲ��ڵ���		
		createLeaves(this.root);										//����Ҷ�ӣ����أ�
		this.allNodes=new Node[innerTotal+leaves.length];				//ֱ��fillNodesArray���ã��������
		fillNodesArray(root);											//����������ڵ�����ã����浽�������
	};
	/**
	 * ���췽��
	 * @param features��������ֵ���׺���Ĭ��Ϊ0���������ͳ���Ĭ��
	 */
	public Antibody(double[] features) {
		super();
		this.features = features;
		this.innerTotal = (int) (Math.pow(2,HEIGHT-1)-1);				//�����ڲ��ڵ����
		initializeLeaves();												//��ʼ��Ҷ��
		this.root = recurCreateFullBiTree(1);							//�����ڲ��ڵ���		
		createLeaves(this.root);										//����Ҷ�ӣ����أ�
	}
	

	/**
	 * ��ʼ������ģ����������Ҳ��ظ�������Ҷ���б�
	 *  ��ȡ�Ĳ���Ϊ��
	 * ���ȣ��������е�Ҷ�ӽڵ��б�
	 * ��Σ��������������ظ���˳����뵽Ҷ���б���
	 * ��󣬽����е�δ��д��Ҷ�Ӵӳ��������������д 
	 * �����γɵ�һ����ʼҶ���б����ظ������ȡ�ڵ��γ����б���ֵ��leaves��
	 */
	private void initializeLeaves() {
		leaves = new Node[(int) Math.pow(2,HEIGHT-1)];
		Node[] temp = new Node[(int) Math.pow(2,HEIGHT-1)];				//��ʱҶ������
		for(int i =0;i<features.length;i++){							//�����ڵ�
			leaves[i] = new Node(0,features[i]);						//��Featrues���룬��Ż�û����Ҫ�ȹ��ص������ܶ�
			leaves[i].setFeature(true);	//�������Ҫ������ʾ�˸ýڵ��������ڵ�
			leaves[i].setFeatureId(i);	//���Ҳ����Ҫ������ʾ�˸�������id
		}
		for(int i = features.length;i<(int) Math.pow(2,HEIGHT-1);i++)	//�����ڵ�
		{
			int index = (int)(Math.random() * constant.length);			//��������±�
			leaves[i] = new Node(0,constant[index]);					//�������Ľڵ�
		}		
		//Ҫ��֤���ظ���ѡ��ѡ������������±�Ԫ�أ�Ȼ�󽫸�Ԫ���滻Ϊ�������һ��Ԫ�أ��������鳤�ȼ�1.
		int length = leaves.length;
	    for (int i = 0; i < leaves.length; i++)
	    {
	      int index = (int)(Math.random() * length);//�����±�
	      temp[i] = leaves[index];					//��i������ڵ�
	//System.out.print(" " + leaves[index].getValue());
	      leaves[index] = leaves[length - 1];
	      length--;
	    }   
	    leaves = temp;			//leaves�����һ��������б��ˣ����Ұ������в��ظ�featrue������������ġ�
	}

	/**
	 * ����һ���ڲ��ڵ����������
	 * 1.�Ƚ����ڲ��ڵ㡣�ڵ��value���ѡ������������е�һ����
	 * 2.����ĩβ����Ҷ�ӽڵ㡣
	 * 
	 * @return �ڲ��ڵ����ĸ�
	 */
	private Node recurCreateFullBiTree(int num) {
			int index = (int)(Math.random() * op.length);//��������±�
		    Node rootNode = new Node(num,op[index]);	//���ڵ�  
	       //--------�Ƚ����ڲ��ڵ㣬�ڵ�����Ϊ2^(HEIGHT-1)-1��--------
		    //������������򴴽�������  
	        if ( num * 2 < innerTotal) {  
	            rootNode.left = recurCreateFullBiTree(num * 2);  
	            //��������Դ������������򴴽�  
	            if (num * 2 + 1 <= innerTotal) {  
	                rootNode.right = recurCreateFullBiTree(num * 2 + 1);  
	            }  
	        }  
	      return  rootNode;  		
	}
	/**
	 * �����ڲ��ڵ�ĸ������������е�Ҷ�ӽڵ�
	 * @param rootNode �ڲ��ڵ����ĸ�	
	 */
	private void createLeaves(Node root) {
		
		if(root.left ==null && root.right ==null)
		{
			
			root.left = getARandomLeaf(root.num*2,temp++);//temp��Ϊһ����ʱ�ģ���ʾ�±�
			root.right = getARandomLeaf(root.num*2+1,temp++);
		}else
		{
			createLeaves(root.left);
			createLeaves(root.right);
		}
		
	}	
	/**
	 * �������������䵽һ��������
	 * @param root����
	 */
	private void fillNodesArray(Node root) {
			if (root != null) {
				allNodes[index++] = root;		//allNods����ֻ�Ǳ�����һ�����нڵ������
				fillNodesArray(root.left);
				fillNodesArray(root.right);
		}
	}

	/**
	 * ���Ѿ������Ҷ���б���˳��ѡ��ڵ㣬���������б��
	 * @param j ����ڵ�ı��
	 * @param temp2 �±�
	 * @return ���ؽڵ�
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
	 * ���㿹���뿹ԭ���ϵ�ƽ���׺���,ʹ��ָ��MAP��
	 * @param aGR ��ԭ��
	 */
	public double computeAffinity(AntigenRepertoire agR) {
		ArrayList<Antigen> aglist = agR.getAgList();
		double map = 0;
		int numQuery = aglist.size();				//��ѯ����
		double sum = 0;
		for(int i = 0;i<numQuery;i++)
		{
			double ap = computeAffinity(aglist.get(i));
			/*System.out.println();
			System.out.println("query idΪ"+i+"��ap:"+ap);*/
			sum += ap;
		}
		map = sum/numQuery;
		/*System.out.println("sum :"+sum);
		System.out.println("numQuery :"+numQuery);
		System.out.println("MAPΪ��");
		System.out.println(map);*/
		affinity = map;									//�������affinity���Ը�ֵ
		return map;
	}

	/**
	 * ���㿹����ĳ����ԭ���׺���.������������Ԥ���һ����ѯ���ĵ��б�������ۣ�ʹ��ָ��AP��
	 * @param ag ĳ����ԭ
	 * @return �����׺���
	 */
	public double computeAffinity(Antigen ag) {
		Antigen orderedag = orderAntigen(ag);
		//��������Ԥ���RankList��������
		APScorer s = new APScorer();	
		return s.score(orderedag.getRl());
	}
	/**
	 * �Կ�ԭ�����ÿ���ĵ���ʹ���������������ĵ÷�
	 * @param ag
	 * @return
	 */
	public double[] computeScores(Antigen ag) {
		RankList rl  = ag.getRl();
		double[] scores = new double[ag.getRl().size()] ;						//����ò�ѯ�������ĵ��ķ���
		for(int i=0;i<rl.size();i++)
		{
			DataPoint dp = rl.get(i);
			float[] fvals = dp.getfVals();
		//System.out.println("id="+dp.getID()+" features:"+Arrays.toString(fvals));
			setFeatures(convertFloatsToDoubles(fvals));
			//updateLeaves(root);													//��Ҷ�Ӷ�Ӧ��ŵ�������ֵ������Ϊ��ǰfeatures��Ӧ������ŵ�ֵ
			//trav(root);
			scores[i] = ranker(root);											//����λ�õ��ĵ��ĵ÷ַ�����Ӧ��λ��
		}
		return scores;
	}

	

	public double[] computeScores(RankList rl) {
		double[] scores = new double[rl.size()] ;						//����ò�ѯ�������ĵ��ķ���
		for(int i=0;i<rl.size();i++)
		{
			DataPoint dp = rl.get(i);
			float[] fvals = dp.getfVals();
		//System.out.println("id="+dp.getID()+" features:"+Arrays.toString(fvals));
			setFeatures(convertFloatsToDoubles(fvals));
			//updateLeaves(root);														//��Ҷ�Ӷ�Ӧ��ŵ�������ֵ������Ϊ��ǰfeatures��Ӧ������ŵ�ֵ
			//trav(root);
			scores[i] = ranker(root);											//����λ�õ��ĵ��ĵ÷ַ�����Ӧ��λ��
		}
		return scores;
		
	}

	/**
	 * ���������ڵ��ֵ
	 */
	public void updateLeaves(Node root) {
		if(root!=null){
			if(root.isFeature==true)
			{
				int id = root.getFeatureId();
				root.setValue(features[id]);//���½ڵ��ֵ
			}else
			{
				updateLeaves(root.left);
				updateLeaves(root.right);
			}
		}
	}

	/**
	 * ��ѡ�����������壩���ܹ�����ĳ���ĵ��Ե�����Ե÷֡��÷ֿ����Ǹ�ֵ��
	 * @param root
	 * @return
	 */
	public double ranker(Node root){
		
			if(root.value == ADD)							//�ӷ�
				return  ranker(root.left)+ranker(root.right);
			if(root.value == MIN)							//����
				return  ranker(root.left)-ranker(root.right);
			if(root.value == MUL)							//�˷�
				return  ranker(root.left)*ranker(root.right);
			if(root.value == DIV)							//����
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
	 * ��¡����
	 * @param clonefactor��¡����
	 * @param nѡ���¡�Ŀ�������
	 * @return ��¡��Ŀ���Ⱥ
	 */
	public AntibodyRepertoire onClone(double clonefactor,int n) {
		ArrayList<Antibody> abl = new ArrayList<Antibody>();
		//��¡����
		int num = 0;
		num = (int)(clonefactor*affinity*n);
	//	System.out.println("��������¡������"+num);
		for(int i =0;i<num;i++)
		{
			Antibody ab = CloneUtils.clone(this);
			//��¡�󣬽��������±�temp,temp2������Ϊ0
			ab.setTemp(0);
			ab.setTemp2(0);
			abl.add(ab);
		}
		AntibodyRepertoire abrclone = new AntibodyRepertoire(abl);
		return abrclone;
		
	}
	/**
	 * ����BCA�Ŀ�¡����¡����Ϊ��Ⱥ��С��
	 * @param clonefactor����Ⱥ��С�ĳ�������
	 * @param n����Ⱥ��С
	 */
	public AntibodyRepertoire cloneBCell(double clonefactor,int n) {
		ArrayList<Antibody> abl = new ArrayList<Antibody>();
		//��¡����
		int num = 0;
		num = (int)(clonefactor*n);
	//	System.out.println("��������¡������"+num);
		for(int i =0;i<num;i++)
		{
			Antibody ab = CloneUtils.clone(this);
			//��¡�󣬽��������±�temp,temp2������Ϊ0
			ab.setTemp(0);
			ab.setTemp2(0);
			abl.add(ab);
		}
		AntibodyRepertoire abrclone = new AntibodyRepertoire(abl);
		return abrclone;
		
	}
	
	
	
	/**
	 * ���峬����
	 * @param mutationactor��������
	 */
	public void hypermutete(double mutationactor) {
		//���������ʵ�ֽ������������Ϊ�̶��ı���ٷֱ�
		//���ȴӽ�������еĿ��尴���׺�������
		
		
	}

	/**
	 * �����졣����������£�
	 * 1.�����Ҷ�ӣ�
	 * 		�������������ô���ѡ����һ��Ҷ������������������š�
	 * 		����ǳ�������ô���ѡ����һ���������滻ֵ
	 * 2.������ڲ��ڵ㣬��ô���ѡ����һ����������滻ֵ��
	 */
	public void singleMutate(){
		Node n = randomNode();
		//System.out.println(n.getNum()+":"+n.isFeature+":"+n.getValue());
		//�����Ҷ��
		if(n.left == null)
		{
			//System.out.println("�������Ҷ��");
			if(n.isFeature()==true)
			{
				//System.out.println("�����������");
				Node newNode = randomFeatrueNode();
				int newid = newNode.getFeatureId();
				int id = n.getFeatureId();
				newNode.setFeatureId(id);
				n.setFeatureId(newid);
			}else
			{
				//System.out.println("������ǳ���");
				double newValue = randomConstant();
				if(n.getValue() == newValue)
					newValue = randomConstant();
				else
					n.setValue(newValue);
			}
		}else//�����
		{
			//System.out.println("������������");
			double op = randomOperator();
			if(op == n.getValue())
				op = randomOperator();
			else
				n.setValue(op);
		}
	}
	/**
	 * ����죬�������ķ�ʽ�滻��
	 * ��Ҫ��֤2�㣺1.�滻��Ľڵ���Ҫ��������������˳��2.�滻�����������뱻�滻������������ȫ��ͬ�Ҳ��ظ��������������ڵ�
	 * ����������ġ� 
	 * ���ԣ�
	 * 1.���ѡȡ�����һ���ڲ��ڵ�n����ȡ�߶�h
	 * 2.��ȡ�ýڵ�Ϊ���������ӽڵ�f[]����������Ϊ�˽�������Щ�����ڵ㣬����������ؽ��µ������ϣ��������������������Ȼ���ظ�����ȫ��������������
	 * 3.����h-1�߶ȵ�����t
	 * 4.��������f[]��Ҷ�ӽڵ�l[]������Ϊ�߶�Ϊh��Ҷ�ӽڵ���Ŀ������h-1�����һ�㣩.
	 * 5.���ѡȡl[]�е�Ҷ�ӣ����ص�t���档
	 * 6.��t�滻n�������ǽ�����n=t.�����������ԭ����������ֻ�Ǹ����˱���n���ѡ�
	 */
	public void multiMutate(int layer){
		Node subtree = randomInnerNode(layer);//��ȡĳһ����ڲ��ڵ�
		ArrayList<Node> featrues = getFeatrues(subtree);//��ȡ�ڲ��ڵ�Ϊ������������������Ҷ�ӽڵ㡣
		int height = getHeight(subtree);//��ȡ�����ĸ߶�
		//System.out.println("�������ĸ߶ȣ�"+height+"\n");
		int num = subtree.getNum();//��ȡ���ı��
		Node t = createAsubTree(height-1,num);//����h-1�߶�����
		Node[] leaves =	createAllLeaves(featrues,height);//һ�������������������Ҷ��
		createLeaves(t,leaves);//��Ҷ�Ӱ�˳�����
		//���ϣ�һ�������ͽ����ˡ�
		replaceSubTree(subtree,t);
	}
	
	public void multiMutateNOlimit(int layer){		
		Node subtree = randomInnerNode(layer);//��ȡĳһ����ڲ��ڵ�
		int height = getHeight(subtree);//��ȡ�����ĸ߶�
		int num = subtree.getNum();//��ȡ���ı��
		Node t = createAsubTree(height-1,num);//����h-1�߶�����
		Node[] leaves =	createAllLeavesNoLimit(height);//Ҷ���ϵĽڵ����Ͳ������ơ�
		createLeaves(t,leaves);//��Ҷ�Ӱ�˳�����
		replaceSubTree(subtree,t);
	}
		
	
	
	 private Node[] createAllLeavesNoLimit(int height) {
		 Node[] ls = new Node[(int) Math.pow(2,height-1)];				//��ʱҶ������
		 double[] allone = new double[constant.length+FEATRUE_COUNT]; //�ϲ�һ�������ͳ��������������ѡ��
		 //��һ�����ǳ������±귶Χ�ǡ�0-constant.length-1]
		 for(int i = 0;i<constant.length;i++)
			 allone[i] = constant[i];
		 //�ڶ������������±�
		 for(int i = constant.length;i<constant.length+FEATRUE_COUNT;i++)
			 allone[i] = i-constant.length;									
		for(int i = 0;i<ls.length;i++)//����Ԫ��ȫ������������Ƿ��ظ�
		{
			 int index = (int)(Math.random() * allone.length);			//��������±�
			 if(index>=0 && index<=constant.length-1)//ѡ��һ�������ڵ�
				 ls[i] = new Node(0,allone[index]);
			 else
			 {
				ls[i] = new Node(0,allone[index]);//�����±���ֵ���������������İ����������Ƶ�
				ls[i].setFeature(true);
				ls[i].setFeatureId((int) allone[index]);
			 }
		}
		return ls;
				
	}

	/**
	  * ʹ���ұߵ������滻��ߵ����������ԣ�
	  * 1.�Ӹ��ڵ�����жϣ����ݽڵ�ı�Ž��С�
	  * 2.�����ԭָ��ָ��t
	  * 3.���������Ҫ����root
	  * @param subtree���滻����
	  * @param t�滻����
	  */
		public  void replaceSubTree(Node subtree, Node t) {
		int num = subtree.getNum();//�ѱ��ȡ�������ڶԱ�
		if(this.root.getNum() == num)
			this.root = t;
		else 
			traverseReplace(this.root,subtree,t);
		}

/**
 * ��������Ѱ�ҳɹ����滻
 * @param r������
 * @param subtree���滻����
 * @param t�滻��
 * @return �Ƿ�ɹ�
 */
		private boolean traverseReplace(Node r, Node subtree, Node t) {
			if(r.left!=null)
			{
				if(replace(r,subtree,t)== true)
					return true;//�滻����ֹ
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
 * ��3���ڵ��С�����жϣ��������滻
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
 * ��Ҷ��������ص���
 * @param t����
 * @param lsҶ������
 * @param idxҶ�������±�
 */
public  void createLeaves(Node t, Node[] ls) {
	if(t.left ==null && t.right ==null)
	{
		Node l  = ls[temp2++];
		l.setNum(2*t.num);
		t.setLeft(l);;
		Node r = ls[temp2++];
		r.setNum(2*t.num+1);
		t.setRight(r);//��������Ȼ���������ˣ�������������������������������������������������������������������������������������������
	}else
	{
		createLeaves(t.left,ls);
		createLeaves(t.right,ls);
	}
		
	}

/**
 * ����һ��������е�Ҷ�ӡ��������ֵ�����ƶ������������Լ������������֮ǰ�Ǹ���ʼ���������ơ������Ǹ�������Ӧ�ı䡣
 * @param fs�����б�
 * @param height���߶�
 * @param lsҪ����Ҷ������
 */
	public Node[] createAllLeaves(ArrayList<Node> fs,int height) {	
		Node[] ls = new Node[(int) Math.pow(2,height-1)];				//��ʱҶ������
		Node[] temp = new Node[(int) Math.pow(2,height-1)];				//��ʱҶ������
		for(int i =0;i<fs.size();i++){									//�����ڵ�
			ls[i] = fs.get(i);											//�������ڵ����
	    }
		for(int i = fs.size();i<(int) Math.pow(2,height-1);i++)	//�����ڵ�
		{
			int index = (int)(Math.random() * constant.length);			//��������±�
			ls[i] = new Node(0,constant[index]);					//�������Ľڵ�
		}		
		//Ҫ��֤���ظ���ѡ��ѡ������������±�Ԫ�أ�Ȼ�󽫸�Ԫ���滻Ϊ�������һ��Ԫ�أ��������鳤�ȼ�1.
		int length = ls.length;
	    for (int i = 0; i < ls.length; i++)
	    {
	      int index = (int)(Math.random() * length);//�����±�
	      temp[i] = ls[index];					    //��i������ڵ�
	//System.out.print(" " + leaves[index].getValue());
	      ls[index] = ls[length - 1];
	      length--;
	    }   
	    //ls =temp;			//leaves�����һ��������б��ˣ����Ұ������в��ظ�featrue������������ġ�
	  /*  for(Node l:temp)
			System.out.print(" ["+l.getNum()+":"+l.getValue()+"]"+"feature��"+l.isFeature());*/
	    return temp;
	}

	/**
	 * ����һ������
	 * @param height�����ĸ߶�
	 * @param num���ڵ����ʼ���
	 * @return
	 */
	public Node createAsubTree(int height,int num) {
		int index = (int)(Math.random() * op.length);//��������±�		
		Node rootNode = new Node(num,op[index]);	//���ڵ�
		//����߶ȴ���1��������������
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
	 * ��ȡ����������Ҷ��
	 * @param subtree������
	 * @return ����Ҷ������
	 */
	public ArrayList<Node> getFeatrues(Node subtree) {
		ArrayList<Node> fs = new ArrayList<Node>();
		fillfs(subtree,fs);		
		return fs;
	}

	/**
	 * ��һ�������л�ȡ����Ҷ�ӣ����뵽һ���б���
	 * 
	 * @param subtree������
	 * @param fs��������Ҷ�ӵ��б�
	 */
	private void fillfs(Node subtree, ArrayList<Node> fs) {
		if (subtree.left == null)// �ж���Ҷ��
		{
			if (subtree.isFeature() == true)// �������ڵ������
				fs.add(subtree);
		} else {
			fillfs(subtree.left, fs);
			fillfs(subtree.right, fs);
		}

	}
	/**
	 * ��ȡĳ�������ĸ߶�
	 * @param subtree�����ĸ�
	 * @return
	 */
	public int getHeight(Node subtree) {
		if(subtree ==null)		
			return  0;
		else
			return getHeight(subtree.left)+1;	
	}


	/**
	 * ���ѡ��ĳ�����ϵ�һ���ڲ��ڵ㡣����Ϊ��
	 * ��allNodes[]��ѡ�����е��ڲ��ڵ㣬���һ����ʱ��innerNodes[]���顣Ȼ��Ӹ���ʱ������ѡ��ò�����нڵ㣬���
	 * layerNode[]�������ѡ��
	 * @param j �ڵ�ĸ߶ȣ��㣬���
	 * @return
	 */
	public Node randomInnerNode(int j) {
		Node[]	innerNodes = new Node[innerTotal];
		int idx=0;
		for(int i = 0;i<allNodes.length;i++)
		{
			if(allNodes[i].left != null)//˵�����ڲ��ڵ�
				innerNodes[idx++] = allNodes[i];
		}
		int left = (int)Math.pow(2, j-1);
		int right = ((int)Math.pow(2, j))-1;
		Node[] layerNode =  new Node[right-left+1];
		int temp = 0;
		//����ڵ���n�ڡ�2^(h-1)��2^h-1]֮�䣬��ô����layerNode[]��
		for(int k = 0;k<innerNodes.length;k++)
		{
			int num= innerNodes[k].getNum();
			if(num>=left && num<=right)
				layerNode[temp++] = innerNodes[k];				
		}
		int index = (int)(Math.random() * layerNode.length);//��������±�
		return layerNode[index];
	}

	/**
	 * ���ѡ�����������op������
	 */
	private double randomOperator() {
		int index = (int)(Math.random() * op.length);//��������±�
		return op[index];
	}

	/**
	 * ���������������constant������
	 * @return
	 */
	private double randomConstant() {
		int index = (int)(Math.random() * constant.length);//��������±�
		return constant[index];		
	}

	/**
	 * �����һ�������ڵ���ѡ��һ�������ڵ㣬����Ϊ��
	 * ��Ҷ�ӽڵ�leaves[]��ѡ������е������ڵ㣬������ʱfeatrues�ڵ��У�Ȼ���featrues�����ѡ��
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
		int index = (int)(Math.random() * fNodes.length);//��������±�
		return fNodes[index];
	}

	/**
	 * ���������ѡ��һ���ڵ㣬���õĲ��ԣ�
	 * ��������������ڵ㱣����������С�Ȼ����������������ѡ��һ���ڵ㡣
	 * @return
	 */
	public Node randomNode() {
		int index = (int)(Math.random() * allNodes.length);//��������±�
		return allNodes[index];			
	}
	
	/**
	 * ��ѵ�����õ�ģ��д���ļ���
	 * @param modelpath�ļ�·��
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
			if(n.isFeature()==true)//�ж��������������������������ǳ���
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
			//�ж��������������ǳ���
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
	 * ����ԭ�е��ĵ��б��յ÷ֽ������У����������ĵ��б�Ŀ�ԭ
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
	 * ��¡���󣬾����ԣ�ʧ�ܣ�ʹ�����л�
	 */
	/*@Override  
    public Object clone() throws CloneNotSupportedException {  
		Antibody newab = (Antibody) super.clone();  
		//��¡��
		recursivelyClone(newab.root,this.root);
		//��¡featrues
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
			if(n.isFeature()==true)//�ж��������������������������ǳ���
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
	 * ����BCA�������������
	 */
	public void mutate() {
		int H = HEIGHT;
		int[] direction = { -1, 1 };
		int p1 = (int) (Math.random() * (int) (Math.pow(2, H) - 1)) + 1;// hotspot,��1��ʼ
		int d = direction[(int) (Math.random() * 2)];// �������-1��ʾ��1��ʾ�ҡ�
		// System.out.println("hostsopt:"+p1);
		//System.out.println("���볤�ȣ�"+H);
	//	 System.out.println("���췽��"+d);
		if (d == -1)// ����������
		{
			int l = (int) (Math.random() *p1) + 1;// ���쳤�ȣ���1��ʼ������hotspot
			//System.out.println("���쳤�ȣ�"+l);
			for(int i = p1-1;i>p1-l-1;i--){
				Node n = allNodes[i];
				doMutation(n);
			}
		} else// ���ұ���
		{
			int l = (int) (Math.random() * (int) (Math.pow(2, H) - p1)) + 1;// ���쳤�ȣ���1��ʼ������hotspot
		//	System.out.println("���쳤�ȣ�"+l);
			for (int i = p1 - 1; i < p1 - 1 + l; i++)// ��������
			{
				Node n = allNodes[i];
				doMutation(n);
			}
		}

	}

	private void doMutation(Node n) {
		if (n.getLeft() == null)// ��Ҷ�ӣ��ʹӳ�������������ѡ��
		{
			double[] allone = new double[constant.length
					+ FEATRUE_COUNT]; // �ϲ�һ�������ͳ��������������ѡ��
			// ��һ�����ǳ������±귶Χ�ǡ�0-constant.length-1]
			for (int i1 = 0; i1 < constant.length; i1++)
				allone[i1] = constant[i1];
			// �ڶ������������±�
			for (int i1 = constant.length; i1 < constant.length
					+ FEATRUE_COUNT; i1++)
				allone[i1] = i1 - constant.length;

			int index = (int) (Math.random() * allone.length); // ��������±�
			if (index >= 0 && index <= constant.length - 1)// ѡ��һ�������ڵ�
				n.setValue(allone[index]);// �ı俹�����ýڵ��ֵ
			else// �������������ô�����ÿ������ýڵ�Ϊ�����ڵ㣬����������id
			{
				n.setFeature(true);
				n.setFeatureId((int) allone[index]);
			}
		} else// ����һ������������
		{
			int index = (int) (Math.random() * op.length); // ��������±�
			n.setValue(op[index]);
		}
		
	}

	/**
	 * �ڲ��Լ���ʹ��P@1-10�������ſ���
	 * @param tEST
	 * @return
	 */
	public double[] evaluateP1To10(AntigenRepertoire tEST) {
		PrecisionScorer ps = new PrecisionScorer();
		double[] p = new double[11];
		//�Բ��Լ��еĿ�ԭ�����ղ�ѯ��������
		//�����в�ѯ����
		ArrayList<Antigen> agl = tEST.getAgList();
		List<RankList> rl  = new ArrayList<RankList>();//����ź����
		for(int i = 0;i<agl.size();i++)
		{
			Antigen ag =agl.get(i);
			Antigen orderag = this.orderAntigen(ag);//����
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
	 * �ڲ��Լ���ʹ��NDCG@1-10���ۿ���
	 * @param tEST
	 * @return
	 */
	public double[] evaluateNDCG1To10(AntigenRepertoire tEST) {
		
		//ʹ���Լ���д��NDCG����Ϣ��Ҫ�õ��������ź��ĵ���label�б���һ��double����
		
		double[] ndcg = new double[10];
		//�Բ��Լ��еĿ�ԭ�����ղ�ѯ��������
		//�����в�ѯ����
		ArrayList<Antigen> agl = tEST.getAgList();
		List<RankList> rl  = new ArrayList<RankList>();//����ź����
		for(int i = 0;i<agl.size();i++)
		{
			Antigen ag =agl.get(i);
			Antigen orderag = this.orderAntigen(ag);//����
			rl.add(orderag.getRl());			
		}
	 
		//��ȡlabel����
		for(int k = 0;k<rl.size();k++)
		{
			RankList r = rl.get(k);//��ȡһ����ѯ
			double[] labels = new double[r.size()];
			for(int i = 0;i<r.size();i++)//���labels
				labels[i] =  r.get(i).getLabel();
			double[] n = NDCG.getNDCG(labels);
			for(int j = 0;j<10;j++)					//��NDCG��ǰ10�����ۼ�
				ndcg[j] = ndcg[j] + n[j];
			
		}
		for(int i =0;i<10;i++)						//��ƽ��ֵ
			ndcg[i] = ndcg[i]/rl.size();
		
		return ndcg;
	}
/**
 * ʹ�ÿ��壬������Լ�ÿ���ĵ��Եĵ÷�
 * @param tEST���Լ�
 * @return
 */
	public String predictionScore(AntigenRepertoire tEST) {
		StringBuilder sb =new StringBuilder();
		ArrayList<Antigen> tlist = tEST.getAgList();
		int numQuery = tlist.size();				//��ѯ����
		for(int i = 0;i<numQuery;i++)
		{
			Antigen ag = tlist.get(i);
			RankList rl = ag.getRl();
			for(int j=0;j<rl.size();j++)
			{
				DataPoint dp = rl.get(j);
				float[] fvals = dp.getfVals();
				setFeatures(convertFloatsToDoubles(fvals));
				double s = ranker(root);											//���÷�д����
				sb.append(s+"\n");
			}			
		}
		return sb.toString();
	}
}
