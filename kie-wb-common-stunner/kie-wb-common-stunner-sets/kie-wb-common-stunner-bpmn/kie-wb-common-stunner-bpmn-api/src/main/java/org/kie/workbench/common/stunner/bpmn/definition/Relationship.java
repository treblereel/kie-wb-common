/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.definition;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.BPSimData;
import org.treblereel.gwt.jackson.api.annotation.XmlUnwrappedCollection;

@XmlRootElement(name = "relationship", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class Relationship {

    @XmlAttribute
    private String type = "BPSimData";

    @XmlElementWrapper(name = "extensionElements", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
    @XmlUnwrappedCollection
    @XmlElement(name = "BPSimData", namespace = "http://www.bpsim.org/schemas/1.0")
    private List<BPSimData> extensionElements;

    @XmlElement(name = "source")
    private String source;

    @XmlElement(name = "target")
    private String target;

    public Relationship() {

    }

    public Relationship(String id) {
        this.source = id;
        this.target = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<BPSimData> getExtensionElements() {
        return extensionElements;
    }

    public void setExtensionElements(List<BPSimData> extensionElements) {
        this.extensionElements = extensionElements;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
