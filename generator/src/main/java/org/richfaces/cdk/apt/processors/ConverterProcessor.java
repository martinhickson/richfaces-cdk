/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.cdk.apt.processors;

import java.lang.annotation.Annotation;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import jakarta.faces.convert.FacesConverter;

import org.richfaces.cdk.CdkProcessingException;
import org.richfaces.cdk.annotations.JsfConverter;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ConverterModel;
import org.richfaces.cdk.model.FacesId;

/**
 * @author akolonitsky
 * @since Jan 4, 2010
 */
@SupportedAnnotationTypes({ "jakarta.faces.component.FacesComponent", "jakarta.faces.convert.FacesConverter",
        JsfConverter.NAME })
public class ConverterProcessor extends ProcessorBase implements CdkAnnotationProcessor {
    @Override
    public void process(Element element, ComponentLibrary library) throws CdkProcessingException {
        SourceUtils sourceUtils = getSourceUtils();
        AnnotationMirror converter = sourceUtils.getAnnotationMirror(element, JsfConverter.class);
        if (null != converter) {
            processJsfConverter(element, library, converter);
            return;
        }

        AnnotationMirror facesConverter = sourceUtils.getAnnotationMirror(element, FacesConverter.class);
        if (null != facesConverter) {
            processFacesConverter(element, library, facesConverter);
        }
    }

    private void processJsfConverter(Element element, ComponentLibrary library, AnnotationMirror converter)
            throws CdkProcessingException {
        SourceUtils sourceUtils = getSourceUtils();
        ConverterModel converterModel = new ConverterModel();
        sourceUtils.setModelProperty(converterModel, converter, "id");
        sourceUtils.setModelProperty(converterModel, converter, "converterForClass", "forClass");

        setDescription(converterModel, converter, getDocComment(element));
        processAttributes(element, converterModel, converter);
        setTagInfo(converter, converterModel);
        library.getConverters().add(converterModel);
    }

    private void processFacesConverter(Element element, ComponentLibrary library, AnnotationMirror facesConverter) {
        SourceUtils sourceUtils = getSourceUtils();
        ConverterModel converterModel = new ConverterModel();
        String converterId = sourceUtils.getAnnotationValue(facesConverter, "value", String.class);
        if (null == converterId || converterId.isEmpty()) {
            converterId = sourceUtils.getAnnotationValue(facesConverter, "converterId", String.class);
        }
        if (null != converterId && !converterId.isEmpty()) {
            converterModel.setId(FacesId.parseId(converterId));
        }
        converterModel.setTargetClass(ClassName.parseName(((TypeElement) element).getQualifiedName().toString()));
        converterModel.setGenerate(false);
        setDescription(converterModel, facesConverter, getDocComment(element));
        library.getConverters().add(converterModel);
    }

    @Override
    public Class<? extends Annotation> getProcessedAnnotation() {
        return JsfConverter.class;
    }
}
