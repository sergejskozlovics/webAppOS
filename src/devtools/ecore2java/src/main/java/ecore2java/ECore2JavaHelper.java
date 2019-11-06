package ecore2java;

import java.io.*;

import ecore2java.main.Generate;

public class ECore2JavaHelper {
	public boolean fileExists(String fileName)
	{
		return new File(Generate.TARGET_FOLDER+File.separator+fileName).exists();
	}

}
