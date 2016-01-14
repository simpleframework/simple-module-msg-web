package net.simpleframework.module.msg.web.component.mnotice;

import java.util.Enumeration;

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

		pp.addComponentBean("MNoticeLoaded_autocomplete", AutocompleteBean.class)
				.setInputField("sm_receiver").setSepChar(";")
				.setHandlerClass(_AutocompleteHandler.class);
	}

	public static class _AutocompleteHandler extends AbstractAutocompleteHandler {

		@Override
		public Enumeration<AutocompleteData> getData(final ComponentParameter cp, final String val,
				final String val2) {
			return null;
		}
	}
}