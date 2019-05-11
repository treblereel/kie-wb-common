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

package org.kie.workbench.common.stunner.client.widgets.canvas.actions;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLTextAreaElement;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.SinkNative;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.stunner.core.client.canvas.controls.actions.InLineTextEditorBox;
import org.uberfire.mvp.Command;

@Templated(value = "TextEditorInLineBox.html", stylesheet = "TextEditorInLineBox.css")
@InLineTextEditorBox
public class TextEditorInLineBoxView extends AbstractTextEditorBoxView
        implements TextEditorBoxView,
                   IsElement {

    @Inject
    @DataField
    HTMLTextAreaElement nameField;

    @Inject
    public TextEditorInLineBoxView(final TranslationService translationService) {
        super();
        this.translationService = translationService;
    }

    TextEditorInLineBoxView(final TranslationService translationService,
                            final Div editNameBox,
                            final HTMLTextAreaElement nameField,
                            final Command showCommand,
                            final Command hideCommand,
                            final HTMLElement closeButton,
                            final HTMLElement saveButton) {
        super(showCommand, hideCommand, closeButton, saveButton);
        this.translationService = translationService;
        this.nameField = nameField;
        super.editNameBox = editNameBox;
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        nameField.addEventListener("input", event -> grow(), false);
    }

    @Override
    public void init(TextEditorBoxView.Presenter presenter) {
        super.presenter = presenter;
    }

    @Override
    public void show(final String name) {
        nameField.value = name;

        grow();
        setVisible();
    }

    public void setWidth(double width) {
        nameField.style.width = CSSProperties.WidthUnionType.of(width + "px");
        //nameField.getStyle().setProperty("width", width + "px");

        GWT.log("got width " + width);
    }

    public void setHeight(double height) {
        nameField.style.height = CSSProperties.HeightUnionType.of(height + "px");

        //nameField.getStyle().setProperty("height", height + "px");

        GWT.log("got height " + height);
    }

    public void setFontSize(double fontSize) {
        GWT.log("TextEditorInLineBoxView setFontSize " + fontSize);

        nameField.style.fontSize = CSSProperties.FontSizeUnionType.of(fontSize+"px");
    }

    public void setFontFamily(String fontFamily) {
        nameField.style.fontFamily = fontFamily;
    }

    public void setFontStyle(String fontStyle) {
        nameField.style.fontStyle = fontStyle;
    }

    @EventHandler("nameField")
    //@SinkNative(Event.ONCHANGE | Event.ONKEYPRESS | Event.ONKEYDOWN)
    @SinkNative(Event.ONCHANGE | Event.ONKEYPRESS | Event.ONKEYDOWN | Event.ONKEYUP)
    public void onChangeName(Event event) {
        switch (event.getTypeInt()) {
            case Event.ONCHANGE:
                presenter.onChangeName(nameField.value);
                break;
            case Event.ONKEYUP:
                grow();
                break;
            case Event.ONKEYPRESS:
                presenter.onKeyPress(event.getKeyCode(),
                                     false,
                                     nameField.value);
                break;
            case Event.ONKEYDOWN:
                //Defer processing of KeyDownEvent until after KeyPress has been processed as we write the value to the Presenter from the TextInput.
                scheduleDeferredCommand(() -> presenter.onKeyDown(event.getKeyCode(), nameField.value));
        }
    }

    void grow() {
        GWT.log("height " + nameField.style.height);
        GWT.log("scrollHeight " + nameField.scrollHeight);
        nameField.style.height = CSSProperties.HeightUnionType.of("auto");
        setHeight(nameField.scrollHeight);
    }
}
