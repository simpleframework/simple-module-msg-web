package net.simpleframework.module.msg.web.component.mnotice;

import java.util.Enumeration;
import java.util.Iterator;

import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
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
		pp.addComponentBean("MNoticeLoaded_autocomplete", AutocompleteBean.class)
				.setInputField("sm_receiver").setSepChar(";")
				.setParameters(MNoticeUtils.BEAN_ID + "=" + nCP.hashId())
				.setHandlerClass(_AutocompleteHandler.class);
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
					return createAutocompleteData(user, sepChar);
				}
			};
		}
	}
}