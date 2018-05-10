package com.tiansu.eam.common.utils.excel.servlet;

import com.tiansu.eam.common.utils.excel.ExcelConfigParser;
import com.tiansu.eam.common.utils.excel.ExcelUtil;
import com.tiansu.eam.common.utils.excel.UploadExcel;
import com.tiansu.eam.common.utils.excel.dao.BatchDao;
import com.tiansu.eam.common.utils.excel.dao.ConvertDataDao;
import com.tiansu.eam.common.utils.excel.entity.TsCustomer;
import com.tiansu.eam.common.utils.excel.model.ExcelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component()
public class ImportExcelServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7468411283230026793L;

	@Autowired
	private ConvertDataDao convertDataService;

	@Resource
	private BatchDao batchDao;
	/*@Resource
	private CrudService DatabaseUtil;*/

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		doPost(request,response);
	}
	

	


	@Transactional
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		Map<String, Object> map = request.getParameterMap();
		int readLineLog = 0;
		String err_line = "";
		Map<String, Object> param=new HashMap<String, Object>();//入参对象
		List<TsCustomer>  customers=new ArrayList<TsCustomer>();
		//上传文件到服务器
		String name = request.getParameter("name");
		String excelPath=this.getServletContext().getRealPath("/file/"+name);
		String[] a=(String[]) map.get("a");
		
		if (excelPath != null || !"".equals(excelPath)) {
			// 解析excel
			List<String[]> myList = UploadExcel.readExcelsByPoi(excelPath, "Sheet1");
			if(myList == null){
				 response.getWriter().write("数据读取失败，请检查工作表名称是否为【Sheet1】");
			}

			ExcelConfig excelConfig = ExcelConfigParser.getConfigById(a[0]);
			try{
				if(a[0].equals("customer")) {
					for (int i = 0; i < myList.size(); i++) {
						readLineLog = i + 3;//从第3行算第一行;
						err_line = "第" + readLineLog + "行";
						String[] values = myList.get(i);
						if (excelConfig != null) {
							//excel value从第1个索引开始，第0个统一为序号列
							int startIndex = 1;
							TsCustomer cus = (TsCustomer) ExcelUtil.convertToBean(excelConfig, values, new TsCustomer(), startIndex);
							customers.add(cus);


						}
					}
				}
				Map<String,Object> insertParam=new HashMap<String,Object>();
				insertParam.put("models",customers);
				batchDao.insertCustomer(insertParam);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				response.getWriter().write(err_line+"导入失败["+e.getMessage()+"]");
				return;
			}
			}else{
				response.getWriter().write("文件解析失败");
			}
		}
}
