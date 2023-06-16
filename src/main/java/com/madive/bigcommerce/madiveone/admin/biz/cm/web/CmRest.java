package com.madive.bigcommerce.madiveone.admin.biz.cm.web;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.madive.bigcommerce.madiveone.admin.biz.cm.service.AttachFileService;
import com.madive.bigcommerce.madiveone.admin.domain.AttachFile;
import com.madive.bigcommerce.madiveone.admin.domain.Result;
import com.madive.bigcommerce.madiveone.admin.util.FileUtils;
import com.madive.bigcommerce.madiveone.admin.util.StringUtils;

@RestController
@RequestMapping("/")
public class CmRest {
	
	private static Logger log = LoggerFactory.getLogger(CmRest.class);
	
	@Autowired
	private AttachFileService attachFileService;
	
	@Value("${upload.path}")
	private String UPLOAD_PATH;
	
	@PostMapping("cm/upload")
	public Result upload(
			@RequestParam MultipartFile[] files,
			@RequestParam Map<String, String> parameter
			) {
		log.debug("## files = {}", files.toString());
		log.debug("## parameter = {}", parameter);
		
		try {
			List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
			
			for (MultipartFile item : files){
				if (item == null || item.isEmpty())
					continue;
	
				String orgFileName = item.getOriginalFilename();
				
				if(!FileUtils.isAllowExtForFileName(orgFileName)) {
					return Result.FAILURE("File type not allowed");
				}
			}
				
			for (MultipartFile item : files){
				if (item == null || item.isEmpty())
					continue;
	
				String savePath = StringUtils.nvl(parameter.get("path"), "temp");
				File path = new File(UPLOAD_PATH + File.separatorChar + savePath);
				if(!path.exists()){
					if (path.mkdirs() == Boolean.FALSE){
						throw new RuntimeException("upload directory create failure.");
					}
				}
				
				String fileID = FileUtils.getUniqueFileID();
				File file = new File(UPLOAD_PATH + File.separatorChar + savePath + File.separatorChar + fileID);
				
				item.transferTo(file);
				
				if(file.exists()) {
					Map<String, String> obj = new HashMap<String, String>();
					
					obj.put("isNew", "true");
					obj.put("fileName", item.getOriginalFilename());
					obj.put("fileId", fileID);
					
					fileList.add(obj);
				}
			}
			
			return Result.LIST(fileList);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return Result.FAILURE("File Upload process failed");
	}
	
	@PostMapping("cm/filedown")
	@ResponseBody
	public FileSystemResource download(
			@RequestParam String fileId,
			HttpServletRequest request,
			HttpServletResponse response) {
		log.debug("download() == {}", fileId);
		AttachFile file = attachFileService.getData(fileId);
		log.debug("file == {}", file);
		File f = new File(UPLOAD_PATH + File.separatorChar + file.getAf10SavePath());
		String fileName = file.getAf10OrgName();
		String userAgent = (String)request.getHeader("User-Agent");
		try{
			boolean ie = (userAgent.indexOf("MSIE") > -1) || (userAgent.indexOf("Trident") > -1);
			if (ie) {
			        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else {
			        fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
			}
		}catch(Exception e){}
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		return new FileSystemResource(f);
	}
}