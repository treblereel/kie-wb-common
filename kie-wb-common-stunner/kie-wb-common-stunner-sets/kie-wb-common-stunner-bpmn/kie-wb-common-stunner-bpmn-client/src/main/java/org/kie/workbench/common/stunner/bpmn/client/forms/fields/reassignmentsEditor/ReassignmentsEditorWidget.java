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

package org.kie.workbench.common.stunner.bpmn.client.forms.fields.reassignmentsEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.common.client.dom.TextInput;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.model.Reassignment;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.model.ReassignmentRow;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.reassignmentsEditor.widget.ReassignmentWidget;

@Dependent
@Templated
public class ReassignmentsEditorWidget extends Composite implements HasValue<String> {

    private GetReassignmentsCallback callback = null;

    private String reassignmentsInfo;

    @Inject
    @DataField
    private Button reassignmentsButton;

    @Inject
    private ReassignmentWidget reassignmentWidget;

    @Inject
    @DataField
    private TextInput reassignmentsTextBox;

    private List<Reassignment> reassignments = new ArrayList<>();

    public ReassignmentsEditorWidget() {

    }

    @PostConstruct
    public void init() {
        reassignmentsButton.addEventListener("click", event -> showReassignmentsDialog(), false);
        reassignmentsTextBox.addEventListener("click", event -> showReassignmentsDialog(), false);
    }

    @Override
    public String getValue() {
        return reassignmentsInfo;
    }

    @Override
    public void setValue(String json) {
        setValue(json,
                false);
    }

    private void updateReassignment(String json) {
        JSONArray array = JSONParser.parseStrict(json).isArray();
        reassignments.clear();
        for (int i = 0; i < array.size(); i++) {
            reassignments.add(Reassignment.fromJSONObject(array.get(i).isObject()));
        }
    }

    @Override
    public void setValue(String json, boolean fireEvents) {
        if (json != null && !json.isEmpty()) {
            updateReassignment(json);
        }
        String oldValue = reassignmentsInfo;
        reassignmentsInfo = json;
        initTextBox();
        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this,
                    oldValue,
                    reassignmentsInfo);
        }
    }

    private void initTextBox() {
        if (reassignments == null) {
            reassignmentsTextBox.setValue("empty");
        } else {
            reassignmentsTextBox.setValue("{" + reassignments.size() + " } reassignments");
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler,
                ValueChangeEvent.getType());
    }

    private void showReassignmentsDialog() {
        reassignmentWidget.setValue(reassignments
                .stream(
                ).map(r -> new ReassignmentRow(r))
                .collect(Collectors.toList()), true);

        reassignmentWidget.setCallback(reassignmentsData -> setValue(reassignmentsData,
                true));
        reassignmentWidget.show();
    }

    public void setReadOnly(final boolean readOnly) {
        reassignmentWidget.setReadOnly(readOnly);
    }

    /**
     * Callback interface which should be implemented by callers to retrieve the
     * edited Reassignments data.
     */
    public interface GetReassignmentsCallback {

        void getData(String reassignmentsData);
    }
}
