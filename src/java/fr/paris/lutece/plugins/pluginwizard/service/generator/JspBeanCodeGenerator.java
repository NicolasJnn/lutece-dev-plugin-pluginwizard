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

import fr.paris.lutece.plugins.pluginwizard.business.model.BusinessClass;
import fr.paris.lutece.plugins.pluginwizard.business.model.BusinessClassHome;
import fr.paris.lutece.plugins.pluginwizard.business.model.PluginFeature;
import fr.paris.lutece.plugins.pluginwizard.business.model.PluginModel;
import static fr.paris.lutece.plugins.pluginwizard.service.generator.Markers.*;
import fr.paris.lutece.plugins.pluginwizard.web.Constants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * The JspBean needed to manage back office are generated by the class
 *
 */
public class JspBeanCodeGenerator implements Generator
{
    private static final String TEMPLATE_JSPBEAN_CODE_TEMPLATE = "/skin/plugins/pluginwizard/templates/pluginwizard_jspbean_template.html";

    /**
     * Visits the path and verifies if JspBean is relevant
     *
     * @param plugin The plugin
     * @param pluginModel the representation of the created plugin
     * @return The map with the name of the file and its corresponding content
     */
    @Override
    public Map generate( Plugin plugin, PluginModel pluginModel )
    {
        HashMap map = new HashMap(  );
        String strBasePath = "plugin-{plugin_name}/src/java/fr/paris/lutece/plugins/{plugin_name}/web/";
        strBasePath = strBasePath.replace( "{plugin_name}", pluginModel.getPluginName(  ) );

        for ( PluginFeature feature : pluginModel.getPluginFeatures(  ) )
        {
            Collection<BusinessClass> listBusinessClasses = BusinessClassHome.getBusinessClassesByFeature( feature.getIdPluginFeature(  ),
                    pluginModel.getIdPlugin(  ), plugin );
            String strPath = strBasePath + feature.getPluginFeatureName(  ) + Constants.PROPERTY_JSP_BEAN_SUFFIX +
                ".java";

            String strSourceCode = getJspBeanCode( pluginModel, feature.getPluginFeatureName(  ),
                    feature.getPluginFeatureRight(  ), listBusinessClasses );
            map.put( strPath, strSourceCode );
        }

        return map;
    }

    /**
     * Return JspBean code
     * @param pluginModel The plugin model
     * @param listBusinessClasses The list of business classes
     * @return the template The source code of the Jsp Bean
     */
    private String getJspBeanCode( PluginModel pluginModel, String strFeatureName, String strFeatureRight,
        Collection<BusinessClass> listBusinessClasses )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_LIST_BUSINESS_CLASS, listBusinessClasses );
        model.put( MARK_PLUGIN_MODEL, pluginModel );
        model.put( MARK_FEATURE_NAME, strFeatureName );
        model.put( MARK_FEATURE_RIGHT, strFeatureRight );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_JSPBEAN_CODE_TEMPLATE, Locale.getDefault(  ),
                model );

        return template.getHtml(  );
    }
}
