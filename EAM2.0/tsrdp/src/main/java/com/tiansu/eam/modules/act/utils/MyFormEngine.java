package com.tiansu.eam.modules.act.utils;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.FormEngine;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.scripting.ScriptingEngines;

import java.io.UnsupportedEncodingException;

/**
 * @author Douglas
 * @description
 * @create 2017-09-07 16:31
 **/
public class MyFormEngine implements FormEngine {

    private static final  String NOT_FOUND_FORM_CONTENT_FLAG = "____NOT_FOUND_FORM___";
    public MyFormEngine() {
        System.out.print("MyFormEngine init...............");
    }

    @Override
    public String getName() {
        return "myFormEngine";
    }

    @Override
    public Object renderStartForm(StartFormData startForm) {
        System.out.print("renderStartForm...............");
        if(startForm.getFormKey() == null) {
            return null;
        } else {
            String formTemplateString = this.getFormTemplateString(startForm, startForm.getFormKey());
            System.out.print("formTemplateString..............."+formTemplateString);
            if(NOT_FOUND_FORM_CONTENT_FLAG.equals(formTemplateString)){
                return startForm.getFormKey();
            }
            ScriptingEngines scriptingEngines = Context.getProcessEngineConfiguration().getScriptingEngines();
            return scriptingEngines.evaluate(formTemplateString, "juel", (VariableScope)null);
        }
    }

    @Override
    public Object renderTaskForm(TaskFormData taskForm) {
        if(taskForm.getFormKey() == null) {
            return null;
        } else {
            String formTemplateString = this.getFormTemplateString(taskForm, taskForm.getFormKey());
            if(NOT_FOUND_FORM_CONTENT_FLAG.equals(formTemplateString)){
                return taskForm.getFormKey();
            }
            ScriptingEngines scriptingEngines = Context.getProcessEngineConfiguration().getScriptingEngines();
            TaskEntity task = (TaskEntity)taskForm.getTask();
            return scriptingEngines.evaluate(formTemplateString, "juel", task.getExecution());
        }
    }

    protected String getFormTemplateString(FormData formInstance, String formKey) {
        String deploymentId = formInstance.getDeploymentId();
        ResourceEntity resourceStream = Context.getCommandContext().getResourceEntityManager().findResourceByDeploymentIdAndResourceName(deploymentId, formKey);
        if(resourceStream == null) {
// throw new ActivitiObjectNotFoundException("Form with formKey \'" + formKey + "\' does not exist", String.class);
            return NOT_FOUND_FORM_CONTENT_FLAG;
        } else {
            byte[] resourceBytes = resourceStream.getBytes();
            String encoding = "UTF-8";
            String formTemplateString = "";
            try {
                formTemplateString = new String(resourceBytes, encoding);
                return formTemplateString;
            } catch (UnsupportedEncodingException var9) {
                throw new ActivitiException("Unsupported encoding of :" + encoding, var9);
            }
        }
    }
}
