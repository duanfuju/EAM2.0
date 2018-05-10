package com.tiansu.eam.common.utils.excel;

import com.tiansu.eam.common.utils.RegularUtils;
import com.tiansu.eam.common.utils.excel.model.ExcelConfig;
import com.tiansu.eam.common.utils.excel.model.ExcelFieldModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @creator duanfuju
 * @createtime 2017/8/28 9:08
 * @description: 数据验证拦截器
 *
 */
public class ImportInterceptor implements HandlerInterceptor {
    //文件的名称
    private static String txt_fileName="";


    //生成文件路径
    private static String txt_path = "";

    //文件路径+名称
   // private static String txt_filenameTemp="";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //判断是否进行导入
        boolean flag=true;
        //返回信息
        String msg="";
        //错误单元格收集
        List<Map<Integer,Integer>> errorCells=new ArrayList<Map<Integer,Integer>>();
        PrintWriter write = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");


        try {
            Map<String, Object> map = request.getParameterMap();
            String classType=((String[]) map.get("a"))[0];
            //获取当前传入的excel文件中数据所要传入的类的类型
            ExcelConfig excelConfig = ExcelConfigParser.getConfigById(classType);
            String sheetName=excelConfig.getConfigModel().getExportFileName();
            //获取文件的名称
           // fileName=request.getParameter("fileName");
            //获取上传文件的真实名称
            String name=request.getParameter("name");
            //获取文件的名称 用于创建txt文件
            txt_fileName=name.split("\\.")[0];
            //解析文件
            String excelPath= request.getSession().getServletContext().getRealPath("/file/"+name);
            //获取文件的路径 用于创建txt文件
            txt_path=request.getSession().getServletContext().getRealPath("/file/");
            //获取文件中的数据，如果报错，则将所有的错误记录下来
            //操作服务器中的文件标红
            if (excelPath != null || !"".equals(excelPath)) {
                // 解析excel
                List<String[]> myList = UploadExcel.readExcelsByPoi(excelPath, sheetName);
                if (myList == null) {
                    flag=false;
                    msg="数据读取失败，请检查工作表名称是否为【"+sheetName+"】<br/>";
                }
                if (myList.size()==0){
                    flag=false;
                    msg="数据读取失败，请检查工作表【"+sheetName+"】是否有数据<br/>";
                }
                if(myList.size()>5000){
                    msg="数据读取失败，请检查工作表【"+sheetName+"】数据数量是否超过5000（excel清行空数据时请删除整行）<br/>";
                    write.print(msg);
                    createFile(txt_fileName,msg);
                    return false;
                }

                //获取当前类所有的字段类型和属性
                List<ExcelFieldModel> fields = excelConfig.getConfigModel().getFields();




                //根据传入的类型进行循环判断
                for (int i = 0; i < myList.size(); i++) {
                    //代表一行的数据
                    String[] values = myList.get(i);
                    for (int j=0,k=1;j<values.length-1;j++,k++){
                        try{
                            //索引从1开始
                            Object value=values[k];
                            //values的数据和fields的数据的顺序是一致的
                            ExcelFieldModel field=fields.get(j);
                            if(StringUtils.isEmpty((String) value) && StringUtils.isNotEmpty(field.getDefaultVal())){
                                //当前值为空并且默认值不为空的情况，则赋默认值
                                    value = field.getDefaultVal();
                            }
                            if("".equals(value)){
                                value = null;//不要把数据库里的null替换为“”
                                if(!field.isNullbale()){//当前值为空并且当前字段不能为空时
                                    flag=false;
                                    msg=msg+"第"+(i+3)+"行,第"+k+"列:[当前值{"+field.getShowName()+"}不能为空]<br/>";

                                }
                            }
                            //判断是否是符合规范
                            if (StringUtils.isNotEmpty(field.getType())&& StringUtils.isNotEmpty((String) value)){
                                String regex= RegularUtils.getRegular(field.getType());
                                /*if (StringUtils.isEmpty(regex)){
                                    flag=false;
                                    msg=msg+"第"+(i+3)+"行,第"+j+"列:[当前值的正则不存在,请联系开发人员检查配置文件]\n";
                                    continue;
                                }*/

                                if ((!((String) value).matches(regex))&&(StringUtils.isNotEmpty(regex))){
                                    flag=false;
                                    msg=msg+"第"+(i+3)+"行,第"+k+"列:[当前值{"+field.getShowName()+"}不符合规范,请检查]<br/>";

                                }
                            }
                        }catch (Exception e){
                            //校验出错时:①不进行导入操作,②打印错误到控制台,③记录失败原因,④记录失败行和列,⑤跳出当前循环
                            flag=false;
                            e.printStackTrace();
                            msg=msg+"第"+(i+3)+"行,第"+k+"列:["+e.getMessage()+"]<br/>";
                            continue;
                        }
                    }
                }
            }
            request.getSession().setAttribute("importInterceptorFlag", flag);
            request.getSession().setAttribute("importInterceptorPath", txt_path);
            if (flag){
                //验证通过，进行导入操作
                write.print("success");
                return flag;
            }else{

                //验证失败，进行错误导出，以及重新下载无效的文件返回给客户
                write.print(msg);
               createFile(txt_fileName,msg);
                return flag;
            }
        } catch (Exception e) {
            flag=false;
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String strs = sw.toString();
            msg+=strs;
            createFile(txt_fileName,msg);
            write.print(msg);
            return flag;
        } finally {
            if (write != null)
                write.close();
        }


    }

    /**
     * 创建文件（txt文件，用于记录错误的）
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public static boolean createFile(String fileName,String filecontent){

        //将文本中的<br/> 换行替换成\r\n
        filecontent=filecontent.replaceAll("<br/>","\r\n");
        Boolean bool = false;
       String  txt_filenameTemp = txt_path+txt_fileName+".txt";//文件路径+名称+文件类型
        File file = new File(txt_filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+txt_filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(txt_filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }










































    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
