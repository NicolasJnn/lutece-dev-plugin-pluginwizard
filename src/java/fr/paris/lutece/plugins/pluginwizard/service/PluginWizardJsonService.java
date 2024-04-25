/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.plugins.pluginwizard.service;

import fr.paris.lutece.plugins.pluginwizard.business.model.PluginModel;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;

/**
 * The json service will create a model json file
 */
public class PluginWizardJsonService
{
    private static PluginWizardJsonService _singleton;
    private static final String PARAM_PLUGIN_ID = "plugin_id";
    private static Plugin _plugin = PluginService.getPlugin( "pluginwizard" );

    /**
     * Gets the unique instance of the PluginWizardJsonService
     * 
     * @return The unique instance of the PluginWizardJsonService
     */
    public static synchronized PluginWizardJsonService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = new PluginWizardJsonService( );
        }

        return _singleton;
    }

    /**
     * Exports the file in a byte array
     * 
     * @param request
     *            The Http request
     * @return An array of byte which is the content of the model.json
     */
    public byte [ ] exportJson( HttpServletRequest request )
    {
        String strPluginId = request.getParameter( PARAM_PLUGIN_ID );
        int nPluginId = Integer.parseInt( strPluginId );
        PluginModel pluginModel = ModelService.getPluginModel( nPluginId );

        return MapperService.getJson( pluginModel ).getBytes( StandardCharsets.UTF_8 );
    }
    
    public String getPluginName( HttpServletRequest request ) {
        String strPluginId = request.getParameter( PARAM_PLUGIN_ID );
        int nPluginId = Integer.parseInt( strPluginId );
    	PluginModel pluginModel = ModelService.getPluginModel( nPluginId );
    	return pluginModel.isModule() ? pluginModel.getModuleName() : pluginModel.getPluginName();
    }
}
