package com.tiansu.eam.common.utils.excel.servlet;

import com.oreilly.servlet.MultipartRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

@SuppressWarnings("serial")
@Component()
public class FileUploadServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().print("STOP!");
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		char fg = File.separatorChar;
		String path = this.getServletContext().getRealPath( "/")+"file";
		int maxPostSize = 1 * 100 * 1024 * 1024;
		MultipartRequest mr = new MultipartRequest(req, path, maxPostSize,
				"UTF-8");
		Enumeration files = mr.getFileNames();
		while (files.hasMoreElements()) {
			String name = (String) files.nextElement();
			File f = mr.getFile(name);
			if (f != null) {
				String fileName = mr.getFilesystemName(name);
				UUID uuid = UUID.randomUUID();
				String uid = uuid.toString().replaceAll("-", "").toLowerCase();
				String type = fileName.substring(fileName.lastIndexOf("."),
						fileName.length());
				File fs = new File(path + fg + fileName);
				File fsto = new File(path + fg + uid + type);
				fs.renameTo(fsto);
				fs.delete();
				String json="{'uid':'"+uid+"','type':'"+type+"','fileName':'"+fileName.split("\\.")[0]+"'}";
				resp.getWriter().write(json);
			}
			break;
		}
	}
}
