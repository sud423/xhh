package cn.xhh.infrastructure;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
import java.util.UUID;

import sun.misc.BASE64Decoder;

public class Utils {

	public static void main(String[] args) {
//		List<String> l = new ArrayList<String>();
//		for (int i = 0; i < 10000; i++) {
//			l.add(generateCode());
//		}
//
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		for (String item : l) {
//			if (map.containsKey(item)) {
//				map.put(item, map.get(item).intValue() + 1);
//			} else {
//				map.put(item, new Integer(1));
//			}
//		}
//		Iterator<String> keys = map.keySet().iterator();
//		while (keys.hasNext()) {
//			String key = keys.next();
//			System.out.println(key + "重复" + map.get(key).intValue() + "次");
//		}

	}

	public static String generateCode() {
		long now = System.currentTimeMillis();// 一个13位的时间戳

		int hashCode = Math.abs(UUID.randomUUID().toString().hashCode());

		return getPeriod() + String.format("%011d", hashCode) + now;
	}

	public static String getPeriod() {
		SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
		String format2 = format.format(new Date());
		return format2;
	}

	/**
	 * 根据Key读取全局配置文件的value
	 * 
	 * @param key
	 * @return
	 */
//	public static String getValueByKey(String filePath, String key) {
//
//		Properties prop = new Properties();
//		String value = null;
//		try {
//			// 通过输入缓冲流进行读取配置文件
//			InputStream inputStream = new BufferedInputStream(
//					new FileInputStream(new File("src/main/resources/" + filePath)));
//			// 加载输入流
//			prop.load(inputStream);
//			// 根据关键字获取value值
//			value = prop.getProperty(key);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return value;
//	}

	/**
	 * @Description: 将base64编码字符串转换为图片
	 * @Author:
	 * @CreateTime:
	 * @param imgStr base64编码字符串
	 * @param path   图片路径-具体到文件
	 * @return
	 * @throws Exception 
	 */
	public static void generateImage(String base64Img, String path) throws Exception {
		if (base64Img == null)
			return;
		
		int istart=base64Img.indexOf("base64,");
		if(istart>-1)
			base64Img=base64Img.substring(istart + 7);
		
		
        String suffix = "";
        if("data:image/jpeg;".equalsIgnoreCase(base64Img)){//data:image/jpeg;base64,base64编码的jpeg图片数据
            suffix = ".jpg";
        } else if("data:image/x-icon;".equalsIgnoreCase(base64Img)){//data:image/x-icon;base64,base64编码的icon图片数据
            suffix = ".ico";
        } else if("data:image/gif;".equalsIgnoreCase(base64Img)){//data:image/gif;base64,base64编码的gif图片数据
            suffix = ".gif";
        } else if("data:image/png;".equalsIgnoreCase(base64Img)){//data:image/png;base64,base64编码的png图片数据
            suffix = ".png";
        }else{
            throw new Exception("上传图片格式不合法");
        }
		
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// 解密
			byte[] b = decoder.decodeBuffer(base64Img);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
		} catch (Exception e) {
			throw e;
		}
	}
}
