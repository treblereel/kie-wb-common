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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kie.workbench.common.stunner.bpmn.definition.BPMNProperty;
import org.kie.workbench.common.stunner.bpmn.definition.UserTask;
import org.kie.workbench.common.stunner.bpmn.definition.dto.handler.AssignmentDemarshaller;
import org.kie.workbench.common.stunner.bpmn.definition.dto.handler.AssignmentMarshaller;
import org.treblereel.gwt.jackson.api.annotation.XmlTypeAdapter;

@XmlRootElement(name = "dataInput", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class DataInputAssociation implements BPMNProperty {

    @XmlElement(name = "bpmn2:targetRef")
    private String targetRef;

    private Assignment assignment;

    public DataInputAssociation() {

    }

    public DataInputAssociation(UserTask userTask, String value, String postfix) {
        targetRef = userTask.getId() + "_" + postfix;
        assignment = new Assignment(value, userTask.getId().toString(), postfix);
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    @XmlTypeAdapter(
            serializer = AssignmentMarshaller.class,
            deserializer = AssignmentDemarshaller.class)
    public static class Assignment {
        private String from;
        private String to;

        public Assignment() {

        }

        public Assignment(String value, String id, String postfix) {
            this.from = value;
            this.to = id + "_" + postfix;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}
