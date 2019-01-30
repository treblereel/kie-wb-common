/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.bpmn.client.forms.widgets;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.IntegerBox;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.model.Duration;

@Dependent
@Templated
public class PeriodBox extends Composite implements IsWidget, HasValue<String> {

    private String current;

    @DataField
    @Inject
    private IntegerBox numberPeriod;

    @DataField
    private Select unitPeriod = new Select();

    @Inject
    @DataField
    private Div divPeriodInputGroup;

    private HandlerManager handlerManager = createHandlerManager();

    @PostConstruct
    public void init() {
        initTypeSelector();
        numberPeriod.addValueChangeHandler(event -> {
            String newValue = getValue();
            setValue(newValue, true);

        });
        unitPeriod.addValueChangeHandler(event -> {
            String newValue = getValue();
            setValue(newValue, true);
        });

    }

    private void initTypeSelector() {
        Arrays.stream(Duration.values()).forEach(p -> {
            createOptionAndAddtoSelect(unitPeriod, p.getType(), p.getAlias());
        });
    }

    private void createOptionAndAddtoSelect(Select typeSelector, String name, String value) {
        Option option = new Option();
        option.setValue(value);
        option.setText(name);
        typeSelector.add(option);
    }

    @Override
    public String getValue() {
        return numberPeriod.getValue() + "" + unitPeriod.getSelectedItem().getValue();
    }

    @Override
    public void setValue(String value) {
        parse(value);
        setValue(value, false);
    }

    private void parse(String value) {
        if (value != null && value.length() >= 2) {
            String unit = value.substring(0, value.length() - 1);
            numberPeriod.setValue(Integer.valueOf(unit));
            String duration = value.substring(value.length() - 1);
            unitPeriod.setValue(Duration.get(duration).getAlias());
        }
    }

    @Override
    public void setValue(String value, boolean fireEvents) {
        String oldValue = current;
        current = value;
        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
        }
    }

    public void onShow() {
        unitPeriodSelectorWidth();
    }

    private void unitPeriodSelectorWidth() {
        for (int i = 0; i < divPeriodInputGroup.getChildNodes().getLength(); i++) {
            Element element = (Element) divPeriodInputGroup.getChildNodes().item(i);
            if (element != null && element.getTagName() != null
                    && element.getTagName().toLowerCase().equals(DivElement.TAG)) {
                DivElement div = (DivElement) element;
                if (div.getClassName().contains("bootstrap-select input-group-btn")) {
                    div.getStyle().setWidth(90, Style.Unit.PX);
                }
            }
        }
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }
}
