/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.dmn.client.editors.types.listview;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import elemental2.dom.HTMLElement;
import org.kie.workbench.common.dmn.client.editors.types.common.DataType;
import org.kie.workbench.common.dmn.client.editors.types.common.DataTypeManager;
import org.kie.workbench.common.dmn.client.editors.types.listview.common.SmallSwitchComponent;
import org.kie.workbench.common.dmn.client.editors.types.listview.confirmation.DataTypeConfirmation;
import org.uberfire.client.mvp.UberElemental;
import org.uberfire.mvp.Command;

import static org.kie.workbench.common.dmn.client.editors.types.persistence.CreationType.ABOVE;
import static org.kie.workbench.common.dmn.client.editors.types.persistence.CreationType.BELOW;
import static org.kie.workbench.common.dmn.client.editors.types.persistence.CreationType.NESTED;
import static org.kie.workbench.common.stunner.core.util.StringUtils.isEmpty;

@Dependent
public class DataTypeListItem {

    private final View view;

    private final DataTypeSelect dataTypeSelectComponent;

    private final DataTypeConstraint dataTypeConstraintComponent;

    private final SmallSwitchComponent dataTypeListComponent;

    private final DataTypeManager dataTypeManager;

    private final DataTypeConfirmation confirmation;

    private DataType dataType;

    private int level;

    private DataTypeList dataTypeList;

    private String oldName;

    private String oldType;

    private String oldConstraint;

    private boolean oldIsList;

    @Inject
    public DataTypeListItem(final View view,
                            final DataTypeSelect dataTypeSelectComponent,
                            final DataTypeConstraint dataTypeConstraintComponent,
                            final SmallSwitchComponent dataTypeListComponent,
                            final DataTypeManager dataTypeManager,
                            final DataTypeConfirmation confirmation) {
        this.view = view;
        this.dataTypeSelectComponent = dataTypeSelectComponent;
        this.dataTypeConstraintComponent = dataTypeConstraintComponent;
        this.dataTypeListComponent = dataTypeListComponent;
        this.dataTypeManager = dataTypeManager;
        this.confirmation = confirmation;
    }

    @PostConstruct
    void setup() {
        view.init(this);
    }

    public void init(final DataTypeList dataTypeList) {
        this.dataTypeList = dataTypeList;
    }

    public HTMLElement getElement() {
        return view.getElement();
    }

    void setupDataType(final DataType dataType,
                       final int level) {

        this.dataType = dataType;
        this.level = level;

        setupSelectComponent();
        setupConstraintComponent();
        setupListComponent();
        setupView();
    }

    void setupListComponent() {
        dataTypeListComponent.setValue(getDataType().isList());
        refreshListYesLabel();
    }

    void setupConstraintComponent() {
        dataTypeConstraintComponent.init(getDataType());
    }

    void setupSelectComponent() {
        dataTypeSelectComponent.init(this, getDataType());
    }

    void setupView() {
        view.setupSelectComponent(dataTypeSelectComponent);
        view.setupConstraintComponent(dataTypeConstraintComponent);
        view.setupListComponent(dataTypeListComponent);
        view.setDataType(getDataType());
    }

    void refresh() {
        dataTypeSelectComponent.refresh();
        dataTypeSelectComponent.init(this, getDataType());
        view.setName(getDataType().getName());
        view.setConstraint(getDataType().getConstraint());
        setupListComponent();
        setupConstraintComponent();
    }

    public DataType getDataType() {
        return dataType;
    }

    public int getLevel() {
        return level;
    }

    void expandOrCollapseSubTypes() {
        if (view.isCollapsed()) {
            expand();
        } else {
            collapse();
        }
    }

    public void expand() {
        view.expand();
    }

    public void collapse() {
        view.collapse();
    }

    void refreshSubItems(final List<DataType> dataTypes) {

        dataTypeList.refreshSubItemsFromListItem(this, dataTypes);

        view.enableFocusMode();
        view.toggleArrow(!dataTypes.isEmpty());
    }

    public void enableEditMode() {

        if (view.isOnFocusMode()) {
            return;
        }

        oldName = getDataType().getName();
        oldType = getDataType().getType();
        oldConstraint = getDataType().getConstraint();
        oldIsList = getDataType().isList();

        view.showSaveButton();
        view.showDataTypeNameInput();
        view.showConstraintContainer();
        view.showListContainer();
        view.hideKebabMenu();
        view.hideConstraintText();
        view.hideListYesLabel();
        view.enableFocusMode();

        dataTypeSelectComponent.enableEditMode();
        dataTypeConstraintComponent.refreshView();
    }

    public void disableEditMode() {
        if (view.isOnFocusMode()) {
            discardNewDataType();
            closeEditMode();
        }
    }

    public void saveAndCloseEditMode() {

        final DataType updatedDataType = updateProperties(getDataType());

        if (updatedDataType.isValid()) {
            confirmation.ifDataTypeDoesNotHaveLostSubDataTypes(updatedDataType, doSaveAndCloseEditMode(updatedDataType), doDisableEditMode());
        } else {
            discardDataTypeProperties();
        }
    }

    Command doDisableEditMode() {
        return this::disableEditMode;
    }

    Command doSaveAndCloseEditMode(final DataType dataType) {
        return () -> {
            final List<DataType> updateDataTypes = persist(dataType);
            dataTypeList.refreshItemsByUpdatedDataTypes(updateDataTypes);
            closeEditMode();
        };
    }

    List<DataType> persist(final DataType dataType) {
        return dataTypeManager
                .from(dataType)
                .withSubDataTypes(dataTypeSelectComponent.getSubDataTypes())
                .get()
                .update();
    }

    void discardNewDataType() {

        final DataType oldDataType = discardDataTypeProperties();

        view.setDataType(oldDataType);

        setupListComponent();
        setupSelectComponent();
        refreshSubItems(oldDataType.getSubDataTypes());
    }

    DataType discardDataTypeProperties() {
        return dataTypeManager
                .withDataType(getDataType())
                .withName(getOldName())
                .withType(getOldType())
                .withConstraint(getOldConstraint())
                .asList(getOldIsList())
                .get();
    }

    void closeEditMode() {

        view.hideDataTypeNameInput();
        view.hideConstraintContainer();
        view.hideListContainer();
        view.showEditButton();
        view.showConstraintText();
        view.showKebabMenu();
        view.disableFocusMode();

        refreshListYesLabel();

        dataTypeSelectComponent.disableEditMode();
    }

    void refreshListYesLabel() {
        if (getDataType().isList()) {
            view.showListYesLabel();
        } else {
            view.hideListYesLabel();
        }
    }

    public void remove() {
        confirmation.ifIsNotReferencedDataType(getDataType(), doRemove());
    }

    Command doRemove() {
        return () -> {

            final List<DataType> destroyedDataTypes = getDataType().destroy();
            final List<DataType> removedDataTypes = removeTopLevelDataTypes(destroyedDataTypes);

            destroyedDataTypes.removeAll(removedDataTypes);

            dataTypeList.refreshItemsByUpdatedDataTypes(destroyedDataTypes);
        };
    }

    List<DataType> removeTopLevelDataTypes(final List<DataType> destroyedDataTypes) {
        return destroyedDataTypes.stream()
                .filter(dataType -> dataType.isTopLevel() && (isDestroyedDataType(dataType) || isAReferenceToDestroyedDataType(dataType)))
                .peek(dataTypeList::removeItem)
                .collect(Collectors.toList());
    }

    private boolean isDestroyedDataType(final DataType dataType) {
        return Objects.equals(dataType.getUUID(), getDataType().getUUID());
    }

    private boolean isAReferenceToDestroyedDataType(final DataType dataType) {
        return getDataType().isTopLevel() && Objects.equals(dataType.getType(), getDataType().getName());
    }

    DataType updateProperties(final DataType dataType) {
        return dataTypeManager
                .from(dataType)
                .withName(view.getName())
                .withType(dataTypeSelectComponent.getValue())
                .withConstraint(dataTypeConstraintComponent.getValue())
                .asList(dataTypeListComponent.getValue())
                .get();
    }

    String getOldName() {
        return oldName;
    }

    String getOldType() {
        return oldType;
    }

    String getOldConstraint() {
        return oldConstraint;
    }

    boolean getOldIsList() {
        return oldIsList;
    }

    DataTypeList getDataTypeList() {
        return dataTypeList;
    }

    public void insertFieldAbove() {

        closeEditMode();

        final DataType newDataType = newDataType();
        final String referenceDataTypeHash = dataTypeList.calculateParentHash(getDataType());
        final List<DataType> updatedDataTypes = newDataType.create(getDataType(), ABOVE);

        if (newDataType.isTopLevel()) {
            dataTypeList.insertAbove(newDataType, getDataType());
        } else {
            dataTypeList.refreshItemsByUpdatedDataTypes(updatedDataTypes);
        }

        dataTypeList.enableEditMode(getNewDataTypeHash(newDataType, referenceDataTypeHash));
    }

    public void insertFieldBelow() {

        closeEditMode();

        final DataType newDataType = newDataType();
        final String referenceDataTypeHash = dataTypeList.calculateParentHash(getDataType());
        final List<DataType> updatedDataTypes = newDataType.create(getDataType(), BELOW);

        if (newDataType.isTopLevel()) {
            dataTypeList.insertBelow(newDataType, getDataType());
        } else {
            dataTypeList.refreshItemsByUpdatedDataTypes(updatedDataTypes);
        }

        dataTypeList.enableEditMode(getNewDataTypeHash(newDataType, referenceDataTypeHash));
    }

    public void insertNestedField() {

        closeEditMode();
        expand();

        final DataType newDataType = newDataType();
        final String referenceDataTypeHash = dataTypeList.calculateHash(getDataType());
        final List<DataType> updatedDataTypes = newDataType.create(getDataType(), NESTED);

        dataTypeList.refreshItemsByUpdatedDataTypes(updatedDataTypes);
        dataTypeList.enableEditMode(getNewDataTypeHash(newDataType, referenceDataTypeHash));
    }

    String getNewDataTypeHash(final DataType newDataType,
                              final String referenceDataTypeHash) {
        return Stream
                .of(referenceDataTypeHash, newDataType.getName())
                .filter(s -> !isEmpty(s))
                .collect(Collectors.joining("."));
    }

    private DataType newDataType() {
        return dataTypeManager.fromNew().get();
    }

    public interface View extends UberElemental<DataTypeListItem> {

        void setDataType(final DataType dataType);

        void toggleArrow(final boolean show);

        void expand();

        void collapse();

        void showEditButton();

        void showSaveButton();

        void setupSelectComponent(final DataTypeSelect typeSelect);

        void setupConstraintComponent(final DataTypeConstraint dataTypeConstraintComponent);

        void setupListComponent(final SmallSwitchComponent dataTypeListComponent);

        void showListContainer();

        void hideListContainer();

        void showListYesLabel();

        void hideListYesLabel();

        void showConstraintText();

        void hideConstraintText();

        boolean isCollapsed();

        void hideDataTypeNameInput();

        void setConstraint(final String constraint);

        void showDataTypeNameInput();

        void enableFocusMode();

        void disableFocusMode();

        boolean isOnFocusMode();

        String getName();

        void setName(final String name);

        void showConstraintContainer();

        void hideConstraintContainer();

        void hideKebabMenu();

        void showKebabMenu();
    }
}
