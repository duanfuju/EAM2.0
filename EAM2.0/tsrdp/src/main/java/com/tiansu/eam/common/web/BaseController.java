/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.common.web;

import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.supcan.common.Common;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.tiansu.eam.common.utils.ReflectUtils;
import com.tiansu.eam.modules.sys.model.fieldcontrol.PageFieldConfigModel;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 控制器支持类
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;
	
	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;
	
	/**
	 * 前端URL后缀
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}
	
	/**
	 * 添加Model消息
	 * @param messages
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	
	/**
	 * 添加Flash消息
	 * @param messages
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 客户端返回JSON字符串
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}
	
	/**
	 * 客户端返回字符串
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
	        response.setContentType(type);
	        response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 参数绑定异常
	 */
	@ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {  
        return "error/400";
    }
	
	/**
	 * 授权登录异常
	 */
	@ExceptionHandler({AuthenticationException.class})
    public String authenticationException() {  
        return "error/403";
    }
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
//			@Override
//			public String getAsText() {
//				Object value = getValue();
//				return value != null ? DateUtils.formatDateTime((Date)value) : "";
//			}
		});
	}

	/**
	 *
	 * @param key 页面参数
	 * 			获取页面传递的一个参数
	 * @return
	 */
	public String getPara(String key){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getParameter(key);
	}

	/**
	 * 将页面查询参数封装到Map（包含用于mybatis插件分页的参数）
	 * @return
	 */
	public Map getFormMap(){

		Map map = getParams();

		String rootPath = this.getClass().getResource("/").getPath();
		rootPath=rootPath+"/jeesite.properties";

		Properties prop = new Properties();
		String dbName=null;
		try {
			InputStream inStream = new BufferedInputStream(new FileInputStream(rootPath));
			prop.load(inStream);
			dbName = prop.getProperty("jdbc.type");
			inStream.close();
		}catch (Exception e){
			System.out.println("找不到jeesite.properties资源文件");
		}
		map.put("dbName",dbName);

		return map;
	}

	/**
	 * @creator wangr
	 * @createtime 2017/10/16 0016 下午 4:37
	 * @description: 用于app只获取数据库类型
	 * @return
	 */
	public Map getFormMapForApp(){

		Map map = new HashMap();

		String rootPath = this.getClass().getResource("/").getPath();
		rootPath=rootPath+"/jeesite.properties";

		Properties prop = new Properties();
		String dbName=null;
		try {
			InputStream inStream = new BufferedInputStream(new FileInputStream(rootPath));
			prop.load(inStream);
			dbName = prop.getProperty("jdbc.type");
			inStream.close();
		}catch (Exception e){
			System.out.println("找不到jeesite.properties资源文件");
		}
		map.put("dbName",dbName);

		return map;
	}

	/**
	 * 单表查询，将页面查询参数封装到实体对象
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T getFormEntity(Class<T> clazz){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

		Map<String, Object> map = getParams();

		T obj= ReflectUtils.getBean(map, clazz);
		return obj;

	}

	/**
	 * 获取页面传递来的查询参数存入Map返回
	 * @return
	 */
	public Map getParams(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Enumeration<String> en = request.getParameterNames();

		Map<String,String> map = new HashMap();
		while (en.hasMoreElements()) {
			String nms = en.nextElement().toString();
			if(nms.startsWith("search")){
				String as = request.getParameter(nms);
				if(!Common.isEmpty(as)){
					nms=nms.substring(nms.indexOf("[")+1,nms.indexOf("]"));
					map.put(nms, as);
				}
			}
		}

		return map;

	}

	/**
	 * 将数据实体转化为Map
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>(0);
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!"class".equals(name)) {
					params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	/**
	 *
	 * @param menuno 页面资源id
	 * @return 以json格式返回表单、表格、查询区域的字段
	 */

	public Map getFieldsByMenuno(String menuno){
		Map<String,Object> result = new HashMap();


		//以list形式返回字段数据
		List<PageFieldConfigModel> data = UserUtils.getMenuFieldConfigList(menuno);

		List<Map> gridfield = new ArrayList();//表格
		List<Map> formfield = new ArrayList();//表单
		List<Map> srchfield = new ArrayList();//查询

		for(PageFieldConfigModel field:data) {
			String displayName = field.getDisplayName();	//显示名称
			String fieldName = field.getFieldName();		//字段name
			String marginType = field.getMarginType();	//字段类型
			String editable = field.getEditable();

			//校验规则
			Map validate = new HashMap();
			if(field.getNullable().equals("false")){
				validate.put("required",true);//是否必填
			}

			String validateType= field.getValidateType();
			if(validateType!=null&&!validateType.equals("")){
				if(validateType.equals("vint")){
					validate.put("digits",true);//整数
				}
				if(validateType.equals("vfloat")){
					validate.put("number",true);//数字校验
				}
				if(validateType.equals("vmail")){
					validate.put("email",true);//邮箱校验
				}
			}


			/*//其他属性
			Map editors = new HashMap();
			if(field.getEditable().equals("false")){
				editors.put("type","string");
			}else{
				editors.put("type",marginType);
			}*/


			//表格字段赋值
			if (field.isShowInGrid()!=null && field.isShowInGrid().equals("true")) {
				Map gridmap = new HashMap();
				gridmap.put("data", fieldName);
				gridmap.put("title", displayName);

				gridfield.add(gridmap);
			}
			//表单字段赋值
			if (field.getShowInForm()!=null && field.getShowInForm().equals("true")) {
				Map formmap = new HashMap();

				formmap.put("display", displayName);
				formmap.put("name", fieldName);
				formmap.put("type", marginType);
				formmap.put("editable",editable);
				formmap.put("validate", validate);
				//formmap.put("editors",editors);

				if(marginType.equals("select")||marginType.equals("combobox")){
					formmap.put("comboboxName", fieldName+"Box");
				}
				if(editable.equals("false")){
					formmap.put("readonly",true);
				}

				formfield.add(formmap);
			}
			//搜索框字段赋值
			if(field.getShowInSearch()!=null && field.getShowInSearch().equals("true")){
				Map srchmap = new HashMap();
				srchmap.put("display", displayName);
				srchmap.put("name", fieldName);
				srchmap.put("type", marginType);
				if(marginType.equals("select")||marginType.equals("combobox")){
					srchmap.put("comboboxName", fieldName+"Box");
				}

				srchfield.add(srchmap);
			}

		}

		result.put("gridfield",gridfield);
		result.put("formfield",formfield);
		result.put("srchfield",srchfield);
		System.out.println(result);

		return result;
	}
}
