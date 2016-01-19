package net.simpleframework.module.msg.web.component.mnotice;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ado.query.IteratorDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.msg.IMessageContext;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.EWarnType;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ext.userselect.DefaultUserSelectHandler;
import net.simpleframework.mvc.component.ext.userselect.UserSelectBean;
import net.simpleframework.mvc.component.ui.autocomplete.AbstractAutocompleteHandler;
import net.simpleframework.mvc.component.ui.autocomplete.AutocompleteBean;
import net.simpleframework.mvc.component.ui.autocomplete.AutocompleteData;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MNoticeLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);

		final ComponentParameter nCP = MNoticeUtils.get(pp);
		// 自动完成组件
		pp.addComponentBean("MNoticeLoaded_autocomplete", AutocompleteBean.class)
				.setInputField("sm_receiver").setSepChar(";")
				.setParameters(MNoticeUtils.BEAN_ID + "=" + nCP.hashId())
				.setHandlerClass(_AutocompleteHandler.class);
		// 添加用户
		pp.addComponentBean("MNoticeLoaded_userSelect", UserSelectBean.class).setShowGroupOpt(false)
				.setMultiple(true).setJsSelectCallback("return MNoticeLoaded.userselect(selects);")
				.setDestroyOnClose(true).setHandlerClass(_UserSelectHandler.class)
				.setAttr("mnotice_component", nCP.componentBean);
		// 发送
		pp.addComponentBean("MNoticeLoaded_sent", AjaxRequestBean.class).setHandlerMethod("doSent")
				.setHandlerClass(UserSelectAction.class).setSelector(".mnotice_sent");
		// 验证
		pp.addComponentBean("MNoticeLoaded_validation", ValidationBean.class)
				.setTriggerSelector(".mnotice_sent input.button2")
				.setWarnType(EWarnType.insertLast)
				.addValidators(new Validator(EValidatorMethod.required, "#sm_receiver"),
						new Validator(EValidatorMethod.required, "#sm_topic"),
						new Validator(EValidatorMethod.required, "#sm_content"));
	}

	public static class _AutocompleteHandler extends AbstractAutocompleteHandler {
		@Override
		public Enumeration<AutocompleteData> getData(final ComponentParameter cp, final String val,
				final String val2) {
			final String sepChar = (String) cp.getBeanProperty("sepChar");
			final ComponentParameter nCP = MNoticeUtils.get(cp);
			final IMNoticeHandler nhdl = (IMNoticeHandler) nCP.getComponentHandler();
			final Iterator<PermissionUser> it = nhdl.allUsers(nCP);
			return new Enumeration<AutocompleteData>() {
				private PermissionUser user;

				@Override
				public boolean hasMoreElements() {
					while (it.hasNext()) {
						final PermissionUser user2 = it.next();
						if (user2.getName().contains(val2)) {
							user = user2;
							return true;
						}
					}
					return false;
				}

				@Override
				public AutocompleteData nextElement() {
					final AutocompleteData data = createAutocompleteData(user, sepChar);
					return data.setData(data.getTxt() + sepChar);
				}
			};
		}
	}

	public static class _UserSelectHandler extends DefaultUserSelectHandler {
		@Override
		public IDataQuery<PermissionUser> getUsers(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.get(cp,
					(AbstractComponentBean) cp.componentBean.getAttr("mnotice_component"));
			final IMNoticeHandler hdl = (IMNoticeHandler) nCP.getComponentHandler();
			return new IteratorDataQuery<PermissionUser>(hdl.allUsers(nCP));
		}

		@Override
		public Map<String, Object> getUserAttributes(final ComponentParameter cp,
				final PermissionUser user) {
			final KVMap kv = (KVMap) super.getUserAttributes(cp, user);
			kv.add("_user", user.getText() + "(" + user.getName() + ")");
			return kv;
		}
	}

	public static class UserSelectAction extends DefaultAjaxRequestHandler {

		@Transaction(context = IMessageContext.class)
		public IForward doSent(final ComponentParameter cp) throws Exception {
			final ComponentParameter nCP = MNoticeUtils.get(cp);
			final IMNoticeHandler nhdl = (IMNoticeHandler) nCP.getComponentHandler();
			final ArrayList<PermissionUser> users = new ArrayList<PermissionUser>();
			for (String rev : StringUtils.split(cp.getParameter("sm_receiver"), ";")) {
				final int ps = rev.indexOf("(");
				final int pe = rev.indexOf(")");
				if (ps > -1 && pe > ps) {
					rev = rev.substring(ps + 1, pe);
				}
				final PermissionUser user = cp.getUser(rev.trim());
				if (user.exists()) {
					users.add(user);
				}
			}
			return nhdl.onSent(nCP, users, cp.getParameter("sm_topic"), cp.getParameter("sm_content"));
		}
	}
}