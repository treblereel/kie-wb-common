/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.backend.converters.fromstunner.properties;

import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.ItemDefinition;

import static org.kie.workbench.common.stunner.bpmn.backend.converters.fromstunner.Factories.bpmn2;

public class DataObjectPropertyWriter extends PropertyWriter {

    private final Set<DataObject> dataObjects;

    private final DataObject dataObject;

    public DataObjectPropertyWriter(DataObjectReference element,
                                    VariableScope variableScope,
                                    Set<DataObject> dataObjects) {
        super(element, variableScope);
        this.dataObjects = dataObjects;
        dataObject = bpmn2.createDataObject();
        element.setDataObjectRef(dataObject);
    }

    public void setName(String value) {
        final String escaped = StringEscapeUtils.escapeXml10(value.trim());
        dataObject.setName(escaped);
        dataObject.setId(escaped);
        addDataObjectToProcess(dataObject);
    }

    private void addDataObjectToProcess(DataObject dataObject) {
        if (!dataObjects.stream()
                .anyMatch(elm -> elm.getId()
                .equals(dataObject.getId()))) {
            dataObjects.add(dataObject);
        }
    }

    public void setType(String type) {
        ItemDefinition itemDefinition = bpmn2.createItemDefinition();
        itemDefinition.setStructureRef(type);
        dataObject.setItemSubjectRef(itemDefinition);
    }

    @Override
    public DataObjectReference getElement() {
        return (DataObjectReference) super.getElement();
    }

    public Set<DataObject> getDataObjects() {
        return dataObjects;
    }
}

