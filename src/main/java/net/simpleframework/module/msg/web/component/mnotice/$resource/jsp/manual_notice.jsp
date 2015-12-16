<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.common.th.ThrowableUtils"%>
<%@ page import="net.simpleframework.module.msg.web.component.mnotice.MNoticeUtils"%>
<%
	try {
		MNoticeUtils.doForword(MNoticeUtils.get(request, response));
	} catch (Throwable th) {
		out.write("alert(\""
				+ ThrowableUtils.getThrowableMessage(th, null, true)
				+ "\");");
	}
%>