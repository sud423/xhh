package cn.xhh.application.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.xhh.application.AttachService;
import cn.xhh.domain.business.Attach;
import cn.xhh.domain.business.AttachRepository;
import cn.xhh.infrastructure.OptResult;
import cn.xhh.infrastructure.Utils;
import sun.misc.BASE64Decoder;

@Service
public class AttachServiceImpl implements AttachService {

	@Value("${realPath}")
	private String realPath;

	private String fileName;

	private String filePath;

	private String dataPrix = null;

	private String data = null;

	private String suffix = null;

	private String url = null;

	@Autowired
	private AttachRepository attachRepository;

	@Override
	public OptResult upload(String base64Img, int sort, int sourceId, String sourceName, int userId, int tenantId) {
		if (base64Img == null)
			return OptResult.Successed("上传的图片不存在");
		//
		if(base64Img.indexOf("base64,")<0)
			return OptResult.Successed("上传的图片格式不正确");
		
		OptResult result = spBase64(base64Img);
		if (result.getCode() != 0)
			return result;

		result = suffix();
		if (result.getCode() != 0)
			return result;
		Attach attach = attachRepository.get(sourceId, sourceName, sort);

		if (attach == null) {
			attach = new Attach();
			newSavePath();
		}			
		else {
			/*删除/替换原文件*/
			rpSavePath(attach.getExt(), attach.getPath(), attach.getFileName());
		}
		try {
			uploader();
		} catch (Exception e) {
			return OptResult.Failed("上传失败");
		}
		attach.setSourceId(sourceId);
		attach.setSourceName(sourceName);
		attach.setSort(sort);

		attach.setExt(suffix);
		attach.setPath(url);
		attach.setFileName(fileName);

		attach.setTenantId(tenantId);
		attach.setUploadId(userId);
		attach.setAddTime(new Date());
		attach.setLastUpdateTime(new Date());

		int r = 0;

		if (attach.getId() > 0) {
			r = attachRepository.update(attach);
		} else {
			r = attachRepository.add(attach);
		}
		if (r > 0)
			return OptResult.Successed();

		return OptResult.Failed("上传失败");

	}

	/**
	 * 分割base64字符串
	 * 
	 * @param base64Img
	 * @return
	 */
	private OptResult spBase64(String base64Img) {
		String[] d = base64Img.split("base64,");
		if (d != null && d.length == 2) {
			dataPrix = d[0];
			data = d[1];
			return OptResult.Successed();
		} else {
			return OptResult.Failed("上传失败，图片格式不合法");
		}
	}

	/**
	 * 获取图片扩展名
	 * 
	 * @return
	 */
	private OptResult suffix() {
		if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {// data:image/jpeg;base64,base64编码的jpeg图片数据
			suffix = ".jpg";
		} else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {// data:image/x-icon;base64,base64编码的icon图片数据
			suffix = ".ico";
		} else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {// data:image/gif;base64,base64编码的gif图片数据
			suffix = ".gif";
		} else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {// data:image/png;base64,base64编码的png图片数据
			suffix = ".png";
		} else {
			return OptResult.Failed("上传失败，图片格式不合法");
		}

		return OptResult.Successed();
	}

	/**
	 * 保存文件路径及文件图片地址
	 */
	private void newSavePath() {
		// 临时文件名称
		fileName = Utils.generateCode() + suffix;
		StringBuilder tempPath = new StringBuilder();
		tempPath.append("/image/");
		SimpleDateFormat folderNameFormat = new SimpleDateFormat("yyyyMM");
		tempPath.append(folderNameFormat.format(new Date()));
		File file = new File(realPath + tempPath.toString());
		if (!file.exists())
			file.mkdirs();
		filePath = realPath + tempPath.toString() + "/" + fileName;

		url = "/Upload" + tempPath.toString() + "/" + fileName;
	}
	
	/**
	 * 对比扩展名是否一致，如果不一致就替换掉
	 * @param ext
	 */
	private void rpSavePath(String ext,String filePa,String fileNa) {
		if(suffix.toLowerCase()==ext.toLowerCase())
			return;
		url=filePa.replace(ext, suffix);
		fileName=fileNa.replace(ext, suffix);
		String tempPath=url.replace("/Upload","").replace(fileName, "");
		filePath = realPath + tempPath + "/" + fileName;
		File file=new File(realPath + tempPath + "/" + fileNa);
        if(file.exists()&&file.isFile())
            file.delete();
	}

	private void uploader() throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		// 解密
		byte[] b = decoder.decodeBuffer(data);
		// 处理数据
		for (int i = 0; i < b.length; ++i) {
			if (b[i] < 0) {
				b[i] += 256;
			}
		}
		OutputStream out = new FileOutputStream(filePath);
		out.write(b);
		out.flush();
		out.close();

	}
}
