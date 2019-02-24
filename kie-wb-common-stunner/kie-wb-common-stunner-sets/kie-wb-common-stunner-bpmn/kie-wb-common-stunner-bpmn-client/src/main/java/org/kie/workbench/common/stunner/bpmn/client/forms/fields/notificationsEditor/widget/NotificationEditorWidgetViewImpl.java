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

package org.kie.workbench.common.stunner.bpmn.client.forms.fields.notificationsEditor.widget;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.forms.dynamic.client.rendering.renderers.lov.selector.input.MultipleSelectorInput;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.assigneeEditor.widget.AssigneeLiveSearchService;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.model.NotificationRow;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.model.NotificationType;
import org.kie.workbench.common.stunner.bpmn.client.forms.fields.notificationsEditor.event.NotificationEvent;
import org.kie.workbench.common.stunner.bpmn.client.forms.widgets.PeriodBox;
import org.kie.workbench.common.stunner.bpmn.forms.model.AssigneeType;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;
import org.uberfire.ext.widgets.common.client.dropdown.LiveSearchDropDown;
import org.uberfire.ext.widgets.common.client.dropdown.MultipleLiveSearchSelectionHandler;
import org.uberfire.ext.widgets.common.client.dropdown.SingleLiveSearchSelectionHandler;

@Dependent
@Templated("NotificationEditorWidgetViewImpl.html#container")
public class NotificationEditorWidgetViewImpl extends Composite implements NotificationEditorWidgetView {

    private Presenter presenter;

    private BaseModal modal = new BaseModal();


    private NotificationRow current;

    @Inject
    @AutoBound
    private DataBinder<NotificationRow> customerBinder;

    @Inject
    private Event<NotificationEvent> notificationEvent;

    private AssigneeLiveSearchService assigneeLiveSearchService;

    private AssigneeLiveSearchService groupsLiveSearchService;

    @DataField
    @Bound(property = "users")
    private MultipleSelectorInput<String> multipleSelectorInputUsers;

    @DataField
    @Bound(property = "groups")
    private MultipleSelectorInput<String> multipleSelectorInputGroups;

    @DataField
    @Bound(property = "type")
    private Select typeSelect = new Select();

    @DataField
    private LiveSearchDropDown<String> liveSearchFromDropDown;

    @DataField
    private LiveSearchDropDown<String> liveSearchReplyToDropDown;

    @Inject
    @DataField
    @Bound(property = "expiresAt")
    private PeriodBox periodBox;

    private Option notStarted = new Option();

    private Option notCompleted = new Option();

    @Inject
    @DataField
    @Bound(property = "subject")
    private TextBox subject;

    @Inject
    @DataField
    @Bound(property = "body")
    private TextArea body;

    private MultipleLiveSearchSelectionHandler<String> multipleLiveSearchSelectionHandlerUsers = new MultipleLiveSearchSelectionHandler();

    private MultipleLiveSearchSelectionHandler<String> multipleLiveSearchSelectionHandlerGroups = new MultipleLiveSearchSelectionHandler();

    private SingleLiveSearchSelectionHandler<String> searchSelectionFromHandler = new SingleLiveSearchSelectionHandler<>();

    private SingleLiveSearchSelectionHandler<String> searchSelectionReplyToHandler = new SingleLiveSearchSelectionHandler<>();


    @DataField
    @Inject
    private Button closeButton, saveButton;

    @Inject
    public NotificationEditorWidgetViewImpl(final MultipleSelectorInput multipleSelectorInputUsers,
                                            final MultipleSelectorInput multipleSelectorInputGroups,
                                            final LiveSearchDropDown liveSearchFromDropDown,
                                            final LiveSearchDropDown liveSearchReplyToDropDown,
                                            final AssigneeLiveSearchService assigneeLiveSearchService,
                                            final AssigneeLiveSearchService groupLiveSearchService) {
        initUsersAndGroupsDropdowns(multipleSelectorInputUsers,
                multipleSelectorInputGroups,
                liveSearchFromDropDown,
                liveSearchReplyToDropDown,
                assigneeLiveSearchService,
                groupLiveSearchService);
        initTypeSelector();
    }

    private void initTextBoxes() {
        subject.setMaxLength(100);

        body.setMaxLength(500);
        body.setWidth("350px");
        body.setHeight("70px");
    }

    private void initUsersAndGroupsDropdowns(MultipleSelectorInput multipleSelectorInputUsers,
                                             MultipleSelectorInput multipleSelectorInputGroups,
                                             LiveSearchDropDown<String> liveSearchFromDropDown,
                                             LiveSearchDropDown<String> liveSearchReplyToDropDown,
                                             AssigneeLiveSearchService assigneeLiveSearchService,
                                             AssigneeLiveSearchService groupLiveSearchService) {

        this.assigneeLiveSearchService = assigneeLiveSearchService;
        this.groupsLiveSearchService = groupLiveSearchService;

        this.multipleSelectorInputUsers = multipleSelectorInputUsers;
        this.multipleSelectorInputGroups = multipleSelectorInputGroups;

        this.liveSearchFromDropDown = liveSearchFromDropDown;
        this.liveSearchReplyToDropDown = liveSearchReplyToDropDown;

        this.assigneeLiveSearchService.init(AssigneeType.USER);
        this.groupsLiveSearchService.init(AssigneeType.GROUP);

        this.multipleSelectorInputUsers.init(assigneeLiveSearchService, multipleLiveSearchSelectionHandlerUsers);
        this.multipleSelectorInputGroups.init(groupLiveSearchService, multipleLiveSearchSelectionHandlerGroups);

        this.liveSearchFromDropDown.init(assigneeLiveSearchService, searchSelectionFromHandler);
        this.liveSearchReplyToDropDown.init(assigneeLiveSearchService, searchSelectionReplyToHandler);
    }

    private void initTypeSelector() {
        notStarted.setValue(NotificationType.NotStartedNotify.getAlias());
        notStarted.setText(NotificationType.NotStartedNotify.getType());
        notCompleted.setText(NotificationType.NotCompletedNotify.getType());
        notCompleted.setValue(NotificationType.NotCompletedNotify.getAlias());

        typeSelect.add(notStarted);
        typeSelect.add(notCompleted);
    }

    @PostConstruct
    public void init() {
        closeButton.addEventListener("click", event -> close(), false);
        saveButton.addEventListener("click", event -> save(), false);
    }

    @Override
    public void init(final Presenter presenter) {
        this.presenter = presenter;

        initModel();
        initTextBoxes();
    }

    private void initModel() {
        modal.setTitle(presenter.getNameHeader());
        modal.setHeight("550px");
        modal.setBody(this);
        modal.setClosable(false);
        modal.addShowHandler(modalShowEvent -> periodBox.onShow());

        this.liveSearchFromDropDown.setOnChange(() -> customerBinder.getWorkingModel().setFrom(searchSelectionFromHandler.getSelectedValue()));
        this.liveSearchReplyToDropDown.setOnChange(() -> customerBinder.getWorkingModel().setReplyTo(searchSelectionReplyToHandler.getSelectedValue()));

    }

    @Override
    public void createOrEdit(NotificationWidgetView parent, NotificationRow row) {
        current = row;
        customerBinder.setModel(row.clone());
        if (row.getUsers() != null && row.getUsers().size() > 0) {
            row.getUsers().forEach(u -> assigneeLiveSearchService.addCustomEntry(u));
            multipleSelectorInputUsers.setValue(row.getUsers());
        }

        if (row.getGroups() != null && row.getGroups().size() > 0) {
            row.getGroups().forEach(u -> groupsLiveSearchService.addCustomEntry(u));
            multipleSelectorInputGroups.setValue(row.getGroups());
        }

        if (row.getFrom() != null && !row.getFrom().isEmpty()) {
            assigneeLiveSearchService.addCustomEntry(row.getFrom());
            liveSearchFromDropDown.setSelectedItem(row.getFrom());
        }

        if (row.getReplyTo() != null && !row.getReplyTo().isEmpty()) {
            assigneeLiveSearchService.addCustomEntry(row.getReplyTo());
            liveSearchReplyToDropDown.setSelectedItem(row.getReplyTo());
        }

        if (row.getType() != null) {
            if (row.getType().equals(NotificationType.NotCompletedNotify)) {
                notStarted.setSelected(false);
                notCompleted.setSelected(true);
            } else {
                notStarted.setSelected(true);
                notCompleted.setSelected(false);
            }
        }
        modal.show();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        saveButton.setDisabled(readOnly);
    }

    private void save() {
        // TODO looks like errai data binder doenst support liststore widgets.
        current.setUsers(multipleLiveSearchSelectionHandlerUsers.getSelectedValues());
        current.setGroups(multipleLiveSearchSelectionHandlerGroups.getSelectedValues());
        current.setBody(body.getValue());
        current.setSubject(subject.getValue());
        current.setFrom(searchSelectionFromHandler.getSelectedValue() != null ? searchSelectionFromHandler.getSelectedValue() : "");
        current.setReplyTo(searchSelectionReplyToHandler.getSelectedValue() != null ? searchSelectionReplyToHandler.getSelectedValue() : "");
        current.setExpiresAt(customerBinder.getModel().getExpiresAt());
        current.setType(NotificationType.get(typeSelect.getSelectedItem().getValue()));
        notificationEvent.fire(new NotificationEvent(current));
        hide();
    }

    private void close() {
        notificationEvent.fire(new NotificationEvent(null));
        hide();
    }

    private void hide(){
        //clear widgets and set default values
        multipleSelectorInputUsers.setValue(Collections.EMPTY_LIST);
        multipleSelectorInputGroups.setValue(Collections.EMPTY_LIST);
        subject.clear();
        body.clear();
        liveSearchFromDropDown.clearSelection();
        liveSearchReplyToDropDown.clearSelection();
        periodBox.setValue("1h");
        notStarted.setSelected(true);
        modal.hide();
    }

}
