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
import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.infrastructure.OptResult;
import cn.xhh.infrastructure.Utils;
import sun.misc.BASE64Decoder;

@Service
public class AttachServiceImpl implements AttachService {

	@Value("realPath")
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
	public OptResult upload(String base64Img,int sort, int sourceId, String sourceName) {
		if (base64Img == null)
			return OptResult.Failed("上传的图片不存在");

		OptResult result = spBase64(base64Img);
		if (result.getCode() != 0)
			return result;

		result = suffix();
		if (result.getCode() != 0)
			return result;

		newSavePath();
		try {
			uploader();
		} catch (Exception e) {
			return OptResult.Failed("上传失败");
		}
		Attach attach = new Attach();
		attach.setSourceId(sourceId);
		attach.setSourceName(sourceName);
		attach.setSort(sort);
				
		attach.setExt(suffix);
		attach.setPath(url);
		attach.setFileName(fileName);

		attach.setTenantId(SessionManager.getTenantId());
		attach.setUploadId(SessionManager.getUserId());
		attach.setAddTime(new Date());

		if (attachRepository.add(attach) > 0)
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
		File temp = new File(realPath + tempPath.toString());
		if (!temp.exists())
			temp.mkdirs();
		filePath = realPath + tempPath + "/" + filePath;

		url = "/Upload" + tempPath + "/" + filePath;
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
