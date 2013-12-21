package net.simpleframework.module.msg.web.plugin;

import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IMessageUI {

	/**
	 * 获取我的消息页面
	 * 
	 * @return
	 */
	String getMyPageUrl(PageParameter pp);

	/**
	 * 获取管理消息页面
	 * 
	 * @return
	 */
	String getManagerPageUrl(PageParameter pp);

	/**
	 * 获取图标
	 * 
	 * @return
	 */
	String getIconClass();
}
