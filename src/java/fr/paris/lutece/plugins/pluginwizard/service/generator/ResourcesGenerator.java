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
package fr.paris.lutece.plugins.pluginwizard.service.generator;

import fr.paris.lutece.plugins.pluginwizard.business.model.Application;
import fr.paris.lutece.plugins.pluginwizard.business.model.Attribute;
import fr.paris.lutece.plugins.pluginwizard.business.model.BusinessClass;
import fr.paris.lutece.plugins.pluginwizard.business.model.Feature;
import fr.paris.lutece.plugins.pluginwizard.business.model.PluginModel;
import fr.paris.lutece.plugins.pluginwizard.business.model.Portlet;
import fr.paris.lutece.plugins.pluginwizard.util.Utils;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.text.MessageFormat;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Class generated needed resource files for i18n implementation
 *
 */
public class ResourcesGenerator extends AbstractGenerator
{
    private static final String PATH_JAVA = "src/java/fr/paris/lutece/plugins/{plugin_name}/resources/";
    private static final String PATH_KOTLIN = "src/kotlin/fr/paris/lutece/plugins/{plugin_name}/resources/";
    private static String [ ] _languages = {
            "", "fr"
    };
    private static String [ ] _prefix = {
            ""
    };
    private static String [ ] _suffix = {
            "created", "updated", "removed"
    };
    private static String [ ] _titlePrefix = {
    		"create", "modify"
    };

    /**
     * {@inheritDoc }
     */
    @Override
    public Map generate( PluginModel pm, String generationSchemeName )
    {
        HashMap map = new HashMap( );

        String prefixFileName = pm.getPluginNameForRessource( );

        for ( String strLanguage : _languages )
        {
            String strFilesPath = ( isKotlin( ) ) ? PATH_KOTLIN : PATH_JAVA;
            String strPath = getFilePath( pm, strFilesPath,
                    prefixFileName + "_messages" + ( strLanguage.length( ) > 0 ? "_" : "" ) + strLanguage + ".properties" );

            String strSourceCode = getCode( pm, strLanguage );
            map.put( strPath, strSourceCode );
        }

        return map;
    }

    /**
     * Build the code
     *
     * @param pm
     *            The Plugin Model
     * @param strLanguage
     *            The language
     * @return The code
     */
    private String getCode( PluginModel pm, String strLanguage )
    {
        StringBuilder sb = new StringBuilder( );
        generatePluginKeys( sb, pm );
        generateFeaturesKeys( sb, pm );
        generateXPagesKeys( sb, pm );
        generateBusinessClassKeys( sb, pm, strLanguage );
        generatePortletsKeys( sb, pm );
        generateInfosKeys( sb, pm, strLanguage );

        return sb.toString( );
    }

    /**
     * Writes in the buffer resources keys for the plugin
     *
     * @param sb
     *            The buffer
     * @param pm
     *            The plugin model
     */
    private void generatePluginKeys( StringBuilder sb, PluginModel pm )
    {
        sb.append( "# Plugin's keys\n" );
        if ( pm.isModule( ) )
        {
            sb.append( "module.provider=" ).append( pm.getPluginProvider( ) ).append( "\n" );
            sb.append( "module.description=" ).append( pm.getPluginDescription( ) ).append( "\n" );
        }
        else
        {
            sb.append( "plugin.provider=" ).append( pm.getPluginProvider( ) ).append( "\n" );
            sb.append( "plugin.description=" ).append( pm.getPluginDescription( ) ).append( "\n" );
        }

        sb.append( "\n" );
    }

    /**
     * Writes in the buffer resources keys for features
     *
     * @param sb
     *            The buffer
     * @param pm
     *            The plugin model
     */
    private void generateFeaturesKeys( StringBuilder sb, PluginModel pm )
    {
        if ( !pm.getFeatures( ).isEmpty( ) )
        {
            sb.append( "\n# Admin features keys\n\n" );

            for ( Feature feature : pm.getFeatures( ) )
            {
                sb.append( "adminFeature." ).append( feature.getFeatureName( ) ).append( ".name=" ).append( feature.getFeatureName( ) ).append( "\n" );
                sb.append( "adminFeature." ).append( feature.getFeatureName( ) ).append( ".description=" ).append( feature.getFeatureDescription( ) )
                        .append( "\n" );
            }

            sb.append( "\n" );
        }
    }

    private void generateXPagesKeys( StringBuilder sb, PluginModel pm )
    {
        if ( !pm.getApplications( ).isEmpty( ) )
        {
            sb.append( "\n# XPages keys\n\n" );
        }

        for ( Application application : pm.getApplications( ) )
        {
            if ( application.getIdBusinessClasses( ).isEmpty( ) )
            {
                sb.append( "xpage." ).append( application.getApplicationName( ) ).append( ".pageTitle=" ).append( application.getApplicationName( ) )
                        .append( "\n" );
                sb.append( "xpage." ).append( application.getApplicationName( ) ).append( ".pagePathLabel=" ).append( application.getApplicationName( ) )
                        .append( "\n" );
            }
            else
            {
                for ( int id : application.getIdBusinessClasses( ) )
                {
                    for ( BusinessClass bc : pm.getBusinessClasses( ) )
                    {
                        if ( bc.getId( ) == id )
                        {
                            sb.append( "xpage." ).append( bc.getBusinessClass( ).toLowerCase( ) ).append( ".pageTitle=" )
                                    .append( bc.getBusinessClass( ).toLowerCase( ) ).append( "\n" );
                            sb.append( "xpage." ).append( bc.getBusinessClass( ).toLowerCase( ) ).append( ".pagePathLabel=" )
                                    .append( bc.getBusinessClass( ).toLowerCase( ) ).append( "\n" );
                        }
                    }
                }
            }
        }
    }

    /**
     * Writes in the buffer resources keys for business classes
     *
     * @param sb
     *            The buffer
     * @param pm
     *            The plugin model
     * @param strLanguage
     *            The language
     */
    private void generateBusinessClassKeys( StringBuilder sb, PluginModel pm, String strLanguage )
    {
        if ( !pm.getBusinessClasses( ).isEmpty( ) )
        {
            sb.append( "\n# Business classes keys\n\n" );
        }

        for ( BusinessClass bc : pm.getBusinessClasses( ) )
        {
            sb.append( "\n# keys for business classes keys : " ).append( bc.getBusinessClass( ) ).append( "\n" );

            String strPrefix = "manage_" + bc.getBusinessClass( ).toLowerCase( ) + "s.";
            sb.append( strPrefix ).append( "pageTitle=" ).append( bc.getBusinessClass( ) ).append( "\n" );
            sb.append( strPrefix ).append( "title=" ).append( getLabel( "title.manage", strLanguage, bc.getBusinessClass( ) ) ).append( "\n" );
            sb.append( strPrefix ).append( "buttonAdd=" ).append( getLabel( "buttonAdd", strLanguage, bc.getBusinessClass( ) ) ).append( "\n" );

            for ( Attribute attribute : bc.getAttributes( ) )
            {
                sb.append( strPrefix ).append( "column" ).append( attribute.getName( ) ).append( "=" ).append( attribute.getLabelName( ) ).append( "\n" );
            }
            
            for ( String strTitlePrefix : _titlePrefix ) {
            	strPrefix = strTitlePrefix + "_" + bc.getBusinessClass( ).toLowerCase( ) + ".";
                sb.append( strPrefix ).append("title=").append( getLabel("title." + strTitlePrefix, strLanguage, bc.getBusinessClass( ) ) ).append("\n");
            }            

            for ( String strBasePrefix : _prefix )
            {
                strPrefix = strBasePrefix + (strBasePrefix.isEmpty() ? "" : "_") + bc.getBusinessClass( ).toLowerCase( ) + ".";
                sb.append( strPrefix ).append( "pageTitle=" ).append( bc.getBusinessClass( ) ).append( "\n" );

                for ( Attribute attribute : bc.getAttributes( ) )
                {
                    sb.append( strPrefix ).append( "label" ).append( attribute.getName( ) ).append( "=" ).append( attribute.getLabelName( ) ).append( "\n" );
                    sb.append( strPrefix ).append( "label" ).append( attribute.getName( ) ).append( ".help=" ).append( attribute.getLabelName( ) )
                            .append( " (" ).append( getLabel( "helpText", strLanguage ) ).append( ")\n" );
                }
            }


            sb.append( "\nmessage.confirmRemove" ).append( bc.getBusinessClass( ) ).append( "=" )
                    .append( getLabel( "confirmRemove", strLanguage, bc.getBusinessClass( ) ) ).append( "\n" );

            // Constraints messages
            sb.append( "\n# JSR 303 constraint validator messages\n" );

            strPrefix = "validation." + bc.getBusinessClass( ).toLowerCase( ) + ".";

            for ( Attribute attribute : bc.getAttributes( ) )
            {
                if ( !attribute.getType( ).equals( "int" ) )
                {
                    if ( attribute.getNotNull( ) )
                    {
                        sb.append( strPrefix ).append( attribute.getName( ) ).append( ".notEmpty=" )
                                .append( getLabel( "validation.notEmpty", strLanguage, attribute.getLabelName( ) ) ).append( "\n" );
                    }

                    if ( attribute.getMaxLength( ) > 0 )
                    {
                        sb.append( strPrefix ).append( attribute.getName( ) ).append( ".size=" )
                                .append( getLabel( "validation.size", strLanguage, attribute.getLabelName( ), "" + attribute.getMaxLength( ) ) ).append( "\n" );
                    }
                }
            }

            sb.append( "\n# model attributes for validation messages\n" );
            strPrefix = "model.entity." + bc.getBusinessClass( ).toLowerCase( ) + ".attribute.";

            for ( Attribute attribute : bc.getAttributes( ) )
            {
                sb.append( strPrefix ).append( Utils.firstLowerCase( attribute.getName( ) ) ).append( "=" ).append( attribute.getLabelName( ) ).append( "\n" );
            }
        }
    }

    /**
     * Writes in the buffer resources keys for portlets
     *
     * @param sb
     *            The buffer
     * @param pm
     *            The plugin model
     */
    private void generatePortletsKeys( StringBuilder sb, PluginModel pm )
    {
        if ( !pm.getPortlets( ).isEmpty( ) )
        {
            sb.append( "\n# Portlets keys\n\n" );

            for ( Portlet portlet : pm.getPortlets( ) )
            {
                sb.append( "portlet." ).append( pm.getPluginName( ).toLowerCase( ) ).append( portlet.getPortletClass( ) ).append( ".name=" )
                        .append( portlet.getJspBaseName( ) ).append( "\n" );
            }

            sb.append( "\n" );
        }
    }

    /**
     * Writes in the buffer resources keys for info notifications
     *
     * @param sb
     *            The buffer
     * @param pm
     *            The plugin model
     */
    private void generateInfosKeys( StringBuilder sb, PluginModel pm, String strLanguage )
    {
        if ( !pm.getBusinessClasses( ).isEmpty( ) )
        {
            sb.append( "\n# Infos keys\n\n" );

            for ( BusinessClass bc : pm.getBusinessClasses( ) )
            {
                for ( String strSuffix : _suffix )
                {
                    sb.append( "info." ).append( bc.getBusinessClass( ).toLowerCase( ) ).append( "." ).append( strSuffix ).append( "=" )
                            .append( bc.getBusinessClass( ) ).append( " " ).append( getLabel( "infoKey." + strSuffix, strLanguage ) ).append( "\n" );
                }
            }

            sb.append( "\n" );
        }
    }

    /**
     * Gets a label for a given language from the pluginwizard.properties file
     *
     * @param strKey
     *            The key of the label
     * @param strLanguage
     *            The language
     * @param arguments
     *            arguments of the label
     * @return The value of the label
     */
    private String getLabel( String strKey, String strLanguage, String... arguments )
    {
        String strFullKey = "pluginwizard.label." + strKey + "." + strLanguage;
        String strLabel = AppPropertiesService.getProperty( strFullKey, "Label not found for key " + strFullKey );

        return MessageFormat.format( strLabel, (Object [ ]) arguments );
    }
}
