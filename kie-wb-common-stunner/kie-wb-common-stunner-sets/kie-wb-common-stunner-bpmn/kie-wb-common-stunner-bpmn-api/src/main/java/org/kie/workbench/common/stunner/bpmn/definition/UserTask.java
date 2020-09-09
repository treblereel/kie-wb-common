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
import org.kie.workbench.common.stunner.bpmn.definition.dto.DataInputRefs;
import org.kie.workbench.common.stunner.bpmn.definition.dto.DataOutput;
import org.kie.workbench.common.stunner.bpmn.definition.dto.DataOutputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.dto.InputSet;
import org.kie.workbench.common.stunner.bpmn.definition.dto.MultiInstanceLoopCharacteristics;
import org.kie.workbench.common.stunner.bpmn.definition.dto.OutputSet;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.ExtensionElement;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.Import;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.OnEntryScript;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.OnExitScript;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.Documentation;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.Name;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.TaskGeneralSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.ScriptTypeValue;
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
import org.treblereel.gwt.jackson.api.annotation.XmlUnwrappedCollection;

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
    @XmlElementRefs({
            @XmlElementRef(name = "metaData", type = MetaData.class),
            @XmlElementRef(name = "import", type = Import.class),
            @XmlElementRef(name = "onEntry-script", type = OnEntryScript.class),
            @XmlElementRef(name = "onExit-script", type = OnExitScript.class),
    })
    @XmlElement(name = "extensionElements", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
    private List<ExtensionElement> extensionElements;
    @XmlElementRefs({
            @XmlElementRef(name = "dataInput", type = DataInput.class),
            @XmlElementRef(name = "dataOutput", type = DataOutput.class),
            @XmlElementRef(name = "inputSet", type = InputSet.class),
            @XmlElementRef(name = "outputSet", type = OutputSet.class)
    })
    @XmlElement(name = "ioSpecification", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
    private List<Data> ioSpecification;
    @XmlElementRefs({
            @XmlElementRef(name = "dataInputAssociation", type = DataInputAssociation.class),
            @XmlElementRef(name = "dataOutputAssociation", type = DataOutputAssociation.class),
            @XmlElementRef(name = "multiInstanceLoopCharacteristics", type = MultiInstanceLoopCharacteristics.class)
    })
    @XmlUnwrappedCollection
    private List<BPMNProperty> bpmnProperties;

    public UserTask() {
        this(new Name("Task"),
                new Documentation(""),
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

    public List<ExtensionElement> getExtensionElements() {
        List<ExtensionElement> metaDataList = new ArrayList<>();
        if (this.executionSet.getTaskName().getValue() != null) {
            metaDataList.add(new MetaData("elementname", this.executionSet.getTaskName().getValue()));
        }
        if (this.executionSet.getIsAsync().getValue() != null) {
            metaDataList.add(new MetaData("customAsync", this.executionSet.getIsAsync().getValue().toString()));
        }
        if (this.executionSet.getAdHocAutostart().getValue() != null) {
            metaDataList.add(new MetaData("customAutoStart", this.executionSet.getAdHocAutostart().getValue().toString()));
        }
        if (this.executionSet.getAdHocAutostart().getValue() != null) {
            metaDataList.add(new MetaData("customSLADueDate", this.executionSet.getSlaDueDate().getValue()));
        }
        for (ScriptTypeValue value : this.executionSet.getOnExitAction().getValue().getValues()) {
            if (!value.getScript().isEmpty()) {
                metaDataList.add(new OnExitScript(value.getScript(), value.getLanguage()));
            }
        }

        for (ScriptTypeValue value : this.executionSet.getOnEntryAction().getValue().getValues()) {
            if (!value.getScript().isEmpty()) {
                metaDataList.add(new OnEntryScript(value.getScript(), value.getLanguage()));
            }
        }

        return metaDataList;
    }

    public void setExtensionElements(List<ExtensionElement> extensionElements) {
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
        List<DataInputRefs> list = new ArrayList<>();
        list.add(new DataInputRefs(getId() + "_TaskNameInputX"));
        list.add(new DataInputRefs(getId() + "_SkippableInputX"));
        list.add(new DataInputRefs(getId() + "_CommentInputX"));
        list.add(new DataInputRefs(getId() + "_DescriptionInputX"));
        list.add(new DataInputRefs(getId() + "_PriorityInputX"));
        list.add(new DataInputRefs(getId() + "_ContentInputX"));
        inputSet.setDataInputRefs(list);
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
