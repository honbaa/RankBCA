package test;

import tyut.edu.bca.AntigenRepertoire;

public class Config {
	public static  int M=2 ;//处理器个数
	public static  int N=100;//种群规模
	public static  double clones=0.5;//克隆因子
	public static  int Gen=1 ;//迭代次数
	
	public static  AntigenRepertoire TRAIN ;
	public static  AntigenRepertoire VALI ;
	public static  AntigenRepertoire TEST ;
	//用于同步
	public static final Object lockObj= new Object();
	public static final Object lockObj2= new Object();
	/**
	 * @return the m
	 */
	public static int getM() {
		return M;
	}
	/**
	 * @param m the m to set
	 */
	public static void setM(int m) {
		M = m;
	}
	/**
	 * @return the n
	 */
	public static int getN() {
		return N;
	}
	/**
	 * @param n the n to set
	 */
	public static void setN(int n) {
		N = n;
	}
	/**
	 * @return the clones
	 */
	public static double getClones() {
		return clones;
	}
	/**
	 * @param clones the clones to set
	 */
	public static void setClones(double clones) {
		Config.clones = clones;
	}
	/**
	 * @return the gen
	 */
	public static int getGen() {
		return Gen;
	}
	/**
	 * @param gen the gen to set
	 */
	public static void setGen(int gen) {
		Gen = gen;
	}
	/**
	 * @return the tRAIN
	 */
	public static AntigenRepertoire getTRAIN() {
		return TRAIN;
	}
	/**
	 * @param tRAIN the tRAIN to set
	 */
	public static void setTRAIN(AntigenRepertoire tRAIN) {
		TRAIN = tRAIN;
	}
	/**
	 * @return the vALI
	 */
	public static AntigenRepertoire getVALI() {
		return VALI;
	}
	/**
	 * @param vALI the vALI to set
	 */
	public static void setVALI(AntigenRepertoire vALI) {
		VALI = vALI;
	}
	/**
	 * @return the tEST
	 */
	public static AntigenRepertoire getTEST() {
		return TEST;
	}
	/**
	 * @param tEST the tEST to set
	 */
	public static void setTEST(AntigenRepertoire tEST) {
		TEST = tEST;
	}
	
	
}
