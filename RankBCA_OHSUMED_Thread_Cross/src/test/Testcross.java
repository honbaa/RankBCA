package test;

import tyut.edu.bca.AntibodyRepertoire;

public class Testcross {
	 public static void main(String args[])
	 {
		 for(int i =0;i<100;i++){
		 CloneAndCross c = new CloneAndCross(new AntibodyRepertoire(100));
		 c.cloneAndCross();}
	 }
}
