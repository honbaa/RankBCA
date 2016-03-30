package test;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import tyut.edu.bca.AntibodyRepertoire;
import tyut.edu.bca.AntigenRepertoire;
import Draw2.Draw;
import ciir.umass.edu.utilities.FileUtils;

import com.mathworks.toolbox.javabuilder.MWException;

public class ParallelRankBCAClient extends JFrame {

	private JPanel contentPane;
	private JTextField trainDir;
	private JTextField predictionDir;
	private JTextField gen;
	private JTextField clonefactor;
	private JTextField capacity;
	private JTextField expNum;
	private JTextField evalScriptPath;
	public static TextArea outputWindow;
	private JTextField M;

	//�������ڴ洢ѧϰ��ʼ��ʱ�䣬�ҵ���־�洢��Ŀ¼
	String dataDir;
	String learnTime;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ParallelRankBCAClient frame = new ParallelRankBCAClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ParallelRankBCAClient() {
		setResizable(false);
		setTitle("\u5E76\u884CB\u7EC6\u80DE\u7B97\u6CD5-------RankBCA\u63A7\u5236\u53F0\uFF1Aohsumed\u6570\u636E\u96C6------------------");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 691, 661);
		contentPane = new JPanel();
		contentPane.setName("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();

		panel.setToolTipText("");
		panel.setName("trainPanel");
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "\u8BAD\u7EC3\u9762\u677F",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panel.setBounds(10, 10, 665, 182);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lbltraindir = new JLabel(
				"\u8F93\u5165\u8BAD\u7EC3\u96C6\u4E3B\u76EE\u5F55\uFF1AtrainDir");
		lbltraindir.setBounds(6, 21, 166, 15);
		panel.add(lbltraindir);

		trainDir = new JTextField();
		trainDir.setText("D:\\\u6211\u7684\u6570\u636E\\LETOR\\OHSUMED\\OHSUMED\\QueryLevelNorm");
		trainDir.setBounds(211, 19, 367, 18);
		panel.add(trainDir);
		trainDir.setName("trainDir");
		trainDir.setColumns(10);

		JLabel lblpredictiondir = new JLabel(
				"\u6A21\u578B\u9884\u6D4B\u7ED3\u679C\u4E3B\u76EE\u5F55\uFF1ApredictionDir");
		lblpredictiondir.setBounds(6, 46, 205, 15);
		panel.add(lblpredictiondir);

		predictionDir = new JTextField();
		predictionDir
				.setText("D:\\\u6211\u7684\u6570\u636E\\\u5B9E\u9A8C\\output\\testpbca");
		predictionDir.setBounds(211, 43, 367, 18);
		panel.add(predictionDir);
		predictionDir.setName("predictionDir");
		predictionDir.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(6, 71, 517, 2);
		panel.add(separator);

		JLabel lblRankbcagen = new JLabel("1.\u8FED\u4EE3\u6B21\u6570gen\uFF1A");
		lblRankbcagen.setBounds(6, 98, 168, 15);
		panel.add(lblRankbcagen);

		gen = new JTextField();
		gen.setText("10");
		gen.setBounds(185, 95, 66, 21);
		panel.add(gen);
		gen.setName("gen");
		gen.setColumns(10);

		JLabel lblRankbca = new JLabel("RankBCA\u4E3B\u8981\u53C2\u6570\uFF1A");
		lblRankbca.setBounds(6, 76, 123, 15);
		panel.add(lblRankbca);

		JLabel lblclonefactor = new JLabel(
				"2.\u514B\u9686\u56E0\u5B50\uFF1Aclonefactor");
		lblclonefactor.setBounds(6, 123, 153, 15);
		panel.add(lblclonefactor);

		clonefactor = new JTextField();
		clonefactor.setText("0.1");
		clonefactor.setBounds(185, 120, 66, 21);
		panel.add(clonefactor);
		clonefactor.setName("clonefactor");
		clonefactor.setColumns(10);

		JLabel lblcapacity = new JLabel(
				"3.\u6297\u4F53\u5E93\u5927\u5C0F\uFF1Acapacity");
		lblcapacity.setBounds(6, 148, 168, 15);
		panel.add(lblcapacity);

		capacity = new JTextField();
		capacity.setText("16");
		capacity.setBounds(185, 145, 66, 21);
		panel.add(capacity);
		capacity.setName("capacity");
		capacity.setColumns(10);

		JLabel lblexpnum = new JLabel(
				"\u5B9E\u9A8C\u6267\u884C\u6B21\u6570\uFF1AexpNum");
		lblexpnum.setBounds(284, 98, 123, 15);
		panel.add(lblexpnum);

		expNum = new JTextField();
		expNum.setBounds(419, 95, 66, 21);
		panel.add(expNum);
		expNum.setName("expNum");
		expNum.setText("1");
		expNum.setColumns(10);

		JLabel lblclonefactorcapacity = new JLabel(
				"\u6CE8\uFF1A\u514B\u9686\u6C60\u5927\u5C0F=clonefactor*capacity");
		lblclonefactorcapacity.setBounds(284, 148, 239, 15);
		panel.add(lblclonefactorcapacity);

		JButton button = new JButton("\u5F00\u59CB\u8BAD\u7EC3");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long start = System.currentTimeMillis();
				outputWindow.setText("");
				// ************************************��ȡʵ�����ݼ�**********************
				// 1.��ȡѵ������Ŀ¼
				File tDir = new File(trainDir.getText());
				String dataSetName = "default dataSet";

				System.out.println(tDir.getAbsolutePath());
				if (tDir.getAbsolutePath().indexOf("OHSUMED") != -1) {
					outputWindow.append("������ݼ���OHSUMED����\n");
					dataSetName = "OHSUMED";
				} else if (tDir.getAbsolutePath().indexOf("MQ2007") != -1) {
					outputWindow.append("������ݼ���MQ2007����\n");
					dataSetName = "MQ2007";
				} else if (tDir.getAbsolutePath().indexOf("MQ2008") != -1) {
					outputWindow.append("������ݼ���MQ2008����\n");
					dataSetName = "MQ2008";
				} else
					outputWindow.append("�޷�ͨ��Ŀ¼���ж����ݼ�����\n");
				dataDir = dataSetName;
				// 2.��ȡģ��Ԥ������Ŀ¼
				File pDir = new File(predictionDir.getText());
				outputWindow.append("ģ��Ԥ������Ŀ¼Ϊ��" + pDir.getAbsolutePath() + "\n");
				// 3.��ȡѵ����Ŀ¼�µ�����FoldĿ¼
				File[] folds = tDir.listFiles();
				if (folds.length != 5)
					try {
						throw new Exception("���Ŀ¼���ǺϷ���ѵ������Ŀ¼��");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				// ʵ�鿪ʼִ��ʱ��
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH-mm-ss");
				String exptime = sdf.format(d);
				learnTime = exptime;
				// ********************ִ��expNum��ʵ��************************
				for (int i = 1; i <= Integer.parseInt(expNum.getText()); i++) {
					outputWindow.append("###########���е�" + (i) + "��ʵ��##########\n");

					// ����Fold������ѧϰ
					// ����ÿһ��Fold����ȡtrain.txt,vali.txt,test.txt
					for (final File fold : folds) {
						// ��ȡ��ǰFold������
						String foldName = fold.getName();
						// 3���ļ���·��
						String train_path = null, vali_path = null, test_path = null;
						outputWindow.append("\n----------����" + foldName + "�Ͻ���----------\n");
						// ��ȡ��ǰFoldĿ¼�е�train.txt,vali.txt,test.txt
						final String[] filter = { "train.txt", "vali.txt",
								"test.txt" };
						File[] files = fold.listFiles();

						if (files.length != 3)
							try {
								throw new Exception(
										"FoldĿ¼ֻӦ����train.txt,vali.txt,test.txt�ļ���");
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						// ����ÿ���ļ���·��
						for (File file : files) {
							String fileName = file.getName();
							if (fileName.equals(filter[0])) {
								train_path = file.getAbsolutePath();
								outputWindow.append("��ȡ��train.txt��"
										+ train_path + "\n");
							} else if (fileName.equals(filter[1])) {
								vali_path = file.getAbsolutePath();
								outputWindow.append("��ȡ��vali.txt��" + vali_path
										+ "\n");
							} else if (fileName.equals(filter[2])) {
								test_path = file.getAbsolutePath();
								outputWindow.append("��ȡ��test.txt��" + test_path
										+ "\n");
							}
						}
						// **********************************��ʼ�����Fold��ѵ��**********************
						//����һ������
						Config.setClones(Double.parseDouble(clonefactor.getText()));
						Config.setGen(Integer.parseInt(gen.getText()));					
						Config.setM(Integer.parseInt(M.getText()));
						Config.setN(Integer.parseInt(capacity.getText()));
						Config.setTRAIN(new AntigenRepertoire(train_path));
						Config.setVALI(new AntigenRepertoire(vali_path));
						Config.setTEST(new AntigenRepertoire(test_path));
						final CountDownLatch countDownLatch = new CountDownLatch(Config.M);
						ParallelBCA t = new ParallelBCA(new AntibodyRepertoire(Config.N),countDownLatch);
						String result = t.learn();

						// *************************����Fold��Ԥ��д�����������ļ�**************
						// 1.��ȡ��Ŀ¼
						File outDir = new File(predictionDir.getText());
						if (outDir.exists()) {
							if (outDir.isFile())
								try {
									throw new Exception("ָ�����Ŀ¼��һ���ļ�����Ч");
								} catch (Exception e1) {
									e1.printStackTrace();
								}
						} else
							outDir.mkdirs();
						// 2.����һ��������ʽ���ļ������ݼ�/ʵ��ʱ��/��ǰʵ�����/��ǰFold/prediction.txt
						File folder_dir = new File(outDir.getAbsolutePath()
								+ "\\" + dataSetName + "\\" + exptime + "\\"
								+ i + "\\" + foldName);
						String file_path = outDir.getAbsolutePath() + "\\"
								+ dataSetName + "\\" + exptime + "\\" + i
								+ "\\" + foldName + "\\prediction.txt";
						if (!folder_dir.exists())
							folder_dir.mkdirs();
						File prediction_file = new File(file_path);
						if (!prediction_file.exists())
							try {
								outputWindow.append("��"+foldName+"��ѵ����ɣ������ģ��Ԥ����д���ļ���\n"
										+ file_path + "\n");
								prediction_file.createNewFile();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						ciir.umass.edu.utilities.FileUtils.write(file_path,
								"utf-8", result);
					}
					outputWindow.append("��" + i + "��ʵ�������ɡ�\n\n");
				}
			long end = System.currentTimeMillis();
			double time = (end-start)/1000.0/60.0;
			String unit = " minites";
			if(time>60)
			{
				time = time/60;
				unit = " hours";
			}
			outputWindow.append(expNum.getText()+"��ʵ��һ��ʹ���ˣ� "+time+ unit);
			File outDir = new File(predictionDir.getText());
			File confFile = new File(outDir.getAbsolutePath()+ "\\" + dataSetName + "\\" + exptime+"\\config.txt");
			
			//��ȡ����8��ʵ�����������Ϣ 
			String input = trainDir.getText();
			String output = predictionDir.getText();
			String generation = gen.getText();
			String cf = clonefactor.getText();
			String m = M.getText();
			String abcout = capacity.getText();
			String exp_num = expNum.getText();
			String eval_script = evalScriptPath.getText();
			
			//��������ַ���
			String config = "intput = "+input+"\n"
					+ "output = "+output+"\n\n"
					+ "generation = "+generation+"\n"
					+ "clonefactor = "+cf+"\n"
					+ "capacity = "+abcout+"\n"
					+ "expNum = "+exp_num+"\n"
					+ "M = "+m+"\n"
					+ "evalScriptPath = "+eval_script+"\n\n"
					+ "runningTime = "+time+unit+"\n";	
			
			FileUtils.write(confFile.getAbsolutePath(),"utf-8", config);
			}
		});
		button.setBounds(562, 94, 93, 23);
		panel.add(button);
		
		JLabel lblm = new JLabel("\u5904\u7406\u5668\u4E2A\u6570\uFF1AM");
		lblm.setBounds(284, 123, 123, 15);
		panel.add(lblm);
		
		M = new JTextField();
		M.setText("1");
		M.setBounds(419, 120, 66, 21);
		panel.add(M);
		M.setColumns(10);
		
		JButton button_1 = new JButton("\u4FDD\u5B58\u65E5\u5FD7");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//������־�����ʵ��ĸ�Ŀ¼	
				File outDir = new File(predictionDir.getText());
				File confFile = new File(outDir.getAbsolutePath()+ "\\" + dataDir + "\\" + learnTime+"\\learnLog.txt");
				
				//��ȡ��־����
				String log = outputWindow.getText();
				FileUtils.write(confFile.getAbsolutePath(),"utf-8", log);
			}
		});
		button_1.setBounds(562, 144, 93, 23);
		panel.add(button_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "\u8BC4\u4EF7\u9762\u677F",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panel_1.setBounds(10, 202, 665, 192);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel label = new JLabel(
				"\u8F93\u5165\u5BF9\u5E94\u6570\u636E\u96C6\u7684\u5B98\u65B9\u8BC4\u4EF7\u811A\u672C\u8DEF\u5F84\uFF1A");
		label.setBounds(6, 22, 204, 15);
		panel_1.add(label);

		evalScriptPath = new JTextField();
		evalScriptPath
				.setText("D:\\\u6211\u7684\u6570\u636E\\LETOR\\EvaluationTool\\Eval-Score-3.0.pl");
		evalScriptPath.setBounds(219, 19, 376, 21);
		panel_1.add(evalScriptPath);
		evalScriptPath.setColumns(10);

		JButton btnNewButton = new JButton("\u5F00\u59CB\u8BC4\u4EF7");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// �����ʹ�ùٷ�letor�ű��������ۣ�Ȼ���Զ��ռ�ÿ��Fold�Ľ������ƽ��������Զ�������ʵ���ƽ�������
				/*
				 * for ȡһ��ʵ���� 1.�ֱ�����Fold1~Fold5����5���ļ�[letor�ű�]
				 * 2.��Fold1~Fold5��ƽ��ֵ����1���ļ�[�Լ���д] end for
				 * ������ʵ���ƽ���������1�ļ������գ�[�Լ���д]
				 */
				File pDir = new File(predictionDir.getText());
				outputWindow.setText("��ʼ��������\n");
				outputWindow.append("��ȡԤ���ļ���Ŀ¼��" + pDir.getAbsolutePath());
				File[] dataSetsDir = pDir.listFiles();
				if (dataSetsDir.length == 0)
					try {
						throw new Exception("Ԥ���ļ���Ŀ¼�����ǿյġ�\n");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				// ����ÿһ�����ݼ���Ŀ¼

				for (File dataSetDir : dataSetsDir) {

					// ��ȡ���ݼ�Ŀ¼�µİ�ʱ�����е�ִ��ʵ��
					File[] expByTimeDirs = dataSetDir.listFiles();
					if (expByTimeDirs.length == 0)
						try {
							throw new Exception("û�������κ�ʵ��\n");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					// ����ÿһ��ʱ��ʵ���ļ���
					for (File timeDir : expByTimeDirs) {

						// ��ȡ����ʵ������ŵ�ʵ���ļ���
						File[] expSeqNums = timeDir.listFiles();
						int dirnum = expSeqNums.length;//�����
						if (expSeqNums.length == 0)
							try {
								throw new Exception("û��ʵ���\n");
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						// ����ÿһ�����Ŀ¼
						for (File expseqNum : expSeqNums) {
							//������ļ���������
							if(expseqNum.isFile())
							{
								dirnum--;//�������
								continue;	
							}
							// ��ȡ����Folds
							File[] folds = expseqNum.listFiles();
							if (folds.length == 0)
								try {
									throw new Exception("ʵ��û�й�\n");
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							// ����ÿһ��Fold
							for (File fold : folds) {
								//������ļ���������
								if(fold.isFile())
									continue;								
								String foldName = fold.getName();
								// ��ȡfold�����prediction.txt�ļ�
								String pfile = fold.getAbsolutePath()
										+ "\\prediction.txt";
								File _file = new File(pfile);
								if (_file.exists() == false)
									try {
										throw new Exception(
												"û��prediction�ļ����������ۡ�");
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								// ��ȡ���ݼ��ж�ӦFold��test.txt�ļ�

								String trainFolds = trainDir.getText();
								File trainFolds_folder = new File(trainFolds);
								// ��ȡ����5��Folds
								File[] train_folds = trainFolds_folder
										.listFiles();
								// �ҵ���foldName ��ͬ��fold
								File _test = null;
								for (File _fold : train_folds) {
									if (_fold.getName().equals(foldName)) {
										// ��ȡ���_fold�µ�test.txt�ļ�
										_test = new File(_fold + "\\test.txt");
										break;
									}
								}
								outputWindow.append("\n-------��ʼ����"
										+ dataSetDir.getName() + "���ݼ�" + "-"
										+ timeDir.getName() + "-"
										+ expseqNum.getName() + "-"
										+ fold.getName() + "------\n");
								// ʹ��letor�ű���_file,_test���ۣ����۽��_resultд��ÿ��fold
								outputWindow.append("��ȡ�����ļ���\n"
										+ _test.getAbsolutePath() + "\n");
								outputWindow.append("��ȡԤ���ļ�:\n"
										+ _file.getAbsolutePath() + "\n");
								outputWindow.append("��ʼִ��perl�ű�...\n");
								ExecPerl.exeEvaluation(
										"C:\\Perl64\\bin\\perl.exe",
										evalScriptPath.getText(),
										_test.getAbsolutePath(),
										_file.getAbsolutePath(), fold
												+ "\\result.txt");

							}
							//��ʵ�������ŵ�Ŀ¼�У�����Ŀ¼��5��Fold��ƽ�����
							
							String avg_result = Analysis5Folds.getAvgResultVersion3(expseqNum.getAbsoluteFile());
							outputWindow.append("\n**********�����"+expseqNum.getName()+"ʵ��5��fold��ƽ��ֵ**********\n");
							outputWindow.append(avg_result+"\n");
							outputWindow.append("д�뵽�ļ���"+expseqNum.getAbsolutePath()+"\\folds_avg.txt"+"\n\n");
							FileUtils.write(expseqNum.getAbsolutePath()+"\\folds_avg.txt", "utf-8", avg_result);
							
							
							
						}
						
						//��ʱ���ļ����µ�����ʵ���е�fols_avg.txt��ȡ��Ȼ����ƽ��ֵ����ʱ���ļ�������
						
						String finalresult = AnalysisFinalResult.getFinalResult(timeDir.getAbsoluteFile());
						outputWindow.append("\n**********���� "+timeDir.getName()+" ���� "+dirnum+" ʵ���ƽ��ֵ**********\n");
						outputWindow.append(finalresult+"\n");
						outputWindow.append("д�뵽�ļ���"+timeDir.getAbsolutePath()+"\\finalresult.txt"+"\n\n");
						FileUtils.write(timeDir.getAbsolutePath()+"\\finalresult.txt", "utf-8", finalresult);
						
						
						
						
						
					}

				}

			}
		});
		btnNewButton.setBounds(410, 75, 93, 23);
		panel_1.add(btnNewButton);
		btnNewButton
				.setToolTipText("\u8BC4\u4EF7\u8FC7\u7A0B\uFF1A\r\nfor \u53D6\u4E00\u6B21\u5B9E\u9A8C\u7ED3\u679C\r\n1.\u5206\u522B\u8BC4\u4EF7Fold1~Fold5\uFF0C\u5F975\u4E2A\u6587\u4EF6[letor\u811A\u672C]\r\n2.\u6C42Fold1~Fold5\u7684\u5E73\u5747\u503C\uFF0C\u5F971\u4E2A\u6587\u4EF6[\u81EA\u5DF1\u7F16\u5199]\r\nend for\r\n\u6C42\u6240\u6709\u5B9E\u9A8C\u7684\u5E73\u5747\u7ED3\u679C\uFF0C\u5F971\u6587\u4EF6\uFF08\u6700\u7EC8\uFF09[\u81EA\u5DF1\u7F16\u5199]");

		JTextArea txtrFor = new JTextArea();
		txtrFor.setBounds(6, 57, 320, 125);
		panel_1.add(txtrFor);
		txtrFor.setBackground(SystemColor.control);
		txtrFor.setText("\u8BC4\u4EF7\u8FC7\u7A0B\uFF1A\r\nfor \u53D6\u4E00\u6B21\u5B9E\u9A8C\u7ED3\u679C\r\n1.\u5206\u522B\u8BC4\u4EF7Fold1~Fold5\uFF0C\u5F975\u4E2A\u6587\u4EF6[letor\u811A\u672C]\r\n2.\u6C42Fold1~Fold5\u7684\u5E73\u5747\u503C\uFF0C\u5F971\u4E2A\u6587\u4EF6[\u81EA\u5DF1\u7F16\u5199]\r\nend for\r\n\u6C42\u6240\u6709\u5B9E\u9A8C\u7684\u5E73\u5747\u7ED3\u679C\uFF0C\u5F971\u6587\u4EF6\uFF08\u6700\u7EC8\uFF09[\u81EA\u5DF1\u7F16\u5199]");

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 47, 519, 2);
		panel_1.add(separator_1);
		
		JButton btnNewButton_1 = new JButton("\u4E0E\u57FA\u7EBF\u6BD4\u8F83");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//��4�������㷨���бȽϣ�������״ͼ��
				
				/*
				NDCG@1	NDCG@2	NDCG@3	NDCG@4	NDCG@5	NDCG@6	NDCG@7	NDCG@8	NDCG@9	NDCG@10
				RankSVM	0.4958	0.4331	0.4207	0.424	0.4164	0.4159	0.4133	0.4072	0.4124	0.414
				RankBoost	0.4632	0.4504	0.4555	0.4543	0.4494	0.4439	0.4412	0.4361	0.4326	0.4302
				ListNet	0.5326	0.481	0.4732	0.4561	0.4432	0.44	0.4409	0.446	0.4459	0.441
				AdaRank-MAP	0.5388	0.4789	0.4682	0.4721	0.4613	0.4579	0.4558	0.4506	0.4464	0.4429
				*/
				
				/*
				P@1	P@2	P@3	P@4	P@5	P@6	P@7	P@8	P@9	P@10
				RankSVM	0.5974	0.5494	0.5427	0.5443	0.5319	0.5253	0.5097	0.4933	0.492	0.4864
				RankBoost	0.5576	0.5481	0.5609	0.558	0.5447	0.5297	0.5241	0.513	0.5024	0.4966
				ListNet	0.6524	0.6093	0.6016	0.5745	0.5502	0.5373	0.5267	0.5236	0.5138	0.4975
				AdaRank-MAP	0.6338	0.5959	0.5895	0.5887	0.5674	0.5566	0.5392	0.5239	0.5077	0.4976
				*/
				/*
				MAP
				RankSVM	0.4334
				RankBoost	0.4411
				ListNet	0.4457
				AdaRank-MAP	0.4487
				*/
				String fenge = " ";
				//��ȡ����������ʵ���ʱ��Ŀ¼
				File main = new File(predictionDir.getText());
				File[] dataDirs = main.listFiles();
				if(dataDirs.length!=1)
					try {
						throw new Exception("ֻ������1�����ݼ�Ŀ¼��")	;
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				File[] timeDirs = dataDirs[0].listFiles();
				//����ÿһ��ʵ����Ŀ¼
				outputWindow.append("���������� "+timeDirs.length+" ��ʵ�飡\n");
				for(File timeDir:timeDirs)
				{
					outputWindow.append("##########���ڹ�ע "+timeDir.getName()+" ����ʵ��Ľ��##########\n");
					//��ȡĿ¼�µ�finalresult.txt
					File finalresult = new File(timeDir.getAbsoluteFile()+"\\finalresult.txt");
					//��ȡ�ļ�����ȡp,map��ndcg����
					String res = FileUtils.read(finalresult.getAbsolutePath(), "utf-8");
					outputWindow.append("----------��ȡRankBCA�ı��ֽ����----------\n"+res);
					ArrayList<String> metricList= new ArrayList<String>();
					BufferedReader br = new BufferedReader(new StringReader(res));
					String line;
					//����2��double���飬�洢p,ndcg,0��Ԫ����֮���á�1��double�������洢map
					
					try {
						while((line = br.readLine())!=null)
							metricList.add(line);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					double[] p= new double[11];//��1��ʼ
					double[] ndcg = new double[11];//��1��ʼ
					
				//��һ����precision
						String ps = metricList.get(0);
						String[] strs_p = ps.split(fenge);
						for(int k = 1;k<=10;k++)
							p[k] = Double.parseDouble(strs_p[k]);
							
						
				//�ڶ�����Map
						String map_str = metricList.get(1);
						String[] strs_map = map_str.split(fenge);
						double map = Double.parseDouble(strs_map[1]);
					
				//��������NDCG
							String ndcgs = metricList.get(2);
							String[] strs_ndcg = ndcgs.split(fenge);
							for(int k = 1;k<=10;k++)
								ndcg[k] = Double.parseDouble(strs_ndcg[k]);		
					
					
					//p1-p10,ndcg1-ndcg10,map�Ѿ�׼�����������洴��һ����ͼ���ڽ��л�ͼ
							
				outputWindow.append("\n----------��ȡ�������ݵı��ֽ����----------\n");	
							//������ͼ������
				//��״ͼ��˳���ǣ�RankSVM RankBoost ListNet AdaRank-MAP RankBCA				
				
				
				double[][] _ndcg = {
						{0.4958,0.4632,0.5326,0.5388},
						{0.4331,0.4504,0.481,0.4789},
						{0.4207,0.4555,0.4732,0.4682},
						{0.424,0.4543,0.4561,0.4721},
						{0.4164,0.4494,0.4432,0.4613},
						{0.4159,0.4439,0.44,0.4579},
						{0.4133,0.4412,0.4409,0.4558},
						{0.4072,0.4361,0.446,0.4506},
						{0.4124,0.4326,0.4459,0.4464},
						{0.414,0.4302,0.441,0.4429}
						};
				double[][] _p	={
						{0.5974,0.5576,0.6524,0.6338},
						{0.5494,0.5481,0.6093,0.5959},
						{0.5427,0.5609,0.6016,0.5895},
						{0.5443,0.558,0.5745,0.5887},
						{0.5319,0.5447,0.5502,0.5674},
						{0.5253,0.5297,0.5373,0.5566},
						{0.5097,0.5241,0.5267,0.5392},
						{0.4933,0.513,0.5236,0.5239},
						{0.492,0.5024,0.5138,0.5077},
						{0.4864,0.4966,0.4975,0.4976}
						};
				double[][] final_ndcg = new double[10][5];
				double[][] final_p = new double[10][5];
				
				for(int i = 0;i<10;i++)
				{	for(int j =0;j<4;j++)
					{
						final_ndcg[i][j] = _ndcg[i][j];
						final_p[i][j] = _p[i][j];
					}
					final_ndcg[i][4] = ndcg[i+1]; 
					final_p[i][4] = p[i+1];
				}
							
							try {
								Draw draw_ndcg = new Draw();
								Draw draw_p = new Draw();
								outputWindow.append("���ڻ���NDCG�Ƚ�ͼ...\n");
								draw_ndcg.drawNDCG(final_ndcg);
								draw_ndcg.waitForFigures();
								outputWindow.append("���ڻ���Precision�Ƚ�ͼ...\n");
								draw_p.drawP(final_p);
								draw_p.waitForFigures();
								outputWindow.append("��ͼ����...\n");
							} catch (MWException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					
					
					
					
				}
				
				
				
				
				
				
				
				
				
				
			}
		});
		btnNewButton_1.setBounds(410, 147, 115, 23);
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		btnNewButton_2.setBounds(229, 57, 93, 23);
		panel_1.add(btnNewButton_2);

		outputWindow = new TextArea();
		outputWindow.setBounds(10, 398, 665, 225);
		contentPane.add(outputWindow);
	}
}
