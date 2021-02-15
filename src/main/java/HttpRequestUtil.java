import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;



public class HttpRequestUtil {

	/**
	 *
	 * @param localServerIp        本域server IP地址
	 * @param destServerIp         目标域server IP地址
	 * @param destIp               目标ip地址
	 * @param destFileSavePath     目标文件保存路径
	 * @param filePath             发送文件所在路径
	 * @param fileName             发送文件名称
	 * @param noticeInterface      通知接口
	 */
	public static String sendFileToOtherRegion(String localServerIp,String destServerIp,String destIp, String destFileSavePath, String filePath, String fileName,String noticeInterface){
		JSONObject postData = new JSONObject();
		postData.put("destServerIp",destServerIp);
		postData.put("destIp",destIp);
		postData.put("destFileSavePath",destFileSavePath);
		postData.put("fileName",fileName);
		postData.put("noticeInterface",noticeInterface);

		String url = "http://"+ localServerIp +"/clientserver/interfaceSendFileToOtherRegion";
		String  res =sendPostUplodFile(url,filePath+"/"+fileName, postData.toJSONString());
		return res;
	}

	public static String sendPostUplodFile(String url, String path, String params) {
		DataOutputStream out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			//打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			//发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			String BOUNDARY = "----WebKitFormBoundary07I8UIuBx6LN2KyY";
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			//conn.setRequestProperty("user-agent", "Mozilla/4.0 (conpatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			conn.connect();

			out = new DataOutputStream(conn.getOutputStream());
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			//添加参数
			StringBuffer sb1 = new StringBuffer();
			sb1.append("--");
			sb1.append(BOUNDARY);
			sb1.append("\r\n");
			sb1.append("Content-Disposition: form-data;name=\"sendFileInfo\"");
			sb1.append("\r\n");
			sb1.append("\r\n");
			sb1.append(params);
			sb1.append("\r\n");
			out.write(sb1.toString().getBytes());
			//添加参数file
			File file = new File(path);
			StringBuffer sb = new StringBuffer();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"");
			sb.append("\r\n");
			sb.append("Content-Type: application/octet-stream");
			sb.append("\r\n");
			sb.append("\r\n");
			out.write(sb.toString().getBytes());

			DataInputStream in1 = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in1.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			out.write("\r\n".getBytes());
			in1.close();
			out.write(end_data);

			//flush输出流的缓冲
			out.flush();
			//定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//log.error("发送POST请求出现异常" + e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
		}
		return result;
	}
}
