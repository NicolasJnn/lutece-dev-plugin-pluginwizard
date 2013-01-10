/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.pluginwizard.service.generator;

import fr.paris.lutece.plugins.pluginwizard.business.model.PluginModel;
import fr.paris.lutece.plugins.pluginwizard.business.model.PluginPortlet;
import fr.paris.lutece.plugins.pluginwizard.business.model.PluginPortletHome;
import fr.paris.lutece.plugins.pluginwizard.service.SourceCodeGenerator;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * Class generated the jsp files needed to manage portlets
 *
 */
public class PortletJspFilesGenerator implements Generator
{
    private static final String EXT_JSP = ".jsp";

    /**
     * Visits the path and verifies whether portlet jsp files are needed
     *
     * @param plugin The plugin
     * @param pluginModel the representation of the created plugin
     * @return The map with the name of the file and its corresponding content
     */
    @Override
    public Map generate( Plugin plugin, PluginModel pluginModel )
    {
        HashMap map = new HashMap(  );
        Collection<PluginPortlet> listPortlets = PluginPortletHome.findByPlugin( pluginModel.getIdPlugin(  ), plugin );

        String strBasePath = "plugin-{plugin_name}/webapp/jsp/admin/plugins/{plugin_name}/";
        strBasePath = strBasePath.replace( "{plugin_name}", pluginModel.getPluginName(  ) );

        for ( PluginPortlet portlet : listPortlets )
        {
            for ( int i = 1; i < 5; i++ )
            {
                String strPortlet = portlet.getPluginPortletTypeName(  );
                int nIndex = strPortlet.lastIndexOf( "_" );
                String strPortletFile = getPortletFileName( getFirstCaps( 
                            strPortlet.substring( 0, nIndex ).toLowerCase(  ) ), i );

                String strPath = strBasePath + strPortletFile;

                String strSourceCode = SourceCodeGenerator.getPortletJspFile( portlet, pluginModel.getPluginName(  ), i );
                strSourceCode = strSourceCode.replace( "&lt;", "<" );
                strSourceCode = strSourceCode.replace( "&gt;", ">" );
                map.put( strPath, strSourceCode );
            }
        }

        return map;
    }

    /**
     * Fetches the name of the portlet jsp
     *
     * @param strPortletName the name of the portlet
     * @param nPortletJspFileType The type of jsp
     * @return The name of the jsp file
     */
    private String getPortletFileName( String strPortletName, int nPortletJspFileType )
    {
        String strReturn;

        switch ( nPortletJspFileType )
        {
            case 1:
                strReturn = "ModifyPortlet" + strPortletName + EXT_JSP;

                break;

            case 2:
                strReturn = "DoModifyPortlet" + strPortletName + EXT_JSP;

                break;

            case 3:
                strReturn = "CreatePortlet" + strPortletName + EXT_JSP;

                break;

            case 4:
                strReturn = "DoCreatePortlet" + strPortletName + EXT_JSP;

                break;

            default:
                strReturn = "CreatePortlet" + strPortletName + EXT_JSP;

                break;
        }

        return strReturn;
    }

    /**
     * Returns the value of a string with first letter in caps
     *
     * @param strValue The value to be transformed
     * @return The first letter is in Capital
     */
    private String getFirstCaps( String strValue )
    {
        String strFirstLetter = strValue.substring( 0, 1 );
        String strLettersLeft = strValue.substring( 1 );
        String strValueCap = strFirstLetter.toUpperCase(  ) + strLettersLeft.toLowerCase(  );

        return strValueCap;
    }
}
