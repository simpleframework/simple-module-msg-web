package net.simpleframework.module.msg.web.component.mnotice;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MNoticeBean extends AbstractComponentBean {
	private static final long serialVersionUID = -646192111130851670L;

	/* 是否开启接收人可写 */
	private boolean receiverEnable = BeanDefaults.getBool(getClass(), "receiverEnable", true);
	/* 是否开启标题可写 */
	private boolean topicEnable = BeanDefaults.getBool(getClass(), "topicEnable", true);

	@Override
	public boolean isRunImmediately() {
		return false;
	}

	@Override
	public String getHandlerClass() {
		final String sClass = super.getHandlerClass();
		return StringUtils.hasText(sClass) ? sClass : DefaultMNoticeHandler.class.getName();
	}

	public boolean isReceiverEnable() {
		return receiverEnable;
	}

	public void setReceiverEnable(final boolean receiverEnable) {
		this.receiverEnable = receiverEnable;
	}

	public boolean isTopicEnable() {
		return topicEnable;
	}

	public void setTopicEnable(final boolean topicEnable) {
		this.topicEnable = topicEnable;
	}
}
