<%@ page errorPage="../../ErrorPagePortal.jsp"%>

<%@ page import="fr.paris.lutece.portal.service.util.AppLogService"%>
<%@ page import="fr.paris.lutece.plugins.pluginwizard.service.PluginWizardZipService"%>

<%
	try
	{
		byte[] fileContent = PluginWizardZipService.getInstance().exportZip(request);
		if (fileContent != null)
		{
			String strFileName = "plugin.zip";
			//the header and also the names set by which user will be prompted to save
			response.setHeader("Content-Disposition", "attachment;filename=\"" + strFileName + "\"");
			response.setContentType(application.getMimeType(strFileName));
			response.setHeader("Cache-Control", "must-revalidate");
			response.getOutputStream().write(fileContent);
		}
	} catch (Exception e)
	{
		AppLogService.error(e.getMessage(), e);
	}
	out.clear();
	out = pageContext.pushBody();
%>
