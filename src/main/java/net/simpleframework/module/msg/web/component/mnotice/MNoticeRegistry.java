package net.simpleframework.module.msg.web.component.mnotice;

import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

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
}
