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

package org.kie.workbench.common.dmn.client.editors.types.shortcuts;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import elemental2.dom.Element;
import org.kie.workbench.common.dmn.client.editors.types.listview.DataTypeList;
import org.kie.workbench.common.dmn.client.editors.types.listview.DataTypeListItem;
import org.uberfire.client.mvp.HasPresenter;

public class DataTypeListShortcuts {

    private DataTypeList dataTypeList;

    private View view;

    @Inject
    public DataTypeListShortcuts(final View view) {
        this.view = view;
    }

    @PostConstruct
    public void init() {
        view.init(this);
    }

    public void init(final DataTypeList dataTypeList) {
        this.dataTypeList = dataTypeList;
    }

    void onArrowDown() {
        view.getNextDataTypeRow().ifPresent(view::highlight);
    }

    void onArrowUp() {
        view.getPrevDataTypeRow().ifPresent(view::highlight);
    }

    void onTab() {
        view.getFirstDataTypeRow().ifPresent(view::highlight);
    }

    void onArrowLeft() {
        getCurrentDataTypeListItem().ifPresent(DataTypeListItem::collapse);
    }

    void onArrowRight() {
        getCurrentDataTypeListItem().ifPresent(DataTypeListItem::expand);
    }

    void onCtrlE() {
        getCurrentDataTypeListItem().ifPresent(DataTypeListItem::enableEditMode);
    }

    void onEscape() {

        final Optional<DataTypeListItem> currentDataTypeListItem = getCurrentDataTypeListItem();

        if (currentDataTypeListItem.isPresent()) {
            currentDataTypeListItem.get().disableEditMode();
        } else {
            getVisibleDataTypeListItems().forEach(DataTypeListItem::disableEditMode);
        }

        view.reset();
    }

    void onCtrlBackspace() {
        getCurrentDataTypeListItem().ifPresent(DataTypeListItem::remove);
    }

    void onCtrlS() {
        getFocusedDataTypeListItem().ifPresent(DataTypeListItem::saveAndCloseEditMode);
    }

    void onCtrlB() {
        getCurrentDataTypeListItem().ifPresent(DataTypeListItem::insertNestedField);
    }

    void onCtrlU() {
        getCurrentDataTypeListItem().ifPresent(DataTypeListItem::insertFieldAbove);
    }

    void onCtrlD() {
        getCurrentDataTypeListItem().ifPresent(DataTypeListItem::insertFieldBelow);
    }

    DataTypeList getDataTypeList() {
        return dataTypeList;
    }

    private List<DataTypeListItem> getVisibleDataTypeListItems() {
        return view.getVisibleDataTypeListItems();
    }

    private Optional<DataTypeListItem> getCurrentDataTypeListItem() {
        return view.getCurrentDataTypeListItem();
    }

    private Optional<DataTypeListItem> getFocusedDataTypeListItem() {
        return view.getFocusedDataTypeListItem();
    }

    public void reset() {
        view.reset();
    }

    public interface View extends HasPresenter<DataTypeListShortcuts> {

        void reset();

        List<DataTypeListItem> getVisibleDataTypeListItems();

        Optional<DataTypeListItem> getCurrentDataTypeListItem();

        Optional<DataTypeListItem> getFocusedDataTypeListItem();

        Optional<Element> getFirstDataTypeRow();

        Optional<Element> getNextDataTypeRow();

        Optional<Element> getPrevDataTypeRow();

        void highlight(final Element element);
    }
}
