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

package org.kie.workbench.common.stunner.bpmn.definition.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemDefinition", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class ItemDefinition {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String structureRef = "Object";

    public ItemDefinition() {

    }

    public ItemDefinition(String id, String postfix) {
        this.id = "_" + id + "_" + postfix + "InputXItem";
    }

    public ItemDefinition(String id, String postfix, String structureRef) {
        this.id = "_" + id + "_" + postfix;
        this.structureRef = structureRef;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStructureRef() {
        return structureRef;
    }

    public void setStructureRef(String structureRef) {
        this.structureRef = structureRef;
    }
}
