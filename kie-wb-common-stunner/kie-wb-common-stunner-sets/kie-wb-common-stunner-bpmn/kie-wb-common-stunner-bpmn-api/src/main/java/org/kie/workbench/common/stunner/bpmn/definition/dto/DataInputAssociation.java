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

import org.kie.workbench.common.stunner.bpmn.definition.BPMNProperty;
import org.kie.workbench.common.stunner.bpmn.definition.UserTask;

@XmlRootElement(name = "dataInput", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class DataInputAssociation implements BPMNProperty {

    private SourceRef sourceRef;

    private TargetRef targetRef;

    private Assignment assignment;

    public DataInputAssociation() {

    }

    public DataInputAssociation(UserTask userTask, String value, String postfix) {
        targetRef = new TargetRef(userTask.getId() + "_" + postfix);
        assignment = new Assignment(value, userTask.getId(), postfix);
    }

    public SourceRef getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(SourceRef sourceRef) {
        this.sourceRef = sourceRef;
    }

    public TargetRef getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(TargetRef targetRef) {
        this.targetRef = targetRef;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

}
