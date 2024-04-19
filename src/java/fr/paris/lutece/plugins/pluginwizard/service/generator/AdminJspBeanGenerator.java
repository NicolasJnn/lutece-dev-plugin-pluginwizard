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

import fr.paris.lutece.plugins.pluginwizard.business.model.BusinessClass;
import fr.paris.lutece.plugins.pluginwizard.business.model.Feature;
import fr.paris.lutece.plugins.pluginwizard.business.model.PluginModel;
import fr.paris.lutece.plugins.pluginwizard.service.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin Jsp Bean Generator
 */
public class AdminJspBeanGenerator extends AbstractGenerator
{
    private static final String PATH_JAVA = "java/fr/paris/lutece/plugins/{plugin_name}/web/";
    private static final String PATH_KOTLIN = "kotlin/fr/paris/lutece/plugins/{plugin_name}/web/";
    private static final String PREFIX_JSPBEAN = "AbstractPaginator";
    private static final String PREFIX_JSPBEAN_PATH = "src/";

    private static final String SUFFIX_JAVA_EXTENSION = ".java";
    private static final String SUFFIX_KOTLIN_EXTENSION = ".kt";

    private static final String SUFFIX_JSPBEAN_CLASS = "JspBean";

    private String _strAbstractParentBeanTemplate = "/generators/default/jspbean/gt_jspbean_abstract.html";
    private List<AdminJspBeanFileConfig> _listFiles;

    public void setFiles( List<AdminJspBeanFileConfig> listFiles )
    {
        if ( listFiles != null )
        {
            _listFiles = (List<AdminJspBeanFileConfig>) ( ( (ArrayList<AdminJspBeanFileConfig>) listFiles ).clone( ) );
        }
        else
        {
            _listFiles = null;
        }
    }

    /**
     * Set the parent bean template
     * 
     * @param strParent
     *            The parent bean template
     */
    public void setAbstractParentBeanTemplate( String strParent )
    {
        _strAbstractParentBeanTemplate = strParent;
    }

    /**
     * {@inheritDoc }
     */
    public Map<String, String> generate( PluginModel pm, String generationSchemeName )
    {
        HashMap<String, String> map = new HashMap<>( );      
        String strFilesPath = ( isKotlin( ) ) ? PATH_KOTLIN : PATH_JAVA;
        String strSuffix = SUFFIX_JSPBEAN_CLASS + ( ( isKotlin( ) ) ? SUFFIX_KOTLIN_EXTENSION : SUFFIX_JAVA_EXTENSION );
        
        for ( Feature feature : pm.getFeatures( ) )
        {
            Collection<BusinessClass> listBusinessClasses = ModelService.getBusinessClassesByFeature( pm, feature.getId( ) );

            if ( listBusinessClasses.isEmpty( ) )
            {
                for ( AdminJspBeanFileConfig file : _listFiles )
                {
                    String strSuffixConfig = SUFFIX_JSPBEAN_CLASS + file.getSuffix( ) + ( ( isKotlin( ) ) ? SUFFIX_KOTLIN_EXTENSION : SUFFIX_JAVA_EXTENSION );
                    String strFilename = feature.getFeatureName( ) + strSuffixConfig;
                    String strPath = getFilePath( pm, file.getSourcePath( ) + ( isKotlin( ) ? PATH_KOTLIN : PATH_JAVA ), strFilename );
                    String strSourceCode = getJspBeanCode( pm, feature.getFeatureName( ), feature.getFeatureRight( ), file.getTemplate( ),
                            pm.getPluginNameAsRadicalPackage( ), pm.getPluginName( ) );
                    map.put( strPath, strSourceCode );
                }
            }
            else
            {
                for ( BusinessClass business : listBusinessClasses )
                {
                    for ( AdminJspBeanFileConfig file : _listFiles )
                    {
                        String strSuffixConfig = SUFFIX_JSPBEAN_CLASS + file.getSuffix( )
                                + ( ( isKotlin( ) ) ? SUFFIX_KOTLIN_EXTENSION : SUFFIX_JAVA_EXTENSION );
                        String strFilename = business.getBusinessClassCapsFirst( ) + strSuffixConfig;
                        String strPath = getFilePath( pm, file.getSourcePath( ) + ( isKotlin( ) ? PATH_KOTLIN : PATH_JAVA ), strFilename );
                        String strSourceCode = getJspBeanCode( pm, feature.getFeatureName( ), feature.getFeatureRight( ), business, file.getTemplate( ),
                                pm.getPluginNameAsRadicalPackage( ), pm.getPluginName( ) );
                        map.put( strPath, strSourceCode );
                    }
                }
            }
        }
        String strPath = getFilePath( pm, PREFIX_JSPBEAN_PATH + strFilesPath, PREFIX_JSPBEAN + strSuffix );
        String strSourceCode = getAbstractJspBeanCode( pm, pm.getPluginNameAsRadicalPackage( ),
                pm.getPluginName( ) );
        map.put( strPath, strSourceCode );

        return map;
    }

    /**
     * Return JspBean code
     *
     * @param pm
     *            The plugin model
     * @param strFeatureName
     *            The feature name
     * @param strFeatureRight
     *            The feature right
     * @param business
     *            The business classes
     * @return the template The source code of the Jsp Bean
     */
    private String getJspBeanCode( PluginModel pm, String strFeatureName, String strFeatureRight, BusinessClass business, String strTemplate,
            String strRadicalPackage, String strBeanName )
    {
        Map<String, Object> model = getModel( pm );

        model.put( Markers.MARK_BUSINESS_CLASS, business );
        model.put( Markers.MARK_FEATURE_NAME, strFeatureName );
        model.put( Markers.MARK_FEATURE_RIGHT, strFeatureRight );
        
        model.put( Markers.MARK_RADICAL_PACKAGE, strRadicalPackage );
        model.put( Markers.MARK_BEAN_NAME, strBeanName );

        return build( strTemplate, model );
    }

    /**
     * Return JspBean code
     *
     * @param pm
     *            The plugin model
     * @param strFeatureName
     *            The feature name
     * @param strFeatureRight
     *            The feature right
     * @param business
     *            The business classes
     * @return the template The source code of the Jsp Bean
     */
    private String getJspBeanCode( PluginModel pm, String strFeatureName, String strFeatureRight, String strTemplate, String strRadicalPackage,
            String strBeanName )
    {
        Map<String, Object> model = getModel( pm );

        model.put( Markers.MARK_FEATURE_NAME, strFeatureName );
        model.put( Markers.MARK_FEATURE_RIGHT, strFeatureRight );
        
        model.put( Markers.MARK_RADICAL_PACKAGE, strRadicalPackage );
        model.put( Markers.MARK_BEAN_NAME, strBeanName );

        return build( strTemplate, model );
    }

    /**
     * Return JspBean code
     *
     * @param pm
     *            The plugin model
     * @param strFeatureName
     *            The feature name
     * @param strFeatureRight
     *            The feature right
     * @return the template The source code of the Jsp Bean
     */
    private String getAbstractJspBeanCode( PluginModel pm, String strRadicalPackage, String strBeanName )
    {
        Map<String, Object> model = getModel( pm );

        model.put( Markers.MARK_RADICAL_PACKAGE, strRadicalPackage );
        model.put( Markers.MARK_BEAN_NAME, strBeanName );

        return build( _strAbstractParentBeanTemplate, model );
    }
}
