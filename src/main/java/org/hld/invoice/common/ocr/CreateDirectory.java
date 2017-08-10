package org.hld.invoice.common.ocr;

import org.hld.invoice.common.utils.HashUtil;

import java.io.File;


public class CreateDirectory {
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		String md5destDirName= HashUtil.md5Hex(destDirName);
		if (dir.exists()) {// 判断目录是否存在
			System.out.println("Failed to create directory. The destination directory already exists!");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
			destDirName = destDirName + File.separator;
		}
		if (dir.mkdirs()) {// 创建目标目录
			System.out.println("Create directory success!" + md5destDirName);
			return true;
		} else {
			System.out.println("Failed to create directory!");
			return false;
		}
	}

}
