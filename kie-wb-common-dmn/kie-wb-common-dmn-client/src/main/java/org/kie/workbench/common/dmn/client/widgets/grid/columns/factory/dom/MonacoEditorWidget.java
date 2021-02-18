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

package org.kie.workbench.common.dmn.client.widgets.grid.columns.factory.dom;

import com.google.gwt.dom.client.Document;
import elemental2.dom.Element;
import jsinterop.base.Js;
import org.gwtbootstrap3.client.ui.base.TextBoxBase;
import org.kie.workbench.common.dmn.client.widgets.codecompletion.MonacoPropertiesFactory;
import org.uberfire.client.views.pfly.monaco.jsinterop.MonacoEditor;
import org.uberfire.client.views.pfly.monaco.jsinterop.MonacoStandaloneCodeEditor;

public class MonacoEditorWidget extends TextBoxBase {

    private final MonacoPropertiesFactory monacoPropertiesFactory = new MonacoPropertiesFactory();
    private final MonacoEditor monacoEditor = MonacoEditor.get();
    private final MonacoStandaloneCodeEditor codeEditor;

    public MonacoEditorWidget() {
        super(Document.get().createDivElement());
        codeEditor = monacoEditor.create(getMonacoEditorWidgetElement(),
                                         monacoPropertiesFactory.getConstructionOptions());
    }

    /**
     * for testing
     */
    public Element getMonacoEditorWidgetElement() {
        return Js.uncheckedCast(getElement());
    }

    /**
     * for testing
     */
    MonacoEditorWidget(MonacoStandaloneCodeEditor codeEditor) {
        super(Document.get().createDivElement());
        this.codeEditor = codeEditor;
    }

    public MonacoStandaloneCodeEditor getCodeEditor() {
        return codeEditor;
    }

    @Override
    public String getValue() {
        return codeEditor.getValue() != null ? codeEditor.getValue() : "";
    }

    public void setValue(final String value) {
        codeEditor.setValue(value);
    }

    @Override
    public void setFocus(final boolean focused) {
        if (focused) {
            codeEditor.focus();
        }
    }

    @Override
    public void setTabIndex(final int index) {
        // IStandaloneCodeEditor(codeEditor) does not support tab index.
        // https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandalonecodeeditor.html
    }

    public void dispose() {
        codeEditor.dispose();
    }
}
