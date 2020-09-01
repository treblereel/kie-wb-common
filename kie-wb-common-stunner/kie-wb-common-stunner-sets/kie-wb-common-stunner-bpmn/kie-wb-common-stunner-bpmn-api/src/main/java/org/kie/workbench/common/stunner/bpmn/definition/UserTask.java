/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
import org.kie.workbench.common.forms.adf.definitions.annotations.FieldParam;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormDefinition;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.forms.adf.definitions.settings.FieldPolicy;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Data;
import org.kie.workbench.common.stunner.bpmn.definition.dto.DataInput;
import org.kie.workbench.common.stunner.bpmn.definition.dto.DataInputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.dto.InputSet;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.Documentation;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.Name;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.TaskGeneralSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.TaskType;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.TaskTypes;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.UserTaskExecutionSet;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.PropertySet;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.Morph;
import org.kie.workbench.common.stunner.core.factory.graph.NodeFactory;
import org.kie.workbench.common.stunner.core.rule.annotation.CanDock;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.kie.workbench.common.stunner.core.util.UUID;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.AbstractEmbeddedFormsInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.AbstractEmbeddedFormsInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition(graphFactory = NodeFactory.class)
@CanDock(roles = {"IntermediateEventOnActivityBoundary"})
@Morph(base = BaseTask.class)
@FormDefinition(
        policy = FieldPolicy.ONLY_MARKED,
        startElement = "general",
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "userTask", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class UserTask extends BaseUserTask<UserTaskExecutionSet> {

    @PropertySet
    @FormField(
            afterElement = "general"
    )
    @Valid
    @XmlTransient
    protected UserTaskExecutionSet executionSet;
    @XmlAttribute
    private String id = UUID.uuid();
    private Name name;
    private Documentation documentation;
    @XmlElementWrapper(name = "extensionElements", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
    @XmlElement(name = "metaData")
    private List<MetaData> extensionElements;
    @XmlElementRefs({
            @XmlElementRef(name = "dataInput", type = DataInput.class),
            @XmlElementRef(name = "inputSet", type = InputSet.class),
    })
    @XmlElementWrapper(name = "ioSpecification", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
    private List<Data> ioSpecification;
    @XmlElementRefs({
            @XmlElementRef(name = "dataInputAssociation", type = DataInputAssociation.class)
    })
    private List<BPMNProperty> bpmnProperties;

    public UserTask() {
        this(new Name("TaskZZZ"),
                new Documentation("DocumentationZZZ"),
                new UserTaskExecutionSet(),
                new BackgroundSet(),
                new FontSet(),
                new RectangleDimensionsSet(),
                new SimulationSet(),
                new TaskType(TaskTypes.USER));
    }

    public UserTask(final @MapsTo("name") Name name,
                    final @MapsTo("documentation") Documentation documentation,
                    final @MapsTo("executionSet") UserTaskExecutionSet executionSet,
                    final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                    final @MapsTo("fontSet") FontSet fontSet,
                    final @MapsTo("dimensionsSet") RectangleDimensionsSet dimensionsSet,
                    final @MapsTo("simulationSet") SimulationSet simulationSet,
                    final @MapsTo("taskType") TaskType taskType) {
        super(new TaskGeneralSet(name, documentation),
                backgroundSet,
                fontSet,
                dimensionsSet,
                simulationSet,
                taskType);
        this.name = name;
        this.documentation = documentation;
        this.executionSet = executionSet;
    }

    @Override
    public UserTaskExecutionSet getExecutionSet() {
        return executionSet;
    }

    @Override
    public void setExecutionSet(final UserTaskExecutionSet executionSet) {
        this.executionSet = executionSet;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    public void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
    }

    @Override
    public TaskGeneralSet getGeneral() {
        return new TaskGeneralSet(name, documentation);
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                executionSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserTask) {
            UserTask other = (UserTask) o;
            return super.equals(other) &&
                    executionSet.equals(other.executionSet);
        }
        return false;
    }

    public List<BPMNProperty> getBpmnProperties() {
        List<BPMNProperty> result = new ArrayList<>();
        result.add(new DataInputAssociation(this, executionSet.getTaskName().getValue(), "TaskNameInputX"));
        result.add(new DataInputAssociation(this, executionSet.getSkippable().getValue().toString(), "SkippableInputX"));
        result.add(new DataInputAssociation(this, executionSet.getDescription().getValue(), "DescriptionInputX"));
        result.add(new DataInputAssociation(this, executionSet.getPriority().getValue(), "PriorityInputX"));
        result.add(new DataInputAssociation(this, executionSet.getDescription().getValue(), "DescriptionInputX"));
        return result;
    }

    public void setBpmnProperties(List<BPMNProperty> bpmnProperties) {
        this.bpmnProperties = bpmnProperties;
    }

    public List<MetaData> getExtensionElements() {
        List<MetaData> metaDataList = new ArrayList<>();
        metaDataList.add(new MetaData("elementname", this.executionSet.getTaskName().getValue()));
        metaDataList.add(new MetaData("customAsync", this.executionSet.getIsAsync().getValue().toString()));
        metaDataList.add(new MetaData("customAutoStart", this.executionSet.getAdHocAutostart().getValue().toString()));
        return metaDataList;
    }

    public void setExtensionElements(List<MetaData> extensionElements) {
        this.extensionElements = extensionElements;
    }

    public List<Data> getIoSpecification() {
        List<Data> result = new ArrayList<>();
        result.add(new DataInput(getId(), "TaskNameInputX", executionSet.getTaskName().getValue()));
        result.add(new DataInput(getId(), "SkippableInputX", executionSet.getSkippable().getValue().toString()));
        result.add(new DataInput(getId(), "DescriptionInputX", executionSet.getDescription().getValue()));
        result.add(new DataInput(getId(), "PriorityInputX", executionSet.getPriority().getValue()));
        result.add(new DataInput(getId(), "ContentInputX", executionSet.getContent().getValue()));

        InputSet inputSet = new InputSet();
        List<String> list = new ArrayList<>();
        list.add(getId() + "_TaskNameInputX");
        list.add(getId() + "_SkippableInputX");
        list.add(getId() + "_CommentInputX");
        list.add(getId() + "_DescriptionInputX");
        list.add(getId() + "_PriorityInputX");
        list.add(getId() + "_ContentInputX");
        inputSet.setSet(list);
        result.add(inputSet);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIoSpecification(List<Data> ioSpecification) {
        this.ioSpecification = ioSpecification;
    }
}
