/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.act.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tiansu.eam.modules.act.entity.ActModel;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程模型Service
 * @author marsart
 * @version 2017-08-01
 */
@Service
@Transactional(readOnly = true)
public class EamActModelService extends EamActBaseService{

	@Autowired
	private RepositoryService repositoryService;

	//	@Autowired
//	private ObjectMapper objectMapper;
	protected ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 流程模型列表
	 *
	 */
	public Map<String,Object> modelList() {
		//获取页面请求
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();

		//repositoryService.createProcessDefinitionQuery().latestVersion()
/*		if (StringUtils.isNotEmpty(category)){
			modelQuery.modelCategory(category);
		}*/

		//把activiti内部model转换一下
		List<ActModel> list = new ArrayList<>();
		List<Model> modelList = dataTablePage(modelQuery,request);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for(Object o : modelList) {
			ActModel actModel = new ActModel();
			if (null != o) {
				Model model = (Model) o;
				actModel.setModelId(model.getId());
				actModel.setModelKey(model.getKey());
				if (null != model.getVersion()) {
					actModel.setVersion((model.getVersion().toString()));
				}
				actModel.setCreateTime(sdf.format(model.getCreateTime()));
				actModel.setModifyTime(sdf.format(model.getLastUpdateTime()));
				actModel.setModelName(model.getName());
				list.add(actModel);
			}
		}

		Map<String, Object> map= new HashMap();
		map.put("recordsFiltered",modelQuery.count());
		map.put("recordsTotal",modelQuery.count());
		map.put("data",list);
		int draw = Integer.parseInt(request.getParameter("draw")==null?"1":request.getParameter("draw"));
		map.put("draw",draw);
		return map;
	}

	/**
	 * 创建模型
	 * @throws UnsupportedEncodingException
	 */
	@Transactional(readOnly = false)
	public Model create(String name, String key, String description, String category) throws UnsupportedEncodingException {

		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode properties = objectMapper.createObjectNode();
		properties.put("process_author", "tiansu");
		editorNode.put("properties", properties);
		ObjectNode stencilset = objectMapper.createObjectNode();
		stencilset.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilset);

		Model modelData = repositoryService.newModel();
		description = StringUtils.defaultString(description);
		modelData.setKey(StringUtils.defaultString(key));
		modelData.setName(name);
		modelData.setCategory(category);
		modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count()+1)));

		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
		modelData.setMetaInfo(modelObjectNode.toString());

		repositoryService.saveModel(modelData);
		repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

		return modelData;
	}

	/**
	 * 根据Model部署流程
	 */
	@Transactional(readOnly = false)
	public boolean deploy(String id) {
		//String message = "";
		boolean ret = false;
		try {
			Model modelData = repositoryService.getModel(id);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			String processName = modelData.getName();
			if (!StringUtils.endsWith(processName, ".bpmn20.xml")){
				processName += ".bpmn20.xml";
			}
//			SystemInfo.out.println("========="+processName+"============"+modelData.getName());
			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
					.addInputStream(processName, in).deploy();
//					.addString(processName, new String(bpmnBytes)).deploy();

			// 设置流程分类
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
			for (ProcessDefinition processDefinition : list) {
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), modelData.getCategory());
				//message = "部署成功，流程ID=" + processDefinition.getId();
				ret = true;
			}
			if (list.size() == 0){
				//message = "部署失败，没有流程。";
			}
		} catch (Exception e) {
			throw new ActivitiException("设计模型图不正确，检查模型正确性，模型ID="+id, e);
		}
		return ret;
	}


	/**
	 * @creator Douglas
	 * @createtime 2017-9-1 17:01
	 * @description: 根据上传文件(xml、zip)部署
	 * @param file
	 * @return
	 */
	public Boolean deploy(MultipartFile file) {

		// 获取上传的文件名
		String fileName = file.getOriginalFilename();

		try {
			// 得到输入流（字节流）对象
			InputStream fileInputStream = file.getInputStream();

			// 文件的扩展名
			String extension = FilenameUtils.getExtension(fileName);

			// zip或者bar类型的文件用ZipInputStream方式部署
			DeploymentBuilder deployment = repositoryService.createDeployment();
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment.addZipInputStream(zip);
			} else {
				// 其他类型的文件直接部署
				deployment.addInputStream(fileName, fileInputStream);
			}
			deployment.deploy();
		} catch (Exception e) {
			throw new ActivitiException("部署失败，请检查部署资源正确性。", e);
		}

		return true;
	}


	/**
	 * 导出model的xml文件
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @modifyer wangjl
	 * @modifytime 2017/11/23 14:44
	 * @description: 调整顺序，在流拷贝前指定浏览器下载文件而不是打开文件。解决模型导出时有时打开文件而不导出的问题
	 */
	public void export(String id, HttpServletResponse response) {
		try {
			Model modelData = repositoryService.getModel(id);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			response.setHeader("Content-Type", "application/octet-stream");
			String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
			//通知浏览器是下载文件而不是打开文件
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			IOUtils.copy(in, response.getOutputStream());

//			String filePath = "E:/mysvn/EAM2.0/trunk/code/tsrdp/src/main/webapp/WEB-INF/views/modules/faultOrder";
//			saveTmpFile(bpmnBytes,filePath,filename);

			response.flushBuffer();
		} catch (Exception e) {
			throw new ActivitiException("导出model的xml文件失败，模型ID="+id, e);
		}
	}

	public void saveTmpFile(byte[] bfile, String filePath,String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath+"\\"+fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 更新Model分类
	 */
	@Transactional(readOnly = false)
	public void updateCategory(String id, String category) {
		Model modelData = repositoryService.getModel(id);
		modelData.setCategory(category);
		repositoryService.saveModel(modelData);
	}

	/**
	 * 删除模型
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(String id) {
		repositoryService.deleteModel(id);
	}
}
