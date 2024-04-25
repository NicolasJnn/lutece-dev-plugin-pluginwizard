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
import fr.paris.lutece.plugins.pluginwizard.service.generator.GeneratorService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * The zip service will pack all the chosen files and create an archive
 */
public class PluginWizardZipService
{
    private static PluginWizardZipService _singleton;
    private static final String PARAM_PLUGIN_ID = "plugin_id";
    private static final String PARAM_SCHEME = "scheme";
    private static Plugin _plugin = PluginService.getPlugin( "pluginwizard" );

    /**
     * Gets the unique instance of the PluginWizardZipService
     * 
     * @return The unique instance of the PluginWizardZipService
     */
    public static synchronized PluginWizardZipService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = new PluginWizardZipService( );
        }

        return _singleton;
    }

    /**
     * Exports the files in a byte array
     * 
     * @param request
     *            The Http request
     * @return An array of byte which is the content of the archive
     */
    public byte [ ] exportZip( HttpServletRequest request )
    {
        ByteArrayOutputStream fos = new ByteArrayOutputStream( );

        byte [ ] buffer = new byte [ 1024];

        ZipOutputStream zipOutputStream = new ZipOutputStream( fos );
        zipOutputStream.setLevel( 9 );

        String strPluginId = request.getParameter( PARAM_PLUGIN_ID );
        int nPluginId = Integer.parseInt( strPluginId );
        String strScheme = request.getParameter( PARAM_SCHEME );
        int nScheme = Integer.parseInt( strScheme );
        PluginModel pluginModel = ModelService.getPluginModel( nPluginId );
        GeneratorService generator = new GeneratorService( );
        Map<String, String> mapSources = generator.getGeneratedSources( _plugin, pluginModel, nScheme );

        try
        {
            for ( Map.Entry<String, String> sourceFile : mapSources.entrySet( ) )
            {
                StringBuilder sb = new StringBuilder( sourceFile.getValue( ) );
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( sb.toString( ).getBytes( "UTF-8" ) );
                zipOutputStream.putNextEntry( new ZipEntry( sourceFile.getKey( ) ) );

                int nLength;

                while ( ( nLength = byteArrayInputStream.read( buffer ) ) > 0 )
                {
                    zipOutputStream.write( buffer, 0, nLength );
                }

                zipOutputStream.closeEntry( );
                byteArrayInputStream.close( );
            }

            zipOutputStream.finish( );
            zipOutputStream.close( );
        }
        catch( Exception e )
        {
            AppLogService.error( e );
        }

        return fos.toByteArray( );
    }
    
    public String getPluginName( HttpServletRequest request ) {
        String strPluginId = request.getParameter( PARAM_PLUGIN_ID );
        int nPluginId = Integer.parseInt( strPluginId );
    	PluginModel pluginModel = ModelService.getPluginModel( nPluginId );
    	return pluginModel.isModule() ? pluginModel.getModuleName() : pluginModel.getPluginName();
    }
}
