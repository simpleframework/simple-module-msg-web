<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.module.msg.web.component.mnotice.MNoticeUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	ComponentParameter nCP = MNoticeUtils.get(request, response);
%>
<div class="mnotice_sent">
  <div class="senttb clearfix">
    <%=MNoticeUtils.toSentbarHTML(nCP)%>
  </div>
  <%=MNoticeUtils.toSentTableRows(nCP)%>
  <div class="sm_atten">#(PrivateMessageSentPage.3)</div>
</div>