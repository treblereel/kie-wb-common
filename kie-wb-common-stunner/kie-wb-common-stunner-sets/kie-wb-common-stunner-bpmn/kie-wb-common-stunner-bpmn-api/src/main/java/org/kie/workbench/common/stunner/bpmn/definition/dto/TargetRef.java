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

import javax.xml.bind.annotation.XmlRootElement;

import org.kie.workbench.common.stunner.bpmn.definition.dto.handler.TargetRefValueDemarshaller;
import org.kie.workbench.common.stunner.bpmn.definition.dto.handler.TargetRefValueMarshaller;
import org.treblereel.gwt.jackson.api.annotation.XmlTypeAdapter;

@XmlRootElement(name = "targetRef", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class TargetRef {

    private TargetRefValue value;

    public TargetRef() {

    }

    public TargetRef(String value) {
        this.value = new TargetRefValue(value);
    }

    public TargetRefValue getValue() {
        return value;
    }

    public void setValue(TargetRefValue value) {
        this.value = value;
    }

    @XmlTypeAdapter(
            serializer = TargetRefValueMarshaller.class,
            deserializer = TargetRefValueDemarshaller.class)
    public static class TargetRefValue {
        private String value;

        public TargetRefValue() {
        }

        public TargetRefValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
