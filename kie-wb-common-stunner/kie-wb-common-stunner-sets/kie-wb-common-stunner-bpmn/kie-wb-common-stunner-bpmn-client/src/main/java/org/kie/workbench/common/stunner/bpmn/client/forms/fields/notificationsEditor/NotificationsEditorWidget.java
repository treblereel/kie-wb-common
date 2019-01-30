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

package org.kie.workbench.common.stunner.bpmn.client.forms.fields.notificationsEditor;

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
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.model.Notification;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.model.NotificationRow;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.notificationsEditor.widget.NotificationWidget;

@Dependent
@Templated
public class NotificationsEditorWidget extends Composite implements HasValue<String> {


    private NotificationsEditorWidget.GetNotificationsCallback callback = null;

    private String notificationInfo;

    @Inject
    @DataField
    private Button notificationButton;

    @Inject
    private NotificationWidget notificationWidget;

    @Inject
    @DataField
    private TextInput notificationTextBox;

    private List<Notification> notifications = new ArrayList<>();

    @PostConstruct
    public void init() {
        notificationButton.addEventListener("click", event -> showNotificationsDialog(), false);
        notificationTextBox.addEventListener("click", event -> showNotificationsDialog(), false);
    }

    @Override
    public String getValue() {
        return notificationInfo;
    }

    @Override
    public void setValue(String json) {
        setValue(json,
                false);
    }

    private void updateNotification(String json) {
        JSONArray array = JSONParser.parseStrict(json).isArray();
        notifications.clear();
        for (int i = 0; i < array.size(); i++) {
            notifications.add(Notification.fromJSONObject(array.get(i).isObject()));
        }
    }

    @Override
    public void setValue(String json, boolean fireEvents) {
        if (json != null && !json.isEmpty()) {
            updateNotification(json);
        }
        String oldValue = notificationInfo;
        notificationInfo = json;
        initTextBox();
        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this,
                    oldValue,
                    notificationInfo);
        }
    }

    private void initTextBox() {
        if (notifications == null) {
            notificationTextBox.setValue("empty");
        } else {
            notificationTextBox.setValue("{" + notifications.size() + " } notifications");
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler,
                ValueChangeEvent.getType());
    }

    private void showNotificationsDialog() {
        notificationWidget.setValue(notifications
                .stream(
                ).map(r -> new NotificationRow(r))
                .collect(Collectors.toList()), true);

        notificationWidget.setCallback(reassignmentsData -> setValue(reassignmentsData,
                true));
        notificationWidget.show();
    }

    public void setReadOnly(final boolean readOnly) {
        notificationWidget.setReadOnly(readOnly);
    }

    /**
     * Callback interface which should be implemented by callers to retrieve the
     * edited Notifications data.
     */
    public interface GetNotificationsCallback {

        void getData(String notificationData);
    }
}
