package test;

import java.io.BufferedReader;

import java.io.InputStream;

import java.io.InputStreamReader;

public class ExecPerl {

	public static void exeEvaluation(String perBinDir,String perScriptPath, String testPath, String predictionPath,String outputPath) {

		String[] cmd = {perBinDir, perScriptPath, testPath,	predictionPath,outputPath,"0"};
		StringBuffer resultStringBuffer = new StringBuffer();
		String lineToRead = "";
		// get Process to execute perl, get the output and exitValue
		int exitValue = 0;
		try {

			Process proc = Runtime.getRuntime().exec(cmd);
			InputStream inputStream = proc.getInputStream();
			BufferedReader bufferedRreader = new BufferedReader(
					new InputStreamReader(inputStream));

			// save first line
			if ((lineToRead = bufferedRreader.readLine()) != null) {
				resultStringBuffer.append(lineToRead);
			}
			// save next lines
			while ((lineToRead = bufferedRreader.readLine()) != null) {
				resultStringBuffer.append("\r\n");
				resultStringBuffer.append(lineToRead);
			}
		// Always reading STDOUT first, then STDERR, exitValue last
			proc.waitFor(); // wait for reading STDOUT and STDERR over
			exitValue = proc.exitValue();
		} catch (Exception ex) {
			resultStringBuffer = new StringBuffer("");
			exitValue = 2;
		}
		System.out.println("exit:" + exitValue);
		System.out.println(resultStringBuffer.toString());
	}

}
