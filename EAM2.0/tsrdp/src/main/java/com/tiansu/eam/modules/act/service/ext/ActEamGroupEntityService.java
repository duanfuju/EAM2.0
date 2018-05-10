/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.act.service.ext;

import com.tiansu.eam.modules.act.utils.ActivitiUtil;
import com.tiansu.eam.modules.sys.entity.Role;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Activiti Group Entity Service
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
public class ActEamGroupEntityService extends GroupEntityManager {

	/*@Autowired
	private EamRoleService eamRoleService;*/


	@Override
	public List<Group> findGroupsByUser(final String userCode) {
		if (userCode == null){
			return null;
		}

		//根据用户id查询所属角色
		List<Role> roles = UserUtils.get(userCode).getRoleList();
		//Dept dept = UserUtils.get(userCode).getOffice();

		List<Group> gs = null;
		gs = ActivitiUtil.toActivitiGroups(roles);
		return gs;

	}
	
	/*public Group createNewGroup(String groupId) {
		return new GroupEntity(groupId);
	}

	public void insertGroup(Group group) {
//		getDbSqlSession().insert((PersistentObject) group);
		throw new RuntimeException("not implement method.");
	}*/

	/*public void updateGroup(GroupEntity updatedGroup) {
//		CommandContext commandContext = Context.getCommandContext();
//		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
//		dbSqlSession.update(updatedGroup);
		throw new RuntimeException("not implement method.");
	}

	public void deleteGroup(String groupId) {
//		GroupEntity group = getDbSqlSession().selectById(GroupEntity.class, groupId);
//		getDbSqlSession().delete("deleteMembershipsByGroupId", groupId);
//		getDbSqlSession().delete(group);
		throw new RuntimeException("not implement method.");
	}*/


	/*@Override
	public GroupEntity findGroupById(final String groupCode) {
		if (groupCode == null)
			return null;

		try {
			com.sanshen.js.entity.account.Group bGroup = groupManager.getGroupByGroupId(groupCode);

			GroupEntity e = new GroupEntity();
			e.setRevision(1);

			// activiti有3种预定义的组类型：security-role、assignment、user
			// 如果使用Activiti
			// Explorer，需要security-role才能看到manage页签，需要assignment才能claim任务
			e.setType("assignment");

			e.setId(bGroup.getGroupId());
			e.setName(bGroup.getName());
			return e;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}*/


	public GroupQuery createNewGroupQuery() {
//		return new GroupQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
		throw new RuntimeException("not implement method.");
	}

//	@SuppressWarnings("unchecked")
	public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
		//return getDbSqlSession().selectList("selectGroupByQueryCriteria", query, page);
		throw new RuntimeException("not implement method.");
	}

	public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
//		return (Long) getDbSqlSession().selectOne("selectGroupCountByQueryCriteria", query);
		throw new RuntimeException("not implement method.");
	}


	@Override
	public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
//		return getDbSqlSession().selectListWithRawParameter("selectGroupByNativeQuery", parameterMap, firstResult, maxResults);
		throw new RuntimeException("not implement method.");
	}

	public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
//		return (Long) getDbSqlSession().selectOne("selectGroupCountByNativeQuery", parameterMap);
		throw new RuntimeException("not implement method.");
	}

}