/**
 * 프로그램명 : 
 * 작성자     : 김형진(khj1682@lycos.co.kr)
 * 설명       : 파일 컨틀롤 관련 method 제공
 *
 *******************************************************************
 * 작성일자		작성자	내용
 * -----------------------------------------------------------------
 * 2010/05/28	김형진	최초작성
 ******************************************************************* 
 **/
package com.madive.bigcommerce.madiveone.admin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
	@SuppressWarnings("unused")
	private static Logger L = LoggerFactory.getLogger(FileUtils.class);

	public static final String[] allowExtName = {"jpg", "jpeg", "png", "svg", "xls", "xlsx", "doc", "docx", "ppt", "pptx", "pdf", "hwp"};

	/**
	 * 업로드된 파일을 임시 폴더에 전달된 파일명으로 저장한다. Return value가 0 이상이면 성공
	 * 
	 * @param FormFile formFile : 업로드 파일 객체
	 * @param String   filename : 저장할 파일 경로명
	 * @return int
	 */
	public static int copyFormFile(File formFile, String filename) {
		L.debug("copyFormFile() filename=[{}]", filename);

		int n, nRead = 0;
		byte buffer[] = new byte[4096];
		InputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(formFile);
			L.debug("copyFormFile(), file open, input, upfile=[" + fis + "]");

			fos = new FileOutputStream(filename);
			L.debug("copyFormFile(), file open, output");

			while ((n = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, n);
				nRead += n;
			}
		}
		catch (FileNotFoundException e) {
			L.error("copyFormFile(), 파일 쓰기 오류 filename= [" + filename + "]");
			L.error("copyFormFile()" + e);
			return -1;
		}
		catch (IOException e) {
			L.error("copyFormFile(), 파일 쓰기 오류 filename= [" + filename + "]");
			L.error("copyFormFile(), " + e);
			return -1;
		}
		finally {
			try {
				fis.close();
				fos.flush();
				fos.close();
			}
			catch (IOException e) {}
		}

		return nRead;
	} // end of copyFormFile()

	/**
	 * 임시 폴더에 있는 파일을 저장하고자 하는 폴더로 이동시킴 file : 임시 폴더내의 파일 newFile : 저장하고자 하는 위치의 파일
	 * 
	 * @param file
	 * @param newFile
	 * @return
	 */
	public static boolean copyFile(File file, File newFile) {
		// 기존에 파일이 존재하면 삭제하고 다시 넣기
		if (newFile.exists()) {
			newFile.delete();
		}

		if (file.renameTo(newFile))
			return true;

		return false;
	} // end of copyFormFile()

	/* */
	public static String include(String fname) {
		int ch;
		StringBuffer sb = new StringBuffer();
		FileReader fr = null;

		try {
			fr = new FileReader(fname);

			while ((ch = fr.read()) > 0) {
				sb.append((char) ch);
			}

			fr.close();
		}
		catch (IOException e) {
			L.error("include(), e=" + e);
			return "";
		}

		return sb.toString();
	}

	/* */
	public static String include2(String fname) {
		String buf = null;
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(fname));

			while ((buf = br.readLine()) != null) {
				sb.append(buf);
			}

			br.close();
		}
		catch (IOException e) {
			L.error("include(), e=" + e);
			return "404 Not Found";
		}

		return sb.toString();
	}

	/**
	 * 파일명를 주면, 파일확장자만을 리턴한다.
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileExtname(String file) {
		String[] arrStr = file.split("\\.");
		return arrStr[arrStr.length-1];
	}

	/**
	 * 파일 경로를 주면, "/" 문자로 구분하여, 파일이름만을 리턴한다.
	 * 
	 * @param file
	 * @return
	 */
	public static String getFilename(String file) {
		return getFilename(file, "/");
	}

	/**
	 * 파일 경로를 주면, sep 문자로 구분하여, 파일이름만을 리턴한다. 경로구분자가 주어지지 않으면, "/" 를 기본값으로 한다.
	 */
	public static String getFilename(String file, String sep) {

		if (StringUtils.length(file) <= 0)
			return "";

		sep = StringUtils.defaultIfEmpty(sep, "/");

		return file.substring(file.lastIndexOf(sep) + 1);

	}

	/**
	 * 파일 경로를 주면 해당 경로의 파일을 삭제한다 .
	 * 
	 * @param filepath
	 * @return
	 */
	public static boolean deleteFile(String filepath) {

		File f = new File(filepath);
		return f.delete();

	}

	/**
	 * 파일이름을 넣으면 파일이름의 공백을 '_'로 바꿔준다.
	 * 
	 * @param file
	 * @param sep
	 * @return
	 */
	public static String replaceSpace(String file) {

		String newFile = "";
		if (StringUtils.length(file) <= 0)
			return "";

		newFile = file.trim().replace(' ', '_');
		return newFile;

	}

	public static String getDisposition(String filename, String browser) throws Exception {
		String dispositionPrefix = "attachment;filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		}
		else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		}
		else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		}
		else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				}
				else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		}
		else {
			throw new RuntimeException("Not supported browser");
		}

		return dispositionPrefix + encodedFilename;
	}

	public static String getBrowser(HttpServletRequest request) {

		String header = request.getHeader("User-Agent");

		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		}
		else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		}
		else if (header.indexOf("Opera") > -1) {
			return "Opera";
		}

		return "Firefox";
	}

	/**
	 * 파일이름을 넣으면 중복없는 유일 파일 이름을 반환
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getUniqueFileName(String fileName) {
		String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		fileName = Long.toString(System.currentTimeMillis());
		fileName += Long.toString((long) (Math.random() * 100000)) + fileType;
		return fileName;
	}

	/**
	 * 파일이름을 넣으면 중복없는 유일 파일 이름을 반환
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getUniqueFileID() {
		return Long.toString(System.currentTimeMillis()) + Long.toString((long) (Math.random() * 100000));
	}

	/**
	 * 업로드 가능 확장자 여부
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isAllowExt(String extName) {
		boolean result = false;

		for(String item : allowExtName){
			if (item.equals(extName.toLowerCase())) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * 업로드 가능 확장자 여부
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isAllowExtForFileName(String fileName) {
		String extName = getFileExtname(fileName);
		return isAllowExt(extName);
	}
	
	/**
	 * 바이너리 스트링을 파일로 변환
	 *
	 * @param binaryFile
	 * @param filePath
	 * @param fileName 
	 * @return
	 */
	public static File binaryToFile(String binaryFile, String filePath, String fileName) {
	    if ((binaryFile == null || "".equals(binaryFile)) || (filePath == null || "".equals(filePath))
	            || (fileName == null || "".equals(fileName))) { return null; }
	 
	    FileOutputStream fos = null;
	 
	    File fileDir = new File(filePath);
	    if (!fileDir.exists()) {
	        fileDir.mkdirs();
	    }
	 
	    File destFile = new File(filePath + File.separatorChar + fileName);
	 
	    byte[] buff = binaryFile.getBytes();
	    String toStr = new String(buff);
	    byte[] b64dec = base64Dec(toStr);
	 
	    try {
	        fos = new FileOutputStream(destFile);
	        fos.write(b64dec);
	        fos.close();
	    } catch (IOException e) {
	        System.out.println("Exception position : FileUtil - binaryToFile(String binaryFile, String filePath, String fileName)");
	    }
	 
	    return destFile;
	}
	
	static byte[] base64Enc(byte[] buffer) {
		return Base64.encodeBase64(buffer);
	}
	
	static byte[] base64Dec(String str) {
		return Base64.decodeBase64(str);
	}
}
