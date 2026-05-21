/*
 * $Id$
 *
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
package org.richfaces.cdk.xmlconfig.model;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.model.ComponentLibrary;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class FacesConfigAdapter extends AdapterBase<FacesConfigBean, ComponentLibrary> {
    private static final ConverterAdapter CONVERTER_ADAPTER = new ConverterAdapter();
    private static final ValidatorAdapter VALIDATOR_ADAPTER = new ValidatorAdapter();

    @Override
    protected ComponentLibrary createModelElement(Class<? extends ComponentLibrary> modelClass, FacesConfigBean adapter) {
        List<ConverterBean> converters = new ArrayList<ConverterBean>(adapter.getConverters());
        List<ValidatorBean> validators = new ArrayList<ValidatorBean>(adapter.getValidators());
        adapter.setConverters(new ArrayList<ConverterBean>());
        adapter.setValidators(new ArrayList<ValidatorBean>());
        try {
            ComponentLibrary model = super.createModelElement(modelClass, adapter);
            adapter.setConverters(converters);
            adapter.setValidators(validators);
            return model;
        } catch (RuntimeException e) {
            adapter.setConverters(converters);
            adapter.setValidators(validators);
            throw e;
        }
    }

    @Override
    public FacesConfigBean createBean(Class<? extends FacesConfigBean> beanClass, ComponentLibrary model) throws CdkException {
        List<org.richfaces.cdk.model.ConverterModel> converters = new ArrayList<org.richfaces.cdk.model.ConverterModel>(
                model.getConverters());
        List<org.richfaces.cdk.model.ValidatorModel> validators = new ArrayList<org.richfaces.cdk.model.ValidatorModel>(
                model.getValidators());
        model.getConverters().clear();
        model.getValidators().clear();
        try {
            FacesConfigBean bean = super.createBean(beanClass, model);
            model.getConverters().addAll(converters);
            model.getValidators().addAll(validators);
            return bean;
        } catch (RuntimeException e) {
            model.getConverters().addAll(converters);
            model.getValidators().addAll(validators);
            throw e;
        }
    }

    @Override
    protected void postUnmarshal(FacesConfigBean bean, ComponentLibrary model) {
        for (ConverterBean converterBean : bean.getConverters()) {
            model.getConverters().add(CONVERTER_ADAPTER.unmarshal(converterBean));
        }
        for (ValidatorBean validatorBean : bean.getValidators()) {
            model.getValidators().add(VALIDATOR_ADAPTER.unmarshal(validatorBean));
        }
    }

    @Override
    protected void postMarshal(ComponentLibrary model, FacesConfigBean bean) {
        for (org.richfaces.cdk.model.ConverterModel converterModel : model.getConverters()) {
            bean.getConverters().add(CONVERTER_ADAPTER.marshal(converterModel));
        }
        for (org.richfaces.cdk.model.ValidatorModel validatorModel : model.getValidators()) {
            bean.getValidators().add(VALIDATOR_ADAPTER.marshal(validatorModel));
        }
    }

    @Override
    protected Class<? extends FacesConfigBean> getBeanClass(ComponentLibrary model) {
        return FacesConfigBean.class;
    }

    @Override
    protected Class<? extends ComponentLibrary> getModelClass(FacesConfigBean bean) {
        return ComponentLibrary.class;
    }
}
