package net.simpleframework.module.msg.web.component.mnotice;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(MNoticeRegistry.MNOTICE)
@ComponentBean(MNoticeBean.class)
@ComponentRender(MNoticeRender.class)
@ComponentResourceProvider(MNoticeResourceProvider.class)
public class MNoticeRegistry extends AbstractComponentRegistry {

	public static final String MNOTICE = "msg_manual_notice";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pp, final Object attriData) {
		final MNoticeBean mnotice = (MNoticeBean) super.createComponentBean(pp, attriData);

		final ComponentParameter nCP = ComponentParameter.get(pp, mnotice);
		final String componentName = nCP.getComponentName();

		final AjaxRequestBean ajaxRequest = pp.addComponentBean(componentName + "_win_page",
				AjaxRequestBean.class).setUrlForward(
				getComponentResourceProvider().getResourceHomePath() + "/jsp/mnotice_sent.jsp");
		pp.addComponentBean(componentName + "_win", WindowBean.class)
				.setContentRef(ajaxRequest.getName()).setWidth(500).setHeight(540)
				.setTitle($m("MNoticeRegistry.0"));
		return mnotice;
	}
}
